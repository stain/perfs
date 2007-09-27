package com.googlecode.perfs.fs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.googlecode.perfs.fs.memory.MemoryFileResource;

public class TestFile extends AbstractFileSystemTest {

	@Test
	public void createFile() {
		MemoryFileResource file = new MemoryFileResource(fs);
		assertEquals((long)0, file.size());
		root.put("file.txt", file);
		assertEquals(file, root.get("file.txt"));
	}
	
	@Test
	public void readEmptyFile() throws IOException {
		root.put("file.txt", new MemoryFileResource(fs));
		FileResource file = (FileResource) root.get("file.txt");
		InputStream is = file.getInputStream();
		assertEquals(-1, is.read());
	}
	
	@Test
	public void writeToEmptyFile() throws Exception {
		root.put("file.txt", new MemoryFileResource(fs));
		FileResource file = (FileResource) root.get("file.txt");
		OutputStream os = file.getOutputStream();
		IOUtils.write("Hello there\n", os);
		os.close();
		assertEquals("Hello there\n", 
				IOUtils.toString(file.getInputStream()));
	}
	
}
