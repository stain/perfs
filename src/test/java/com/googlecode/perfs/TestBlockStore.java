package com.googlecode.perfs;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.Test;



public class TestBlockStore extends TempDirTest {
	
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
		BlockStore blockStore = new BlockStore(dir);
		assertTrue("Did not create perfs.meta", 
				meta.isFile());
		assertEquals(dir, blockStore.root);
	}
	
	/**
	 * Create non-existing directory
	 *
	 */
	@Test
	public void nonExistingDir() {
		assertTrue("Could not delete root: " + dir, dir.delete());
		assertFalse("Deleted root still exists: " + dir, dir.exists());
		new BlockStore(dir);
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
		new BlockStore(dir); // Should fail
	}
	
	/**
	 * Confirm content of meta file
	 * 
	 * @throws IOException
	 */
	@Test
	public void metaContent() throws IOException {
		new BlockStore(dir);
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
		new BlockStore(dir);
	}
	
	public void storeAndRetrieve() {
		String id = UUID.randomUUID().toString();
		BlockStore blockStore = new BlockStore(dir);
		
	}

	/*
    

class TestStoreMixin(object):
    """Test external API of storing.
    
    This is an abstract class to be implemented by test classes for
    different backends.
    """
    DATA = [
        "Fish",
        "Other",
        "Storage",
        "1337",
        "fish",
        "soup",
    ]
    
    def testStoreRetrieveString(self):
        id = self.store.store(self.DATA[0])
        self.assertEqual(self.store.retrieve(id), 
               self.DATA[0])
    
    def testStoreOtherID(self):
        id = self.store.store(self.DATA[1], "1337")
        self.assertEqual(id, "1337")
        self.assertEqual(self.store.retrieve("1337"), 
                         self.DATA[1])
        # Should fail
        self.assertRaises(store.NotFoundError, 
                self.store.retrieve, 
                 sha.sha(self.DATA[1]).hexdigest())
        
    def testExists(self):
        id = self.store.store(self.DATA[2])
        self.assert_(self.store.exists(id))    
        self.assert_(not self.store.exists("1337"))
      

    def testRemove(self):
        id = self.store.store(self.DATA[3])
        self.store.retrieve(id)
        self.store.remove(id)
        self.assertRaises(store.NotFoundError, 
                self.store.retrieve, id)
        self.assertRaises(store.NotFoundError, 
                self.store.remove, id)
  
    def testStoreTwiceWithSameID(self):    
        id = "1234-56-789"
        fish = self.store.store(self.DATA[4], id)
        # Tries to store a different value with same id
        self.assertRaises(store.AlreadyExistsError,
                    self.store.store, self.DATA[5], id)
        # Behaviour of self.store.store("fish", id) is undefined,
        # it could or could not raise an error

    
class CreateStoreMixin(CreateDirMixin):
    def setUp(self):
        super(CreateStoreMixin, self).setUp()
        self.store = store.BlockStore(self.dir)
        meta = os.path.join(self.dir, "perfs.meta")
        self.failUnless(os.path.isfile(meta))

class TestBlockStoreBlack(CreateStoreMixin, TestStoreMixin, 
                          unittest.TestCase):
    def testGeneratedIdIsSha1(self):
        id = self.store.store("HashMe")
        self.assertEqual(id, sha.sha("HashMe").hexdigest())
    
    def testStoreTwice(self):
        id = self.store.store("First time")      
        # Store the same again, calculated hash
        other = self.store.store("First time")
        # Should work, and return the same hash. The backend is free to
        # actually store it again or not
        self.assertEqual(id, other)
    
    def testStoreOtherInstance(self):
        # Should be able to retrieve both files stored
        # before and after creating a new BlockStore instance
        before_id = self.store.store("before")    
        other = store.BlockStore(self.dir)
        after_id = self.store.store("after")    
        self.assertEqual(other.retrieve(before_id),
                         "before")
        self.assertEqual(other.retrieve(after_id),
                         "after")
        # And if stored from the other, should be visible
        # even if other is deleted
        other_id = other.store("other")
        del other
        time.sleep(0.5)
        self.assertEqual(self.store.retrieve(other_id),
                         "other")
    

class TestBlockStoreWhite(CreateStoreMixin, unittest.TestCase):
    def testEmpty(self):
        self.assertEqual(["perfs.meta"],
                         os.listdir(self.dir))
    def testStoreSimple(self):
        id = self.store.store("Fish\n")
        root = id[:2]
        self.assertEqual(set((root, "perfs.meta")),
                         set(os.listdir(self.dir)))
        self.assertEqual([id], 
             os.listdir(os.path.join(self.dir, root)))

        path = os.path.join(self.dir, root, id)
        # Should be the exact content, and not \r\n 
        self.assertEqual(open(path, "rb").read(), 
                         "Fish\n")

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
