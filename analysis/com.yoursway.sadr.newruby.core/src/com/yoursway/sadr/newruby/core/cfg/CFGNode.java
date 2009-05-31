package com.yoursway.sadr.newruby.core.cfg;

import com.yoursway.sadr.newruby.core.ir.IRVisitor;
import com.yoursway.sadr.newruby.core.ir.RubyFile;

public interface CFGNode extends UsesContainer {

	RubyFile file();
	
	void visit(IRVisitor visitor);
	
}
