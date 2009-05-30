package com.yoursway.sadr.newruby.core.ir;

import com.yoursway.sadr.newruby.core.valuesandtypes.RubyClass;
import com.yoursway.sadr.newruby.core.valuesandtypes.ValueSet;

public abstract class Context {

	private final RubyClass klass;
	private final ValueSet self;

	public Context(RubyClass klass, ValueSet self) {
		this.klass = klass;
		this.self = self;
	}

	public RubyClass currentClass() {
		return klass;
	}

	public ValueSet self() {
		return self;
	}

}
