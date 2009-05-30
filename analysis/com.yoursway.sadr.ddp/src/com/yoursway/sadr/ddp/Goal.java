package com.yoursway.sadr.ddp;

public interface Goal<T> {

	T result();
	
	void evaluate(SubgoalAccessor engine);
	
}
