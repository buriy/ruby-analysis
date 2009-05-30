package com.yoursway.sadr.ddp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FileChangesProcessor {

	private final DDPEngine engine;
	private final Map<String, Collection<Goal<?>>> fileToGoals = new HashMap<String, Collection<Goal<?>>>();
	private final List<Index<?, ?>> indexes = new ArrayList<Index<?, ?>>();

	public FileChangesProcessor(DDPEngine engine) {
		this.engine = engine;
	}

	public void takeCareOfIndex(Index<?, ?> index) {
		indexes.add(index);
	}

	public void addGoalDependency(Goal<?> goal, String fileName) {
		Collection<Goal<?>> g = fileToGoals.get(fileName);
		if (g == null) {
			g = new HashSet<Goal<?>>();
			fileToGoals.put(fileName, g);
		}
		g.add(goal);
	}

	public void fileChanged(String fileName) {
		Collection<Goal<?>> g = fileToGoals.get(fileName);
		if (g == null)
			return;
		for (Goal<?> goal : g)
			engine.goalAffected(goal);

		for (Index<?, ?> i : indexes)
			i.removeEntriesFrom(fileName);
	}

}
