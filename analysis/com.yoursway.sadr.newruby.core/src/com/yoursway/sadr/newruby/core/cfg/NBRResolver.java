package com.yoursway.sadr.newruby.core.cfg;

public interface NBRResolver {

	void nextNode(CFGNode node);
	void breakNode(CFGNode node);
	void redoNode(CFGNode node);
	
}
