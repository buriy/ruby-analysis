package com.yoursway.sadr.ddp;

public interface SubgoalAccessor {
	
	<T> T goalResult(Goal<T> goal);
	
}
