package com.googlecode.perfs;

import static org.junit.Assert.*;

import org.junit.Test;

public class FishTest extends TempDirTest {
	
	
	@Test
	public void hasTempDir() {
		assertTrue(dir.isDirectory());
		assertEquals(0, dir.listFiles().length);
	}
	

}
