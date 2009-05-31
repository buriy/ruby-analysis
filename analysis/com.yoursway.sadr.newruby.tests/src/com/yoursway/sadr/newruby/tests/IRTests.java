package com.yoursway.sadr.newruby.tests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.yoursway.sadr.newruby.core.RubyAnalyzer;
import com.yoursway.sadr.newruby.core.ir.RubyFile;

public class IRTests {

	@Test
	public void justRuns() throws IOException {
		File file = new File("/Users/fourdman/Projects/ruby-analysis/analysis/com.yoursway.sadr.newruby.tests/tests/test.rb");
		RubyAnalyzer analyzer = new RubyAnalyzer();
		RubyFile rubyFile = analyzer.addFile(file);
		if (rubyFile == null)
			throw new NullPointerException();
		System.out.println(rubyFile.toString());
	}
	
}
