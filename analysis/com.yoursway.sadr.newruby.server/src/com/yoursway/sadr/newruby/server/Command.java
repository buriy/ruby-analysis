package com.yoursway.sadr.newruby.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Command {
	public static final String UNKNOWN = "unknown";
	public static final String COMPLETE = "complete";
	public static final String RELOAD = "reload";
	public static final String EXIT = "exit";

	public static Command build(InputStream inputStream) throws IOException {
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String action = reader.readLine();
			if(action == COMPLETE){
				CompletionCommand command = new CompletionCommand();
				String line;
				while((line = reader.readLine()) != null){
					String[] opt = line.split(" ", 1);
					command.addVariable(opt[0], opt[1]);
				}
			}
			if(action == RELOAD)
				return new SimpleCommand(action);
			if(action == EXIT)
				return new SimpleCommand(action);
			return new SimpleCommand(UNKNOWN);
		}finally{
			inputStream.close();
		}
	}

	public abstract String action();
}
