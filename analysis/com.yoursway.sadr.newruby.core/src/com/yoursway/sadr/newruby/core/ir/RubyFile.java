package com.yoursway.sadr.newruby.core.ir;

import java.io.File;

import org.jruby.ast.Node;
import org.jruby.ast.RootNode;

import com.yoursway.sadr.newruby.core.RubyAnalyzer;
import com.yoursway.sadr.newruby.core.cfg.AstToCFGTransformer;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.util.Graph;

public class RubyFile extends CodeBlock {
	
	private File file;
	private final RubyAnalyzer rubyAnalyzer;
	
	public RubyFile(RubyAnalyzer rubyAnalyzer, File file, Node ast) {
		super(ast);
		this.rubyAnalyzer = rubyAnalyzer;
		buildCFG();
	}

	public File file() {
		return file;
	}
	
	private void buildCFG() {		
		Node ast = ast();
		if (ast instanceof RootNode) {
			Graph<CFGNode> fileCFG = AstToCFGTransformer.fileCFG(this, (RootNode) ast);
			this.cfg().setFrom(fileCFG);
		} else {
			throw new AssertionError("ast isn't an instance of RootNode");
		}
		
	}
	
	public void visit(IRVisitor visitor) {
		visitor.visitFile(this);
		super.visit(visitor);
	}

	public String name() {
		return file.getAbsolutePath();
	}

	public void index() {
		// TODO Auto-generated method stub
		
	}
	
}
