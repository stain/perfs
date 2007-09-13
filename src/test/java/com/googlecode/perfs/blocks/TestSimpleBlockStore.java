package com.googlecode.perfs.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.googlecode.perfs.blocks.SimpleBlockStore;

public class TestSimpleBlockStore extends TestBlockStore {
	
	@Override
	public SimpleBlockStore makeBlockStore() {
		return new SimpleBlockStore(dir);
	}
	
	@Test
	public void creationDir() {
		SimpleBlockStore blockStore = makeBlockStore();
		assertEquals(dir, blockStore.root);
	}
	
	/**
	 * Test that we get the same hash every time.
	 * @throws IOException 
	 * 
	 * @see storeAndRetrieve()
	 *
	 */
	@Test
	public void sameHash() throws IOException {
		SimpleBlockStore blockStore = makeBlockStore();
		byte[] value = {0x70, 0x46, 0x53};
		String hash = blockStore.put(value);
		// sha.sha("pFS").hexdigest()
		assertEquals("d047124663ad047d8fb29d564ef9e8d13d7cdca9", 
				hash);
		byte[] other = IOUtils.toByteArray(blockStore.get(hash));
		assertNotSame(value, other);
		assertTrue(Arrays.equals(value, other));
		assertEquals(hash, blockStore.hash(other));
		assertEquals(hash, blockStore.put(other));
	}
	
	
	/**
	 * Ensure TempDirTest gave us an empty directory
	 * 
	 */
	@Test
	public void hasTempDir() {
		assertTrue(dir.isDirectory());
		assertEquals(0, dir.listFiles().length);
	}
	
	/**
	 * Create meta file in existing directory
	 *
	 */
	@Test
	public void creation() {
		File meta = new File(dir, "perfs.meta");
		assertFalse("perfs.meta already existed in " + dir, meta.isFile());
		makeBlockStore();
		assertTrue("Did not create perfs.meta", 
				meta.isFile());
		// Should be only "perfs.meta"
		assertEquals(1, dir.list().length);
	}
		

	/**
	 * Create non-existing directory
	 *
	 */
	@Test
	public void nonExistingDir() {
		assertTrue("Could not delete root: " + dir, dir.delete());
		assertFalse("Deleted root still exists: " + dir, dir.exists());
		makeBlockStore();
		assertTrue("Did not make directory "+ dir, 
				dir.isDirectory());
		File meta = new File(dir, "perfs.meta");
		assertTrue("Did not create perfs.meta", meta.isFile());
	}
	
	/**
	 * Should throw IllegalArgumentException because the directory
	 * is a non-empty directory without the meta file.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void nonEmptyDir() throws IOException {
		File fish = new File(dir, "fish");
		FileUtils.writeStringToFile(fish, "Hello", "utf8");
		makeBlockStore(); // Should fail
	}
	
	/**
	 * Confirm content of meta file
	 * 
	 * @throws IOException
	 */
	@Test
	public void metaContent() throws IOException {
		makeBlockStore();
		File meta = new File(dir, "perfs.meta");
		String content = FileUtils.readFileToString(meta, "utf8");
		assertEquals("perfs blockstore v0.2\n", content);
	}
	
	/**
	 * Should fail because the meta file is in invalid format
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void wrongMeta() throws IOException {
		File meta = new File(dir, "perfs.meta");
		FileUtils.writeStringToFile(meta, 
				"Not a metafile\n", "utf8");
		makeBlockStore();
	}
	
}
