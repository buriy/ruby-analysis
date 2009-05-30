package com.yoursway.sadr.ddp;

public abstract class AbstractGoal<T> implements Goal<T> {

	protected T result;
	protected SubgoalAccessor engine;
	
	public void evaluate(SubgoalAccessor engine) {
		this.engine = engine;
		evaluate();		
	}
	
	protected <V> V resultOf(Goal<V> goal) {
		return engine.goalResult(goal);
	}
	
	protected abstract void evaluate();
	
	public T result() {
		return result;
	}
	
}
