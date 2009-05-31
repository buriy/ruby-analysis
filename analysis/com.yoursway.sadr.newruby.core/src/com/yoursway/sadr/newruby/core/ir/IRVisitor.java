package com.yoursway.sadr.newruby.core.ir;

import com.yoursway.sadr.newruby.core.cfg.FileEntryNode;
import com.yoursway.sadr.newruby.core.cfg.FileExitNode;
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

public interface IRVisitor {

	void visitFile(RubyFile file);

	void visitAssignement(Assignment assignment);

	void visitBreak(BreakInstruction breakInstruction);

	void visitFileEntry(FileEntryNode fileEntryNode);

	void visitFileExit(FileExitNode fileExitNode);

	void visitIf(IfElseInstruction ifElseInstruction);

	void visitInclude(IncludeInstruction include);

	void visitRequire(RequireInstruction requireInstruction);

	void visitInstanceMethodDefinition(
			InstanceMethodDefinition instanceMethodDefinition);

	void visitNext(NextInstruction nextInstruction);

	void visitRedo(RedoInstruction redoInstruction);

	void visitReturn(ReturnInstruction returnInstruction);

	void visitSingletonMethodDefinition(
			SingletonMethodDefinition singletonMethodDefinition);

	void visitRubyBlock(RubyBlock rubyBlock);

	void visitMethodCall(MethodCall methodCall);

	void visitSuperCall(SuperCall superCall);

	void visitYieldCall(YieldCall yieldCall);

	void visitLiteral(Literal literal);

	void visitVariable(VariableReference variableReference);
	
}
