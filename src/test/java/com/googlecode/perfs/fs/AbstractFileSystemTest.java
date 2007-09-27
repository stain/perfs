package com.googlecode.perfs.fs;

import org.junit.Before;

import com.googlecode.perfs.blocks.BlockStore;
import com.googlecode.perfs.blocks.LargeBlockStore;
import com.googlecode.perfs.blocks.SimpleBlockStore;
import com.googlecode.perfs.fs.memory.MemoryFileSystem;

public abstract class AbstractFileSystemTest extends AbstractTempDirTest {

	protected FileSystem fs;
	protected LargeBlockStore blockStore;
	protected DirectoryResource root;

	@Before
	public void makeFilesystem() {
		BlockStore backendStore = new SimpleBlockStore(dir);
		blockStore = new LargeBlockStore(backendStore);
		fs = makeFileSystem();
		root = fs.getRoot();
	}

	protected MemoryFileSystem makeFileSystem() {
		return new MemoryFileSystem();
	}

}