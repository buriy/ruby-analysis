package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.CodeBlock;
import com.yoursway.sadr.newruby.core.ir.IRVisitor;
import com.yoursway.sadr.newruby.core.ir.LocalVarReference;
import com.yoursway.sadr.newruby.core.ir.MethodArguments;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class SingletonMethodDefinition implements CFGNode, MethodDefinition {

	private final VariableReference object;
	private final String methodName;
	private final MethodArguments args;
	private final CodeBlock code;
	
	public SingletonMethodDefinition(VariableReference object, String methodName,
			MethodArguments args, CodeBlock code) {
		this.object = object;
		this.methodName = methodName;
		this.args = args;
		this.code = code;
	}

	public String name() {
		return methodName;
	}

	public MethodArguments args() {
		return args;
	}

	public CodeBlock code() {
		return code;
	}

	public Set<VariableReference> uses() {		
		return Collections.singleton(object);
	}

	public VariableReference extendedObject() {
		return object;
	}

	public void visit(IRVisitor visitor) {
		visitor.visitSingletonMethodDefinition(this);
		code.visit(visitor);
	}

	public void index() {
		
	}

}
