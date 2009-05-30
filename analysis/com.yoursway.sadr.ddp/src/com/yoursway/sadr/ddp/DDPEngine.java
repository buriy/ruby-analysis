package com.yoursway.sadr.ddp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DDPEngine {

	private final HashMap<Goal<?>, Goal<?>> completed = new HashMap<Goal<?>, Goal<?>>();
	private final HashMap<Goal<?>, Goal<?>> pruned = new HashMap<Goal<?>, Goal<?>>();
	private final LinkedList<Goal<?>> worklist = new LinkedList<Goal<?>>();
	private final HashMap<Goal<?>, Collection<Goal<?>>> dependencies = new HashMap<Goal<?>, Collection<Goal<?>>>();

	private boolean running = false;
	
	FileChangesProcessor fileChangesProcessor = new FileChangesProcessor(this);
	
	class Pruner {
		
		long startTime;
		long timelimit = -1;
		
		void evaluationStarted() {
			startTime = System.currentTimeMillis();
		}
		
		boolean doPrunings() {
			long timeMillis = System.currentTimeMillis();
			if (timeMillis - startTime > timelimit) {
				// kill'em all!
				for (Goal<?> g : worklist)
					pruned.put(g, g);
				worklist.clear();
				return true;
			}				
			return false;
		}
	}
	
	private final Pruner pruner = new Pruner();
	
	class DDPAccessor implements SubgoalAccessor {
		
		private final Goal<?> accessingGoal;

		public DDPAccessor(Goal<?> accessingGoal) {
			this.accessingGoal = accessingGoal;
		}
		
		@SuppressWarnings("unchecked")
		public <T> T goalResult(Goal<T> goal) {
			Collection<Goal<?>> deps = dependencies.get(goal);
			if (deps == null) {
				deps = new ArrayList<Goal<?>>();
				dependencies.put(goal, deps);			
			}
			deps.add(accessingGoal);
			
			if (goal instanceof IFileDependant)
				fileChangesProcessor.addGoalDependency(goal, ((IFileDependant) goal).fileName());
			
			if (pruned.containsKey(goal)) {
				return (T) pruned.get(goal).result();
			} else if (worklist.contains(goal)) {
				return (T) worklist.get(worklist.indexOf(goal)).result();
			} else if (completed.containsKey(goal)) {
				return (T) completed.get(goal).result();
			} else {
				worklist.addLast(goal);
				return goal.result();
			}
		}
		
	}
	
	public void setTimeLimit(long timems) {		
		pruner.timelimit = timems;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T evaluate(Goal<T> goal) {
		running = true;
		Goal<?> goal2 = completed.get(goal);
		if (goal2 != null)
			return (T) goal2.result();
		
		worklist.clear();
		worklist.add(goal);
		
		while (!worklist.isEmpty()) {
			if (!pruner.doPrunings())
				updateNextGoal();
		}
		
		T result = goal.result();
		
		running = false;
		
		return result;
	}

	private void updateNextGoal() {
		Goal<?> g = worklist.poll();
		if (g == null)
			throw new AssertionError("updateNextGoal() called with empty worklist");
		
		if  (update(g)) {
			Collection<Goal<?>> deps = goalsNeeding(g, false);
			worklist.addAll(deps);
			for (Goal<?> d : deps)
				completed.remove(d);
		}		
	}
	
	private Collection<Goal<?>> goalsNeeding(Goal<?> goal, boolean all) {
		Collection<Goal<?>> deps = dependencies.get(goal);
		List<Goal<?>> res = new ArrayList<Goal<?>>();
		if (deps == null) 
			return res;
		if (all)
			return deps;
		for (Goal<?> g : deps)
			if (!pruned.containsKey(goal))
				res.add(g);
		return res;
	}
	
	private boolean update(Goal<?> goal) {
		Object oldResult = goal.result();
		goal.evaluate(new DDPAccessor(goal));
		Object newResult = goal.result();
		return oldResult.equals(newResult);
	}
	
	public <T> void goalAffected(Goal<T> goal) {
		if (running)
			throw new ConcurrentModificationException();
		
		if (update(goal))
			wipe(goal);
	}
	
	private <T> void wipe(Goal<T> goal) {
		completed.remove(goal);
		pruned.remove(goal);
		for (Goal<?> g : goalsNeeding(goal, true))
			wipe(g);
	}
	
}
