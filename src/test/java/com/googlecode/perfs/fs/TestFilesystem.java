package com.googlecode.perfs.fs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.perfs.blocks.BlockStore;
import com.googlecode.perfs.blocks.LargeBlockStore;
import com.googlecode.perfs.blocks.SimpleBlockStore;
import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.rdf.Filesystem;


public class TestFilesystem extends TempDirTest {
	
	Filesystem fs;
	
	@Before
	public void makeFilesystem() {
		BlockStore backendStore = new SimpleBlockStore(dir);
		BlockStore blockStore = new LargeBlockStore(backendStore);
		fs = new Filesystem(blockStore);
	}
	
	@Test
	public void root() {
		DirectoryResource root = fs.getRoot();
		assertTrue(root.isEmpty());
		assertNull(root.getParent());
	}
	
	@Test
	public void addingSubDir() {
		DirectoryResource root = fs.getRoot();
		DirectoryResource subDir = new DirectoryResource(fs);
		assertTrue(root.isEmpty());
		assertTrue(subDir.isEmpty());
		assertFalse(root.containsKey("subdir"));
		assertNull(subDir.getParent());
		assertNull(subDir.get("subdir"));
		
		root.put("subdir", subDir);
		assertFalse(root.isEmpty());
		assertTrue(subDir.isEmpty()); // .. does not count
		assertEquals(root, subDir.getParent());
		assertTrue(root.containsKey("subdir"));
		assertEquals(root.get("subdir"), subDir);
		
	}
	
	@Test
	public void replacingSubdir() {
		DirectoryResource root = fs.getRoot();
		DirectoryResource subDir = new DirectoryResource(fs);
		assertNull("Old 'subdir' already existed", root.put("subdir", subDir));
		DirectoryResource subDir2 = new DirectoryResource(fs);
		assertEquals("Did not return old 'subdir'", subDir, root.put("subdir", subDir2));
		assertNull("Did not reset parent on old resource", subDir.getParent());
		assertEquals(root, subDir2.getParent());
		assertEquals(subDir2, root.get("subdir"));
	}
	

	@Test
	public void subSubDirectory() {
		DirectoryResource root = fs.getRoot();
		DirectoryResource subDir = new DirectoryResource(fs);
		root.put("subdir", subDir);
		DirectoryResource subSub = new DirectoryResource(fs);
		subDir.put("subdir", subSub); // Notice: Same name as parent
		assertEquals(subDir, root.get("subdir"));
		assertEquals(root, subDir.getParent());
		
		assertEquals(subDir, subSub.getParent());
		assertEquals(subSub, subDir.get("subdir"));
	}
	
	@Test(expected=IllegalStateException.class)
	public void subdirWithTwoParentsFails() {
		DirectoryResource root = fs.getRoot();
		DirectoryResource subDir1 = new DirectoryResource(fs);
		root.put("dir1", subDir1);
		DirectoryResource subDir2 = new DirectoryResource(fs);
		root.put("dir2", subDir2);
		
		DirectoryResource subsub = new DirectoryResource(fs);
		subDir1.put("subdir", subsub);
		
		 // should fail because subsub already has parent
		subDir2.put("another", subsub);
	}
	
	
	
}
