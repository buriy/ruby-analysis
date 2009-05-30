package com.yoursway.sadr.newruby.core.ir;

import org.jruby.ast.IterNode;
import org.jruby.ast.Node;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;
import com.yoursway.sadr.newruby.core.util.Graph;

public class RubyBlock extends CodeBlock implements Callable {

	// TODO: args
	
	public RubyBlock(Node ast) {
		super(ast);
	}

	public RubyBlock(IterNode iterNode, Graph<CFGNode> blockCFG) {
		super(iterNode);
		cfg().setFrom(blockCFG);
	}

}
