package com.yoursway.sadr.newruby.core.goals.patterns;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;

public interface INodePattern {

	boolean matches(CFGNode node);
	
}
