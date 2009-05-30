package com.yoursway.sadr.newruby.core.types;

import com.yoursway.sadr.newruby.core.ir.Literal;

public class LiteralType implements TypeBase {


	private final Literal literal;

	public LiteralType(Literal literal) {
		this.literal = literal;
	}

	public Literal literal() {
		return literal;
	}



}
