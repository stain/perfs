package com.googlecode.perfs.fs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.googlecode.perfs.fs.DirectoryResource;


public class TestDirectory extends AbstractFileSystemTest {
	
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
		assertEquals(1, root.size());
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
	
	@Test
	public void threeSubdirs() {
		DirectoryResource root = fs.getRoot();
		DirectoryResource subDir1 = new DirectoryResource(fs);
		root.put("fish", subDir1);
		DirectoryResource subDir2 = new DirectoryResource(fs);
		root.put("blah", subDir2);
		DirectoryResource subDir3 = new DirectoryResource(fs);
		root.put("soup", subDir3);
		assertEquals(3, root.size());
		
		assertTrue("subDir1 should equal itself", subDir1.equals(subDir1));
		assertFalse("subdir1 should not equal subdir2", subDir1.equals(subDir2));
		assertFalse("subdir2 should not equal subdir3", subDir2.equals(subDir3));
		
		assertEquals(subDir1, root.get("fish"));
		assertEquals(subDir3, root.get("soup"));
		assertEquals(subDir2, root.get("blah"));

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
	
	@Test
	public void modifyingCopy() {
		DirectoryResource root = fs.getRoot();
		DirectoryResource subDir = new DirectoryResource(fs);
		root.put("dir1", subDir);
		
		DirectoryResource copy = (DirectoryResource) root.get("dir1");
		
		DirectoryResource child = new DirectoryResource(fs);
		subDir.put("child", child);		
		assertEquals(child, copy.get("child"));
		
		
		DirectoryResource copyChild = new DirectoryResource(fs);
		copy.put("copyChild", copyChild);		
		assertEquals(copyChild, subDir.get("copyChild"));
	}
	
	@Test
	public void resourceFromFS() {
		DirectoryResource root = fs.getRoot();
		assertEquals(root, fs.getResource(root.getUUID()));
		assertEquals(root, fs.getResource(fs.getUUIDString()));
	}
	
}
