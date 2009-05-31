package com.yoursway.sadr.newruby.core.ir;

import org.jruby.ast.Node;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.util.Graph;

public class CodeBlock {

	private Graph<CFGNode> cfg = new Graph<CFGNode>();
	private Node ast;
	
	public CodeBlock(Node ast) {
		this.ast = ast;
	}

	public Graph<CFGNode> cfg() {
		return cfg;
	}

	public Node ast() {
		return ast;
	}

	public void visit(IRVisitor visitor) {
		for (CFGNode n : cfg().nodes()) {
			n.visit(visitor);
		}
	}
	
}
