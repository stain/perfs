package com.googlecode.perfs.fs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public abstract class TestFilesystem extends AbstractFileSystemTest {
	
	FileSystem otherFS = makeFileSystem();
	
	@Test
	public void doubleRegisterOk() throws Exception {
		DirectoryResource dir = otherFS.makeDirectory();;		
		otherFS.registerResource(dir);
		assertEquals(dir, otherFS.getResource(dir.getUUID()));
		assertEquals(dir, otherFS.getResource(dir.getUUIDString()));
	}
	
	@Test
	public void registerAndRetrieve() throws Exception {
		DirectoryResource dir = fs.makeDirectory();
		assertEquals(dir, fs.getResource(dir.getUUID()));
		assertEquals(dir, fs.getResource(dir.getUUIDString()));
		assertNull(otherFS.getResource(dir.getUUID()));
		assertNull(otherFS.getResource(dir.getUUIDString()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void wrongFileSystem() throws Exception {
		DirectoryResource dir = otherFS.makeDirectory();
		fs.registerResource(dir);
	}

	
}
