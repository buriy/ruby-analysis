package com.yoursway.sadr.newruby.server;

public class SimpleCommand extends Command {

	private final String action;

	public SimpleCommand(String command) {
		this.action = command;
	}

	@Override
	public String toString() {
		return "SimpleCommand("+this.action+")";
	}

	@Override
	public String action() {
		return action;
	}
}
