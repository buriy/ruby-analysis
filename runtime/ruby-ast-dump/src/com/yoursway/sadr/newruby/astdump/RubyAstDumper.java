package com.yoursway.sadr.newruby.astdump;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jruby.CompatVersion;
import org.jruby.Ruby;
import org.jruby.ast.Colon2ImplicitNode;
import org.jruby.ast.Colon2MethodNode;
import org.jruby.ast.DefnNode;
import org.jruby.ast.NewlineNode;
import org.jruby.ast.Node;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.parser.Parser;
import org.jruby.parser.ParserConfiguration;
import org.jruby.util.KCode;

import com.yoursway.utils.YsFileUtils;

public class RubyAstDumper {
	
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
	
	private static void dump(Node node, String indent) {
		System.out.println(indent + node.getClass().getSimpleName());
		indent += "  ";
		for (Node n : node.childNodes())
			dump(n, indent);
	}
	
	public static void main(String[] args) {
		String fileName = "/Users/fourdman/Projects/diploma-workspace/test1.rb";
		try {
			ParsingResult result = parse(fileName);
			dump(result.ast, "");
		} catch (IOException e) {
			System.err.println("Failed to parse file: " + fileName);
		}
	}
}
