package com.googlecode.perfs.fs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.googlecode.perfs.fs.memory.MemoryDirectoryResource;

public class TestFilesystem extends AbstractFileSystemTest {
	
	FileSystem otherFS = makeFileSystem();
	
	@Test
	public void registerAndRetrieve() throws Exception {
		MemoryDirectoryResource dir = new MemoryDirectoryResource(fs);
		assertEquals(dir, fs.getResource(dir.getUUID()));
		assertEquals(dir, fs.getResource(dir.getUUIDString()));
		assertNull(otherFS.getResource(dir.getUUID()));
		assertNull(otherFS.getResource(dir.getUUIDString()));
	}
	
	@Test
	public void doubleRegisterOk() throws Exception {
		MemoryDirectoryResource dir = new MemoryDirectoryResource(otherFS);		
		otherFS.registerResource(dir);
		assertEquals(dir, otherFS.getResource(dir.getUUID()));
		assertEquals(dir, otherFS.getResource(dir.getUUIDString()));
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void wrongFileSystem() throws Exception {
		MemoryDirectoryResource dir = new MemoryDirectoryResource(otherFS);
		fs.registerResource(dir);
	}
	
	
}
