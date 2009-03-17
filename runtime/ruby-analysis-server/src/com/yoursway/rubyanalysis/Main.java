package com.yoursway.rubyanalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jruby.CompatVersion;
import org.jruby.Ruby;
import org.jruby.ast.ClassVarNode;
import org.jruby.ast.Colon3Node;
import org.jruby.ast.ConstNode;
import org.jruby.ast.DVarNode;
import org.jruby.ast.GlobalVarNode;
import org.jruby.ast.InstVarNode;
import org.jruby.ast.LocalVarNode;
import org.jruby.ast.Node;
import org.jruby.ast.SelfNode;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.parser.Parser;
import org.jruby.parser.ParserConfiguration;
import org.jruby.util.KCode;

import com.yoursway.utils.YsFileUtils;

public class Main {
	
	static class ParsingResult {
		Node ast;
		String code;
		
		String getNodeText(Node n) {
			ISourcePosition pos = n.getPosition();
			return code.substring(pos.getStartOffset(), pos.getEndOffset());
		}
	}

	private static ParsingResult parse(String fileName) {
		InputStream is = null;
		ParsingResult r = new ParsingResult();
		try {
			is = new FileInputStream(fileName);
			ParserConfiguration config = new ParserConfiguration(KCode.UTF8, 0,
					true, true, CompatVersion.RUBY1_8);
			Ruby ruby = Ruby.newInstance();
			r.ast = new Parser(ruby).parse(fileName, is, null, config);
			r.code = YsFileUtils.readAsString(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return r;
	}
	
	private static List<String> collectEvaluatableNames(ParsingResult r, Node n, int lineno) {
		List<String> result = new ArrayList<String>();
		if (n instanceof LocalVarNode) {
			result.add(((LocalVarNode) n).getName());
		} else if (n instanceof InstVarNode) {
			result.add(((InstVarNode) n).getName());
		} else if (n instanceof ClassVarNode) {
			result.add(((ClassVarNode) n).getName());
		} else if (n instanceof SelfNode) {
			result.add(((SelfNode) n).getName());
		} else if (n instanceof GlobalVarNode) {
			result.add(((GlobalVarNode) n).getName());
		} else if (n instanceof ConstNode) {
			result.add(((ConstNode) n).getName());
		} else if (n instanceof Colon3Node) {
			result.add(r.getNodeText(n.childNodes().get(0)) + "::" + ((Colon3Node) n).getName());			
		} else if (n instanceof DVarNode) {
			result.add(((DVarNode) n).getName());
		}
		List<Node> childNodes = n.childNodes();
		for (Node child : childNodes) {
			ISourcePosition position = n.getPosition();
			if (position.getStartLine() <= lineno
					&& position.getEndLine() >= lineno) {
				result.addAll(collectEvaluatableNames(r, child, lineno));
			}
			if (position.getStartLine() > lineno)
				break;
		}		
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParsingResult r = parse("/Users/fourdman/Projects/ruby-analysis/runtime/run.rb");		
		System.out.println(r.ast);
		int linesCount = r.code.split("\n").length;
		for (int i = 0; i < linesCount; i++) {
			System.out.println("line " + (i + 1));
			for (String s : collectEvaluatableNames(r, r.ast, i)) 
				System.out.print(s + " ");
			System.out.println();
		}
	}

}
