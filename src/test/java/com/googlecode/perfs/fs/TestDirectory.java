package com.googlecode.perfs.fs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.googlecode.perfs.fs.memory.MemoryDirectoryResource;

public class TestDirectory extends AbstractFileSystemTest {

	@Test
	public void root() {
		assertTrue(root.isEmpty());
		assertNull(root.getParent());
		assertFalse("root uuid was not different from fs uuid", root.getUUID()
				.equals(fs.getUUID()));
	}

	@Test
	public void addingSubDir() {
		MemoryDirectoryResource subDir = new MemoryDirectoryResource(fs);
		assertTrue(root.isEmpty());
		assertEquals(0, root.size());
		assertTrue(subDir.isEmpty());
		assertEquals(0, subDir.size());
		assertFalse(root.containsFilename("subdir"));
		assertFalse(root.containsResource(subDir));
		assertNull(subDir.getParent());
		assertNull(subDir.get("subdir"));

		root.put("subdir", subDir);
		assertFalse(root.isEmpty());
		assertEquals(1, root.size());
		assertTrue(subDir.isEmpty()); // .. does not count
		assertEquals(0, subDir.size());
		assertEquals(root, subDir.getParent());
		assertTrue(root.containsFilename("subdir"));
		assertTrue(root.containsResource(subDir));
		assertEquals(subDir, root.get("subdir"));
	}

	@Test(expected = AlreadyExistsException.class)
	public void replacingSubdir() {
		MemoryDirectoryResource subDir = new MemoryDirectoryResource(fs);
		root.put("subdir", subDir);
		MemoryDirectoryResource subDir2 = new MemoryDirectoryResource(fs);
		try {
			root.put("subdir", subDir2);
		} finally {
			assertNull("Changed the new resource", subDir2.getParent());
			assertEquals(root, subDir.getParent());
			assertEquals(subDir, root.get("subdir"));
		}
	}

	@Test
	public void deleteSubdir() {
		MemoryDirectoryResource subDir = new MemoryDirectoryResource(fs);
		root.put("subdir", subDir);
		assertEquals(subDir, root.remove("subdir"));
		assertFalse(root.containsFilename("subdir"));
		assertNull(subDir.getParent());
		assertTrue(root.isEmpty());
		assertEquals(0, root.size());
	}

	@Test
	public void cleanSubdirs() {
		MemoryDirectoryResource subDir1 = new MemoryDirectoryResource(fs);
		root.put("subdir1", subDir1);
		MemoryDirectoryResource subDir2 = new MemoryDirectoryResource(fs);
		root.put("subdir2", subDir2);
		assertEquals(2, root.size());
		root.clear();
		assertEquals(0, root.size());
		assertTrue(root.isEmpty());

		assertFalse(root.containsFilename("subdir1"));
		assertFalse(root.containsFilename("subdir2"));

		assertNull(subDir1.getParent());
		assertNull(subDir2.getParent());
	}

	@Test
	public void subSubDirectory() {
		MemoryDirectoryResource subDir = new MemoryDirectoryResource(fs);
		root.put("subdir", subDir);
		MemoryDirectoryResource subSub = new MemoryDirectoryResource(fs);
		subDir.put("subdir", subSub); // Notice: Same name as parent
		assertEquals(subDir, root.get("subdir"));
		assertEquals(root, subDir.getParent());

		assertEquals(subDir, subSub.getParent());
		assertEquals(subSub, subDir.get("subdir"));
	}

	@Test
	public void threeSubdirs() {
		MemoryDirectoryResource subDir1 = new MemoryDirectoryResource(fs);
		root.put("fish", subDir1);
		MemoryDirectoryResource subDir2 = new MemoryDirectoryResource(fs);
		root.put("blah", subDir2);
		MemoryDirectoryResource subDir3 = new MemoryDirectoryResource(fs);
		root.put("soup", subDir3);
		assertEquals(3, root.size());

		assertTrue("subDir1 should equal itself", subDir1.equals(subDir1));
		assertFalse("subdir1 should not equal subdir2", subDir1.equals(subDir2));
		assertFalse("subdir2 should not equal subdir3", subDir2.equals(subDir3));

		assertEquals(subDir1, root.get("fish"));
		assertEquals(subDir3, root.get("soup"));
		assertEquals(subDir2, root.get("blah"));

	}

	@Test(expected = IllegalStateException.class)
	public void subdirWithTwoParentsFails() {
		MemoryDirectoryResource subDir1 = new MemoryDirectoryResource(fs);
		root.put("dir1", subDir1);
		MemoryDirectoryResource subDir2 = new MemoryDirectoryResource(fs);
		root.put("dir2", subDir2);

		MemoryDirectoryResource subsub = new MemoryDirectoryResource(fs);
		subDir1.put("subdir", subsub);

		// should fail because subsub already has parent
		subDir2.put("another", subsub);
	}

	@Test
	public void modifyingCopy() {
		MemoryDirectoryResource subDir = new MemoryDirectoryResource(fs);
		root.put("dir1", subDir);

		DirectoryResource copy = (DirectoryResource) root.get("dir1");

		MemoryDirectoryResource child = new MemoryDirectoryResource(fs);
		subDir.put("child", child);
		assertEquals(child, copy.get("child"));

		MemoryDirectoryResource copyChild = new MemoryDirectoryResource(fs);
		copy.put("copyChild", copyChild);
		assertEquals(copyChild, subDir.get("copyChild"));
	}

	@Test
	public void resourceFromFS() {
		assertEquals(root, fs.getResource(root.getUUID()));
		assertEquals(root, fs.getResource(root.getUUIDString()));

		MemoryDirectoryResource subDir = new MemoryDirectoryResource(fs);
		// Even before we added it to root
		assertEquals(subDir, fs.getResource(subDir.getUUID()));
		assertEquals(subDir, fs.getResource(subDir.getUUIDString()));
	}

}
