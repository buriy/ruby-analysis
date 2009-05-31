package com.yoursway.sadr.newruby.core;

import com.yoursway.sadr.newruby.core.cfg.FileEntryNode;
import com.yoursway.sadr.newruby.core.cfg.FileExitNode;
import com.yoursway.sadr.newruby.core.ir.IRVisitor;
import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.MethodCall;
import com.yoursway.sadr.newruby.core.ir.RubyBlock;
import com.yoursway.sadr.newruby.core.ir.RubyFile;
import com.yoursway.sadr.newruby.core.ir.SuperCall;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.YieldCall;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.BreakInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.IfElseInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.IncludeInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.InstanceMethodDefinition;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.NextInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.RedoInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.RequireInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.ReturnInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.SingletonMethodDefinition;

public abstract class AbstractIRVisitor implements IRVisitor {

	public void visitAssignement(Assignment assignment) {
		// TODO Auto-generated method stub
		
	}

	public void visitBreak(BreakInstruction breakInstruction) {
		// TODO Auto-generated method stub
		
	}

	public void visitFile(RubyFile file) {
		// TODO Auto-generated method stub
		
	}

	public void visitFileEntry(FileEntryNode fileEntryNode) {
		// TODO Auto-generated method stub
		
	}

	public void visitFileExit(FileExitNode fileExitNode) {
		// TODO Auto-generated method stub
		
	}

	public void visitIf(IfElseInstruction ifElseInstruction) {
		// TODO Auto-generated method stub
		
	}

	public void visitInclude(IncludeInstruction include) {
		// TODO Auto-generated method stub
		
	}

	public void visitInstanceMethodDefinition(
			InstanceMethodDefinition instanceMethodDefinition) {
		// TODO Auto-generated method stub
		
	}

	public void visitNext(NextInstruction nextInstruction) {
		// TODO Auto-generated method stub
		
	}

	public void visitRedo(RedoInstruction redoInstruction) {
		// TODO Auto-generated method stub
		
	}

	public void visitRequire(RequireInstruction requireInstruction) {
		// TODO Auto-generated method stub
		
	}

	public void visitReturn(ReturnInstruction returnInstruction) {
		// TODO Auto-generated method stub
		
	}

	public void visitRubyBlock(RubyBlock rubyBlock) {
		// TODO Auto-generated method stub
		
	}

	public void visitSingletonMethodDefinition(
			SingletonMethodDefinition singletonMethodDefinition) {
		// TODO Auto-generated method stub
		
	}

	public void visitLiteral(Literal literal) {
		// TODO Auto-generated method stub
		
	}

	public void visitMethodCall(MethodCall methodCall) {
		// TODO Auto-generated method stub
		
	}

	public void visitSuperCall(SuperCall superCall) {
		// TODO Auto-generated method stub
		
	}

	public void visitVariable(VariableReference variableReference) {
		// TODO Auto-generated method stub
		
	}

	public void visitYieldCall(YieldCall yieldCall) {
		// TODO Auto-generated method stub
		
	}

}
