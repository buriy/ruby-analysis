package com.yoursway.sadr.newruby.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private static Server server;
	private static int DEFAULT_PORT = 5402;

	private ServerSocket serverSocket;
	private Project project;
	private final int port;
	private final String root;

	public Server(String root, int port) {
		this.root = root;
		this.port = port;
		loadProject();
		startServer();
	}
	
	private void loadProject() {
		try {
			project = new Project(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startServer() {
		try {
		    serverSocket = new ServerSocket(port);
		    while (true) {
		    	Socket client = serverSocket.accept();
		    	Command command = Command.build(client.getInputStream());
		    	if(Command.EXIT.equals(command.action())){
		    		break;
		    	}
		    	String output = run(command);
		    	OutputStream stream = client.getOutputStream();
		    	OutputStreamWriter writer = new OutputStreamWriter(stream);
		    	writer.write(output);
		    	writer.close();
		    }
		} catch (IOException e) {
		    System.out.println("Could not listen on port: "+port);
		    System.exit(-1);
		}
	}
	
	private String run(Command command) {
		if(command.action().equals(Command.RELOAD)){
			boolean result = project.reloadSettings();
			return result?"OK":"FAILED";
		}
		if(command instanceof CompletionCommand){
			CompletionCommand completionCommand = (CompletionCommand) command;
			System.out.println(command);
			final StringBuilder builder = new StringBuilder();
			final List<String> results = new ArrayList<String>();
			project.complete(completionCommand, new CompletionProposalAcceptor(){
				void addResult(String result, String description){
					builder.append(result+"\t"+description+"\n");
					System.out.println("Result:"+result+"\t"+description);
				}
			});
			return builder.toString();
		}
		return "FAILED";
	}

	public static void main(String[] args) {
		if(args.length<2){
			System.err.println("Usage: server <project_root> [<port>]");
		}else{
			String root = args[1];
			if(args.length>2){
				server = new Server(root, Integer.parseInt(args[0]));
			}else{
				server = new Server(root, DEFAULT_PORT);
			}
		}
	}

}
