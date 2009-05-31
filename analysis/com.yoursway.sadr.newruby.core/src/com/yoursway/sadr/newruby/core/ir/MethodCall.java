package com.yoursway.sadr.newruby.core.ir;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;


public class MethodCall extends Call {
	
	private final VariableReference receiver;	
	
	private final String selector;
	private final VariableReference variableSelector;
	
	private final List<VariableReference> arguments;
	private final RubyBlock blockArgument;
	
	public MethodCall(VariableReference receiver, String selector, List<VariableReference> arguments, RubyBlock blockArg) {
		this.receiver = receiver;
		this.selector = selector;
		blockArgument = blockArg;
		this.variableSelector = null;
		this.arguments = arguments;
	}
	
	public MethodCall(VariableReference receiver, VariableReference variableSelector, List<VariableReference> arguments, RubyBlock blockArg) {
		this.receiver = receiver;
		this.variableSelector = variableSelector;
		blockArgument = blockArg;
		this.selector = null;
		this.arguments = arguments;
	}

	public Set<VariableReference> uses() {
		Set<VariableReference> result = new HashSet<VariableReference>();
		result.add(receiver());
		result.addAll(arguments());
		if (variableSelector() != null) {
			result.add(variableSelector());
		}
		return result;
	}

	public VariableReference receiver() {
		return receiver;
	}

	public String name() {
		return selector;
	}

	public VariableReference variableSelector() {
		return variableSelector;
	}

	public List<VariableReference> arguments() {
		return arguments;
	}

	public RubyBlock blockArgument() {
		return blockArgument;
	}

	public boolean hasSingleLiteralArgument() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RubyBlock passedBlock() {
		// TODO Auto-generated method stub
		return null;
	}

	public void visit(IRVisitor visitor) {
		visitor.visitMethodCall(this);
	}
	
}
