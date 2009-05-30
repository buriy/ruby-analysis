package com.yoursway.sadr.newruby.core.ir;

import java.util.ArrayList;
import java.util.List;

public final class AnalysisScope {

	private RubyFile entryPoint;
	private List<RubyFile> files = new ArrayList<RubyFile>();
	
	public void addFile(RubyFile file) {
		files.add(file);
	}

	public void setEntryPoint(RubyFile entryPoint) {
		this.entryPoint = entryPoint;
	}

	public RubyFile entryPoint() {
		return entryPoint;
	}
	

	
}
