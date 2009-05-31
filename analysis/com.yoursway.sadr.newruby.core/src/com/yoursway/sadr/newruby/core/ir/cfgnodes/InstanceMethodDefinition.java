package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.CodeBlock;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.IRVisitor;
import com.yoursway.sadr.newruby.core.ir.LocalVarReference;
import com.yoursway.sadr.newruby.core.ir.MethodArguments;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class InstanceMethodDefinition implements CFGNode, MethodDefinition {

	private final ConstantReference classDefinition;
	private final String methodName;
	private final MethodArguments args;
	private final CodeBlock code;
	
	public InstanceMethodDefinition(ConstantReference classDefinition, String methodName,
			MethodArguments args, CodeBlock code) {
		super();
		this.classDefinition = classDefinition;
		this.methodName = methodName;
		this.args = args;
		this.code = code;
	}

	public ConstantReference classDefinition() {
		return classDefinition;
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
		return Collections.emptySet();
	}

	public void visit(IRVisitor visitor) {
		visitor.visitInstanceMethodDefinition(this);
		code.visit(visitor);
	}
	
	public void index() {
		
	}
	
}
