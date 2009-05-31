package com.yoursway.sadr.newruby.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.util.Graph;

public class DefUseBuilder {

	public static class Use {
		CFGNode node;
		VariableReference var;

		Use(CFGNode node, VariableReference var) {
			this.node = node;
			this.var = var;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			result = prime * result + ((var == null) ? 0 : var.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Use other = (Use) obj;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (!node.equals(other.node))
				return false;
			if (var == null) {
				if (other.var != null)
					return false;
			} else if (!var.equals(other.var))
				return false;
			return true;
		}

	};

	private final Map<Use, Collection<Assignment>> defs = new HashMap<Use, Collection<Assignment>>();
	private final Map<Assignment, Collection<CFGNode>> uses = new HashMap<Assignment, Collection<CFGNode>>();

	private final Map<CFGNode, Map<VariableReference, Set<Assignment>>> reachingDefs = new HashMap<CFGNode, Map<VariableReference, Set<Assignment>>>();

	public DefUseBuilder() {
	}

	public Map<Use, Collection<Assignment>> defs() {
		return defs;
	}

	public Map<Assignment, Collection<CFGNode>> uses() {
		return uses;
	}

	private void updateReachingDefs(Graph<CFGNode> graph, CFGNode v,
			Map<VariableReference, Assignment> currentReachingDefs,
			Map<CFGNode, Boolean> visited) {
		Map<VariableReference, Set<Assignment>> old = reachingDefs.get(v);
		if (old == null) {
			old = new HashMap<VariableReference, Set<Assignment>>();
			reachingDefs.put(v, old);
		}
		boolean changed = false;
		for (Entry<VariableReference, Assignment> e : currentReachingDefs
				.entrySet()) {
			Set<Assignment> oldDefs = old.get(e.getKey());
			if (oldDefs == null) {
				oldDefs = new HashSet<Assignment>();
				old.put(e.getKey(), oldDefs);
			}
			if (!oldDefs.contains(e.getValue())) {
				changed = true;
				oldDefs.add(e.getValue());
			}
		}
		if (visited.get(v) && !changed)
			return;

		visited.put(v, true);

		if (v instanceof Assignment) {
			Assignment ass = (Assignment) v;
			currentReachingDefs = Maps.newHashMap(currentReachingDefs);
			currentReachingDefs.put(ass.lhs(), ass);
		}

		for (CFGNode u : graph.succ(v)) {
			updateReachingDefs(graph, u, currentReachingDefs, visited);
		}

		visited.put(v, false);
	}

	private void addUse(Assignment s, CFGNode n) {
		Collection<CFGNode> c = uses.get(s);
		if (c == null) {
			c = new HashSet<CFGNode>();
			uses.put(s, c);
		}
		c.add(n);
	}

	private void addDef(CFGNode v, VariableReference var, Assignment def) {
		Use use = new Use(v, var);
		Collection<Assignment> c = defs.get(use);
		if (c == null) {
			c = new HashSet<Assignment>();
			defs.put(use, c);
		}
		c.add(def);
	}

	public void build(Graph<CFGNode> graph) {
		Map<VariableReference, Assignment> currentReachingDefs = Maps
				.newHashMap();
		Map<CFGNode, Boolean> visited = Maps.newHashMap();
		updateReachingDefs(graph, graph.entryPoint(), currentReachingDefs,
				visited);

		for (CFGNode v : graph.nodes()) {
			Map<VariableReference, Set<Assignment>> rd = reachingDefs.get(v);
			Set<VariableReference> vUses = v.uses();
			for (VariableReference var : vUses) {
				Set<Assignment> set = rd.get(var);
				if (set != null) {
					for (Assignment s : set) {
						addUse(s, v);
						addDef(v, var, s);
					}
				}
			}
		}
	}

}
