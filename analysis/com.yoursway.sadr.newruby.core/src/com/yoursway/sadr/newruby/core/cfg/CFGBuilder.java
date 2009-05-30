package com.yoursway.sadr.newruby.core.cfg;

import java.util.Set;

public interface CFGBuilder {

	void appendNode(CFGNode node);

	int setMarker();

	CFGNode marker(int no);

	Set<CFGNode> lastNodes();

	void setLastNodes(Set<CFGNode> nodes);

	void addEdge(CFGNode a, CFGNode b);

	void bindLastNodesTo(CFGNode n); // addEdge(x, n); x in lastNodes.
	
}
