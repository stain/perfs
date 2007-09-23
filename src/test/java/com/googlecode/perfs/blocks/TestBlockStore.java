package com.googlecode.perfs.blocks;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.perfs.fs.AbstractTempDirTest;

public abstract class TestBlockStore extends AbstractTempDirTest {

	/**
	 * Generate BlockStore instance to be tested.
	 * 
	 * @return Newly initialized BlockStore instance
	 */
	public abstract BlockStore makeBlockStore();

	@Ignore
	@Test
	public void storeAndRetrieve() throws IOException {
		BlockStore blockStore = makeBlockStore();
		byte[] value = { 0x70, 0x46, 0x53 };
		String hash = blockStore.put(value);
		System.out.println(hash);
		InputStream otherStream = blockStore.get(hash);
		byte[] other = IOUtils.toByteArray(otherStream);
		assertNotSame(value, other);
		assertTrue("Retrieved value did not match", Arrays.equals(value, other));
	}

	@Ignore
	@Test
	public void storeAndRetrieveGivenID() throws IOException {
		BlockStore blockStore = makeBlockStore();
		String id = UUID.randomUUID().toString();
		byte[] value = { 0x42, 0x46, 0x53 };
		blockStore.put(id, value);
		InputStream otherStream = blockStore.get(id);
		byte[] other = IOUtils.toByteArray(otherStream);
		assertNotSame(value, other);
		assertTrue(Arrays.equals(value, other));
		// Should (preferably) NOT be stored by hash as well
		if (blockStore instanceof SimpleBlockStore) {
			assertNull(blockStore.get(((SimpleBlockStore) blockStore).hash(value)));
		}
	}

	@Test
	public void containsAndRemove() {
		BlockStore blockStore = makeBlockStore();
		byte[] value = { 0x70, 0x46, 0x53 };
		String key = "fish";
		assertFalse(blockStore.containsKey(key));
		assertNull(blockStore.get(key));

		blockStore.put(key, value);
		assertTrue(blockStore.containsKey(key));

		blockStore.remove(key);
		assertFalse(blockStore.containsKey(key));
		assertNull(blockStore.get(key));
	}

	@Test
	public void hashTwice() throws InterruptedException {
		BlockStore blockStore = makeBlockStore();
		byte[] value = { 0x70, 0x46, 0x53 };
		String key = blockStore.put(value);
		if (blockStore instanceof SimpleBlockStore) {
			File file = ((SimpleBlockStore) blockStore).fileFor(key);
			// Backdate lastModified so that we can detect changes 
			// even after milliseconds
			long lastMod = file.lastModified() - 2000;
			file.setLastModified(lastMod);
			assertTrue(lastMod == file.lastModified());

			// Should be same hash second time
			assertEquals(key, blockStore.put(value));
			// But not re-written to disk
			assertTrue(lastMod == file.lastModified());

			// Except if we explicitly give the key
			blockStore.put(key, value);
			assertTrue("Did not", lastMod != file.lastModified());
		}
	}

	@Ignore
	@Test
	public void otherInstance() throws IOException {
		// We'll store something here
		BlockStore blockStore = makeBlockStore();
		byte[] value = { 0x70, 0x46, 0x53 };
		String key = blockStore.put(value);
		// .. and then construct a new blockstore
		BlockStore otherStore = makeBlockStore();
		InputStream otherStream = blockStore.get(key);
		byte[] otherValue = IOUtils.toByteArray(otherStream);
		assertNotSame(value, otherValue);
		assertTrue(Arrays.equals(value, otherValue));

		// And try storing something in otherStore -> blockStore
		byte[] newValue = { 0x42, 0x46, 0x53 };
		key = otherStore.put(newValue);
		InputStream stream = blockStore.get(key);
		value = IOUtils.toByteArray(stream);
		assertNotSame(newValue, value);
		assertTrue(Arrays.equals(newValue, value));
	}

	/*
	 * Python store.py 
	 
	 
	 class LargeStoreMixin(CreateStoreMixin):
	 def setUp(self):    
	 super(LargeStoreMixin, self).setUp()
	 self.block_store = self.store
	 self.store = store.LargeStore(self.block_store)


	 class TestLargeStoreSmallData(LargeStoreMixin, TestStoreMixin,
	 unittest.TestCase):
	 pass

	 class TestLargeStoreLargeData(TestLargeStoreSmallData):
	 DATA = [data * 130000 for data in TestLargeStoreSmallData.DATA]

	 class TestLargeStoreVeryLargeData(TestLargeStoreLargeData):
	 def setUp(self):
	 super(TestLargeStoreVeryLargeData, self).setUp()
	 self.store = store.LargeStore(self.block_store, blocksize=1024)

	 

	 if __name__ == "__main__":
	 unittest.main()

	 * 
	 */
}
