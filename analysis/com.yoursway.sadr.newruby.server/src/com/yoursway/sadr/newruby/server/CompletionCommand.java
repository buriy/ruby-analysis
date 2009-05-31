package com.yoursway.sadr.newruby.server;

import java.util.HashMap;
import java.util.Map;

public class CompletionCommand extends Command {
	public static final String OPT_FILE = "file";
	public static final String OPT_POSITION = "position";
	
	private Map<String, String> variables = new HashMap<String, String>();

	public CompletionCommand() {
	}
	
	public void addVariable(String name, String value) {
		variables.put(name, value);
	}
	
	public String get(String name){
		return variables.get(name);
	}
	
	@Override
	public String action() {
		return "CompletionCommand(" + variables.toString()+")";
	}

}
