package com.googlecode.perfs.fs;

import org.junit.Before;

import com.googlecode.perfs.blocks.BlockStore;
import com.googlecode.perfs.blocks.LargeBlockStore;
import com.googlecode.perfs.blocks.SimpleBlockStore;

public abstract class AbstractFileSystemTest extends AbstractTempDirTest {

	protected FileSystem fs;
	protected LargeBlockStore blockStore;

	public AbstractFileSystemTest() {
		super();
	}

	@Before
	public void makeFilesystem() {
		BlockStore backendStore = new SimpleBlockStore(dir);
		blockStore = new LargeBlockStore(backendStore);
		fs = new FileSystem(blockStore);
	}

}