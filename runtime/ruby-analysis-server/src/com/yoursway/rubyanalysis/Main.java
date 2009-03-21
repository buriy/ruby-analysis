package com.yoursway.rubyanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jruby.CompatVersion;
import org.jruby.Ruby;
import org.jruby.ast.ClassVarNode;
import org.jruby.ast.Colon3Node;
import org.jruby.ast.ConstNode;
import org.jruby.ast.DVarNode;
import org.jruby.ast.GlobalVarNode;
import org.jruby.ast.InstVarNode;
import org.jruby.ast.LocalVarNode;
import org.jruby.ast.Node;
import org.jruby.ast.SelfNode;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.parser.Parser;
import org.jruby.parser.ParserConfiguration;
import org.jruby.util.KCode;

import com.yoursway.utils.YsFileUtils;

public class Main {

	static class ParsingResult {
		Node ast;
		String code;

		String getNodeText(Node n) {
			ISourcePosition pos = n.getPosition();
			return code.substring(pos.getStartOffset(), pos.getEndOffset());
		}
	}

	private static ParsingResult parse(String fileName) throws IOException {
		InputStream is = null;
		ParsingResult r = new ParsingResult();

		is = new FileInputStream(fileName);
		ParserConfiguration config = new ParserConfiguration(KCode.UTF8, 0,
				true, true, CompatVersion.RUBY1_8);
		Ruby ruby = Ruby.newInstance();
		r.ast = new Parser(ruby).parse(fileName, is, null, config);
		r.code = YsFileUtils.readAsString(new File(fileName));

		return r;
	}
	
	static class EvaluatableItem {
		String evalString;
		Node node;
		public EvaluatableItem(String name, Node node) {
			evalString = name;
			this.node = node;
		}
	}

	private static List<EvaluatableItem> collectEvaluatables(ParsingResult r,
			Node n, int lineno) {
		List<EvaluatableItem> result = new ArrayList<EvaluatableItem>();
		if (n instanceof LocalVarNode) {
			result.add(new EvaluatableItem(((LocalVarNode) n).getName(), n));
		} else if (n instanceof InstVarNode) {
			result.add(new EvaluatableItem(((InstVarNode) n).getName(), n));
		} else if (n instanceof ClassVarNode) {
			result.add(new EvaluatableItem(((ClassVarNode) n).getName(), n));
		} else if (n instanceof SelfNode) {
			result.add(new EvaluatableItem(((SelfNode) n).getName(), n));
		} else if (n instanceof GlobalVarNode) {
			result.add(new EvaluatableItem(((GlobalVarNode) n).getName(), n));
		} else if (n instanceof ConstNode) {
			result.add(new EvaluatableItem(((ConstNode) n).getName(), n));
		} else if (n instanceof Colon3Node) {
			if (n.childNodes().size() > 0) {
			result.add(new EvaluatableItem(r.getNodeText(n.childNodes().get(0)) + "::"
					+ ((Colon3Node) n).getName(), n));
			} else {
				result.add(new EvaluatableItem(((Colon3Node) n).getName(), n));
			}
		} else if (n instanceof DVarNode) {
			result.add(new EvaluatableItem(((DVarNode) n).getName(), n));
		}
		List<Node> childNodes = n.childNodes();
		for (Node child : childNodes) {
			ISourcePosition position = n.getPosition();
			if (position.getStartLine() <= lineno
					&& position.getEndLine() >= lineno) {
				result.addAll(collectEvaluatables(r, child, lineno));
			}
			if (position.getStartLine() > lineno)
				break;
		}
		return result;
	}

	static class ClientHandler implements Runnable {

		Socket socket;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				BufferedReader inputReader = new BufferedReader(
						new InputStreamReader(inputStream));
				BufferedWriter outputWrite = new BufferedWriter(
						new OutputStreamWriter(outputStream));
				while (true) {
					String line = inputReader.readLine();
					if (line == null)
						break;
					System.out.println(line);
					if (line.startsWith("bye")) {
						outputWrite.write("bye\n");
						socket.close();
						break;
					}
					if (line.startsWith("evaluatable_items ")) { // evaluatable_items
						// <file>
						// <line>
						StringTokenizer stringTokenizer = new StringTokenizer(
								line, " ");
						stringTokenizer.nextToken();
						String file = stringTokenizer.nextToken();
						String lineno = stringTokenizer.nextToken();
						try {
							ParsingResult r = parse(file);
							System.out.println(r.ast);
							List<EvaluatableItem> evaluatables = collectEvaluatables(
									r, r.ast, Integer.parseInt(lineno));
							for (EvaluatableItem i : evaluatables) {
								outputWrite.write(i.evalString + " " + i.node.getClass().getSimpleName() + "\n");
							}
							outputWrite.write("done\n");
							outputWrite.flush();
						} catch (IOException e) {
							e.printStackTrace();
							outputWrite.write("error\n");
							outputWrite.flush();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			System.out.println("-1 client connected");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(6789);
			System.out.println("running");
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("+1 client connected");
				new Thread(new ClientHandler(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
