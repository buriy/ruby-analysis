package com.yoursway.sadr.newruby.core.cfg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.yoursway.sadr.newruby.core.util.Graph;

public class CFGBuilderImpl implements CFGBuilder {
	
	Graph<CFGNode> graph = new Graph<CFGNode>();
	Set<CFGNode> lastNodes = new HashSet<CFGNode>();
	int currentMarker = -1;	
	Map<Integer, CFGNode> markered = new HashMap<Integer, CFGNode>();

	public CFGBuilderImpl(CFGNode entryNode) {
		lastNodes.add(entryNode);
	}
	
	public void addEdge(CFGNode a, CFGNode b) {
		graph.addEdge(a, b);
	}

	public void appendNode(CFGNode node) {
		if (currentMarker != -1) {
			markered.put(currentMarker, node);
			currentMarker = -1;
		}
		for (CFGNode n : lastNodes)
			addEdge(n, node);
		lastNodes.clear();
		lastNodes.add(node);
	}

	public void bindLastNodesTo(CFGNode node) {
		for (CFGNode n : lastNodes)
			addEdge(n, node);
	}

	public Set<CFGNode> lastNodes() {
		return lastNodes;
	}

	public void setLastNodes(Set<CFGNode> nodes) {
		lastNodes.clear();
		lastNodes.addAll(nodes);
	}

	public int setMarker() {
		currentMarker = markered.size();
		return currentMarker;
	}
	
	public CFGNode marker(int no) {
		return markered.get(no);
	}

	public Graph<CFGNode> graph() {
		return graph;
	}
	
}
