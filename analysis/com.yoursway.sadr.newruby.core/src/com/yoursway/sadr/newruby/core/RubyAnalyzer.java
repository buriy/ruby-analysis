package com.yoursway.sadr.newruby.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jruby.CompatVersion;
import org.jruby.Ruby;
import org.jruby.ast.Node;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.parser.Parser;
import org.jruby.parser.ParserConfiguration;
import org.jruby.util.KCode;

import com.yoursway.sadr.ddp.DDPEngine;
import com.yoursway.sadr.ddp.FileChangesProcessor;
import com.yoursway.sadr.ddp.Index;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.MethodCall;
import com.yoursway.sadr.newruby.core.ir.RubyBlock;
import com.yoursway.sadr.newruby.core.ir.RubyFile;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.IncludeInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.InstanceMethodDefinition;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.SingletonMethodDefinition;
import com.yoursway.utils.YsFileUtils;

public class RubyAnalyzer {

	static class ParsingResult {
		Node ast;
		String code;

		String getNodeText(Node n) {
			ISourcePosition pos = n.getPosition();
			return code.substring(pos.getStartOffset(), pos.getEndOffset());
		}

		int lineCount() {
			String[] split = code.split("\n");
			return split.length;
		}
	}

	private static ParsingResult parse(String fileName) throws IOException {
		InputStream is = null;
		ParsingResult r = new ParsingResult();

		is = new FileInputStream(fileName);
		ParserConfiguration config = new ParserConfiguration(KCode.UTF8, 0,
				true, true, CompatVersion.RUBY1_8);
		Ruby ruby = Ruby.newInstance();
		r.ast = new Parser(ruby).parse(fileName, is, null, config);
		r.code = YsFileUtils.readAsString(new File(fileName));

		return r;
	}

	private class Indexer extends AbstractIRVisitor {
		@Override
		public void visitAssignement(Assignment assignment) {
			assigmentsIndex().put(assignment.lhs().name(), assignment, assignment
					.file().name());
		}

		@Override
		public void visitInclude(IncludeInstruction include) {
			includesIndex().put((ConstantReference) include.variable(), include,
					include.file().name()); // FIXME
		}

		@Override
		public void visitInstanceMethodDefinition(
				InstanceMethodDefinition instanceMethodDefinition) {
			callablesIndex().put(instanceMethodDefinition.name(),
					instanceMethodDefinition, instanceMethodDefinition.file()
							.name());
			instanceMethodDefinition.index();
		}

		@Override
		public void visitSingletonMethodDefinition(
				SingletonMethodDefinition singletonMethodDefinition) {
			callablesIndex().put(singletonMethodDefinition.name(),
					singletonMethodDefinition, singletonMethodDefinition.file()
							.name());
			singletonMethodDefinition.index();
		}

		@Override
		public void visitRubyBlock(RubyBlock rubyBlock) {
			rubyBlock.index();
		}

		@Override
		public void visitFile(RubyFile file) {
			file.index();
		}

		@Override
		public void visitMethodCall(MethodCall methodCall) {
			callsIndex().put(methodCall.name(), methodCall, methodCall
					.enclosingInstruction().file().name());
		}

		@Override
		public void visitVariable(VariableReference variableReference) {
			CFGNode enclosingInstruction = variableReference
					.enclosingInstruction();
			if (enclosingInstruction.uses().contains(variableReference))
				usagesIndex().put(variableReference.name(), enclosingInstruction,
						enclosingInstruction.file().name());
		}

	}

	private RubyFile entryPoint;
	private List<RubyFile> files = new ArrayList<RubyFile>();

	private DDPEngine engine;
	private FileChangesProcessor fileChangesProcessor;

	private Map<Node, VariableReference> astToIRMapping = new HashMap<Node, VariableReference>();
	private Indexer indexer;

	private final Index<String, Assignment> assigmentsIndex = new Index<String, Assignment>();
	private final Index<String, Call> callsIndex = new Index<String, Call>();
	private final Index<String, Callable> callablesIndex = new Index<String, Callable>();
	private final Index<String, CFGNode> usagesIndex = new Index<String, CFGNode>();
	private final Index<ConstantReference, IncludeInstruction> includesIndex = new Index<ConstantReference, IncludeInstruction>();

	public RubyAnalyzer() {
		engine = new DDPEngine(-1);
		fileChangesProcessor = new FileChangesProcessor(engine);
		engine.setFileChangesProcessor(fileChangesProcessor);
		indexer = new Indexer();
	}

	public RubyFile addFile(File file) throws IOException {
		ParsingResult parsingResult = parse(file.getAbsolutePath());
		RubyFile rubyFile = new RubyFile(this, file, parsingResult.ast);
		files.add(rubyFile);
		rubyFile.visit(indexer); // TODO: perform iterations to handle define_method, etc
		return rubyFile;
	}

	public void killFile(File file) {
		fileChangesProcessor.fileChanged(file.getAbsolutePath());
	}

	public void setEntryPoint(File file) {
		for (RubyFile f : files) {
			if (f.file().equals(file)) {
				setEntryPoint(f);
				break;
			}
		}
	}

	public VariableReference varByNode(Node node) {
		return astToIRMapping.get(node);
	}

	public void mapIRToAST(VariableReference var, Node node) {
		astToIRMapping.put(node, var);
	}

	public Index<String, Assignment> assigmentsIndex() {
		return assigmentsIndex;
	}

	public Index<String, Call> callsIndex() {
		return callsIndex;
	}

	public Index<String, Callable> callablesIndex() {
		return callablesIndex;
	}

	public Index<String, CFGNode> usagesIndex() {
		return usagesIndex;
	}

	public Index<ConstantReference, IncludeInstruction> includesIndex() {
		return includesIndex;
	}

	public void setEntryPoint(RubyFile entryPoint) {
		this.entryPoint = entryPoint;
	}

	public RubyFile entryPoint() {
		return entryPoint;
	}

}
