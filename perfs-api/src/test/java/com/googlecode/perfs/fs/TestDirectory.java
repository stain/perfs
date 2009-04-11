package com.googlecode.perfs.fs;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class TestDirectory extends AbstractFileSystemTest {

	@Test
	public void root() {
		assertTrue(root.isEmpty());
		assertNull(root.getParent());
		assertFalse("root uuid was not different from fs uuid", root.getUUID()
				.equals(fs.getUUID()));
	}

	@Test
	public void addingSubDir() {
		DirectoryResource subDir = fs.makeDirectory();
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
		DirectoryResource subDir =fs.makeDirectory();
		root.put("subdir", subDir);
		DirectoryResource subDir2 = fs.makeDirectory();
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
		DirectoryResource subDir = fs.makeDirectory();
		root.put("subdir", subDir);
		assertEquals(subDir, root.remove("subdir"));
		assertFalse(root.containsFilename("subdir"));
		assertNull(subDir.getParent());
		assertTrue(root.isEmpty());
		assertEquals(0, root.size());
	}

	@Test
	public void cleanSubdirs() {
		DirectoryResource subDir1 = fs.makeDirectory();
		root.put("subdir1", subDir1);
		DirectoryResource subDir2 = fs.makeDirectory();
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
		DirectoryResource subDir = fs.makeDirectory();
		root.put("subdir", subDir);
		DirectoryResource subSub = fs.makeDirectory();
		subDir.put("subdir", subSub); // Notice: Same name as parent
		assertEquals(subDir, root.get("subdir"));
		assertEquals(root, subDir.getParent());

		assertEquals(subDir, subSub.getParent());
		assertEquals(subSub, subDir.get("subdir"));
	}

	@Test
	public void threeSubdirs() {
		DirectoryResource subDir1 = fs.makeDirectory();
		root.put("fish", subDir1);
		DirectoryResource subDir2 = fs.makeDirectory();
		root.put("blah", subDir2);
		DirectoryResource subDir3 = fs.makeDirectory();
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
		DirectoryResource subDir1 = fs.makeDirectory();
		root.put("dir1", subDir1);
		DirectoryResource subDir2 = fs.makeDirectory();
		root.put("dir2", subDir2);

		DirectoryResource subsub = fs.makeDirectory();
		subDir1.put("subdir", subsub);

		// should fail because subsub already has parent
		subDir2.put("another", subsub);
	}

	@Test
	public void modifyingCopy() {
		DirectoryResource subDir = fs.makeDirectory();
		root.put("dir1", subDir);

		DirectoryResource copy = (DirectoryResource) root.get("dir1");

		DirectoryResource child = fs.makeDirectory();
		subDir.put("child", child);
		assertEquals(child, copy.get("child"));

		DirectoryResource copyChild = fs.makeDirectory();
		copy.put("copyChild", copyChild);
		assertEquals(copyChild, subDir.get("copyChild"));
	}

	@Test
	public void resourceFromFS() {
		assertEquals(root, fs.getResource(root.getUUID()));
		assertEquals(root, fs.getResource(root.getUUIDString()));

		DirectoryResource subDir = fs.makeDirectory();
		// Even before we added it to root
		assertEquals(subDir, fs.getResource(subDir.getUUID()));
		assertEquals(subDir, fs.getResource(subDir.getUUIDString()));
	}

}
