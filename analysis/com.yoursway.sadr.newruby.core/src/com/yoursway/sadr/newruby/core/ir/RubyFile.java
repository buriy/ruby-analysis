package com.yoursway.sadr.newruby.core.ir;

import java.io.File;

import org.jruby.ast.Node;
import org.jruby.ast.RootNode;

import com.yoursway.sadr.newruby.core.cfg.AstToCFGTransformer;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.util.Graph;

public class RubyFile extends CodeBlock {
	
	private File file;
	
	public RubyFile(File file, Node ast) {
		super(ast);
	}

	public File file() {
		return file;
	}
	
	protected void buildCFG() {		
		Node ast = ast();
		if (ast instanceof RootNode) {
			Graph<CFGNode> fileCFG = AstToCFGTransformer.fileCFG(this, (RootNode) ast);
			this.cfg().setFrom(fileCFG);
		} else {
			throw new AssertionError("ast isn't an instance of RootNode");
		}
		
	}
	
}
