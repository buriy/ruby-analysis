package com.yoursway.sadr.newruby.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.yoursway.fsmonitor.FileSystemChangesListener;
import com.yoursway.fsmonitor.FileSystemMonitor;
import com.yoursway.fsmonitor.FileSystemMonitoringContext;
import com.yoursway.utils.YsFileUtils;
import com.yoursway.utils.YsPathUtils;

public class Project {
	private static final String SETTINGS = "BUILDPATHS";
	private final String root;
	private List<String> paths = new ArrayList<String>();
	private Set<String> projectFiles = new HashSet<String>();

	Completer completer = new RubyCompleter();

	private FilenameFilter rubyFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			if (name.startsWith(".") || YsFileUtils.isBogusFile(name))
				return false;
			if (new File(dir, name).isDirectory()) {
				return true;
			} else {
				return name.endsWith(".rb");
			}
		}
	};
	private FileSystemMonitoringContext context;
	private FileSystemMonitor settings_monitor;
	private Map<String, FileSystemMonitor> monitors = new HashMap<String, FileSystemMonitor>();

	public void run() {
		context.run();
	}

	public Project(String root) throws IOException {
		this.root = expandPath(root);
		this.context = new FileSystemMonitoringContext();
		this.settings_monitor = createSettingsMonitor(root);

		paths = getSettings();
		addBuildPaths();
	}

	private FileSystemMonitor createSettingsMonitor(String root) {
		return new FileSystemMonitor(context, new File(root), new FileSystemChangesListener() {
			public void changed(String path) {
				String settings = Project.this.root + SETTINGS;
				if (path.equals(settings)) {
					reloadSettings();
				}
			}

			public void inoperational() {
			}

			public void operational() {
			}
		});
	}

	public void dispose() {
		settings_monitor.dispose();
		for (Entry<String, FileSystemMonitor> monitor : monitors.entrySet()) {
			monitor.getValue().dispose();
		}
	}

	private void addBuildPaths() {
		for (String path : paths) {
			addBuildPath(path);
		}
	}

	private void addBuildPath(String path) {
		loadFiles(path);
		followBuildPath(path);
	}

	private void removeBuildPath(String path) {
		unloadFiles(path);
		unfollowPath(path);
	}

	private void loadFiles(String path) {
		List<String> files = YsPathUtils.findFiles(path, rubyFilter);
		for (String file : files) {
			loadFile(file);
		}
	}

	private synchronized void loadFile(String file) {
		System.err.println("Loading file: " + file);
		completer.loadFile(file);
		projectFiles.add(file);

	}

	private synchronized void unloadFile(String file) {
		System.err.println("Unloading file: " + file);
		completer.unloadFile(file);
		projectFiles.remove(file);
	}

	private void unloadFiles(String path) {
		ArrayList<String> toRemove = filterByPrefix(projectFiles, expandPath(path));
		for (String file : toRemove) {
			unloadFile(file);
		}
	}

	private void followBuildPath(String path) {
		System.err.println("Following path: " + path);
		FileSystemMonitor monitor = new FileSystemMonitor(context, new File(path),
				new FileSystemChangesListener() {
					public void changed(String path) {
						unloadFiles(path);
						loadFiles(path);
					}

					public void inoperational() {
					}

					public void operational() {
					}
				});

		monitors.put(path, monitor);
	}

	private void unfollowPath(String path) {
		System.err.println("Unfollowing path: " + path);
		FileSystemMonitor monitor = monitors.remove(path);
		monitor.dispose();
	}

	private List<String> getSettings() throws FileNotFoundException, IOException {
		paths = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(this.root + SETTINGS));
		String line;
		while ((line = br.readLine()) != null) {
			String path = expandRelativePath(line);
			if (!paths.contains(path)) {
				paths.add(path);
			}
		}
		return paths;
	}

	public boolean reloadSettings() {
		System.err.println("Reloading settings");
		List<String> oldPaths = paths;
		try {
			paths = getSettings();
		} catch (IOException e) {
			e.printStackTrace();
			return false; // reload failed
		}
		for (String path : paths) {
			if (oldPaths.contains(path)) {
				oldPaths.remove(path);
			} else {
				addBuildPath(path);
			}
		}
		for (String path : oldPaths) {
			removeBuildPath(path);
		}
		return true;
	}

	public synchronized void complete(CompletionCommand command, CompletionProposalAcceptor acceptor) {
		String file = command.get(CompletionCommand.OPT_FILE);
		int line = Integer.parseInt(command.get(CompletionCommand.OPT_LINE));
		int column = Integer.parseInt(command.get(CompletionCommand.OPT_COLUMN));
		completer.complete(file, line, column, acceptor);
	}

	private static ArrayList<String> filterByPrefix(Collection<String> files, String prefix) {
		ArrayList<String> output = new ArrayList<String>();
		for (String file : files) {
			if (file.startsWith(prefix))
				output.add(file);
		}
		return output;
	}

	private String expandRelativePath(String path) {
		if (!path.startsWith("/")) {
			path = root + path;
		}
		return expandPath(path);
	}

	private static String expandPath(String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			return file.getAbsolutePath() + "/";
		} else {
			return file.getAbsolutePath();
		}
	}

}
