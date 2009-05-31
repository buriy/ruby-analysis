package com.yoursway.sadr.newruby.core.ir;

import java.util.Collections;
import java.util.Set;

import org.jruby.ast.Node;


public class Literal implements IRRHS {

	private final Node node;

	public Set<VariableReference> uses() {
		return Collections.emptySet();
	}
	
	public Literal(Node n) {
		this.node = n;
	}

	public void visit(IRVisitor visitor) {
		visitor.visitLiteral(this);
	}

}
