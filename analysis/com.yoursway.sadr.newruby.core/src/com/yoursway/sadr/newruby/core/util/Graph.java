package com.yoursway.sadr.newruby.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph<T> {

	private T entryPoint;
	private T exitPoint;
	private Map<T, Set<T>> succs = new HashMap<T, Set<T>>();
	private Map<T, Set<T>> preds = new HashMap<T, Set<T>>();
	
	public Graph() {	
	}
	
	public boolean pathExists(T from, T to) {
		return false;
	}
	
	public Set<List<T>> pathsFromTo(T from, T to) {
		return null;
	}
	
	public void addEdge(T from, T to) {		
		Set<T> s = succs.get(from);
		if (s == null)
			s = new HashSet<T>();
		s.add(to);
		succs.put(from, s);
		
		s = preds.get(to);
		if (s == null)
			s = new HashSet<T>();
		s.add(from);
		preds.put(to, s);
	}
	
	public Set<T> succ(T v) {
		return succs.get(v);
	}
	
	public Set<T> pred(T v) {
		return preds.get(v);
	}

	public void setEntryPoint(T entryPoint) {
		this.entryPoint = entryPoint;
	}

	public T entryPoint() {
		return entryPoint;
	}

	public void setExitPoint(T exitPoint) {
		this.exitPoint = exitPoint;
	}

	public T exitPoint() {
		return exitPoint;
	}

	public void setFrom(Graph<T> result) {		
		this.entryPoint = result.entryPoint;
		this.exitPoint = result.exitPoint;
		this.preds = result.preds;
		this.succs = result.succs;
	}

	public Collection<T> nodes() {
		// TODO Auto-generated method stub
		
	}
	
}
