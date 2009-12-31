package com.googlecode.perfs.fs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public abstract class AbstractTestFile extends AbstractFileSystemTest {

	private static final String ASCII = "ascii";
	private static final String HELLO_THERE = "Hello there\n";

	@Test
	public void createFile() {
		FileResource file = fs.makeFile();
		assertEquals((long) 0, file.size());
		root.put("file.txt", file);
		assertEquals(file, root.get("file.txt"));
	}

	@Test
	public void readEmptyFile() throws IOException {
		root.put("file.txt", fs.makeFile());
		FileResource file = (FileResource) root.get("file.txt");
		InputStream is = file.getInputStream();
		assertEquals(-1, is.read());
	}

	@Test
	public void writeToEmptyFile() throws Exception {
		root.put("file.txt", fs.makeFile());
		FileResource file = (FileResource) root.get("file.txt");
		OutputStream os = file.getOutputStream();
		IOUtils.write(HELLO_THERE, os, ASCII);
		os.close();
		assertEquals(HELLO_THERE, IOUtils.toString(file.getInputStream(), ASCII));
	}

	@Test
	public void writeLotsToFile() throws Exception {
		root.put("bigFile.txt", fs.makeFile());
		FileResource file = (FileResource) root.get("bigFile.txt");
		OutputStream os = file.getOutputStream();
		
		
		int maxSize = 1000;
		StringBuffer megaString = new StringBuffer();
		for (int i=0 ; i<maxSize  ; i++) {
			megaString.append(HELLO_THERE);
			IOUtils.write(HELLO_THERE, os, ASCII);
		}
		os.close();
		System.out.println("Wrote " + megaString.length());
		for (int i=0 ; i<maxSize  ; i++) {
			assertEquals(megaString.toString(), IOUtils.toString(file.getInputStream(), ASCII));
		}
	}

	
}
