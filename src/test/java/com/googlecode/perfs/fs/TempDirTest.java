/**
 * 
 */
package com.googlecode.perfs.fs;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;

public class TempDirTest {

	public File dir;
	
	@Before
	public void makeDir() throws IOException {
		dir = File.createTempFile(this.getClass().getName(), "test");
		assertTrue(dir.delete());
		assertTrue(dir.mkdir());
	}
	
	@After
	public void deleteDir() throws IOException {
		//FileUtils.deleteDirectory(dir);
		System.out.println(dir);
	}
}