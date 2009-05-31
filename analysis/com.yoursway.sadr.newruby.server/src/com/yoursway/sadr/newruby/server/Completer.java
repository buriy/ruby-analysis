package com.yoursway.sadr.newruby.server;

public interface Completer {
	/**
	 * Emitted when file was added or updated 
	 */
	void loadFile(String file); 
	
	/**
	 * Emitted when some file is removed
	 */
	void unloadFile(String file);
	
	void complete(String file, int position, CompletionProposalAcceptor acceptor);
}
