package com.yoursway.sadr.newruby.core.cfg;

import java.util.Collections;
import java.util.Set;

import com.yoursway.sadr.newruby.core.ir.IRVisitor;
import com.yoursway.sadr.newruby.core.ir.RubyFile;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public class FileExitNode implements CFGNode {
	
	private final RubyFile file;

	public FileExitNode(RubyFile file) {
		this.file = file;
	}

	public Set<VariableReference> uses() {
		return Collections.emptySet();
	}

	public void visit(IRVisitor visitor) {
		visitor.visitFileExit(this);
	}

}
