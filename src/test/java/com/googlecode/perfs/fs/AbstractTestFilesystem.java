package com.googlecode.perfs.fs;

import org.junit.Before;

import com.googlecode.perfs.blocks.BlockStore;
import com.googlecode.perfs.blocks.LargeBlockStore;
import com.googlecode.perfs.blocks.SimpleBlockStore;

public abstract class AbstractTestFilesystem extends AbstractTempDirTest {

	protected Filesystem fs;

	public AbstractTestFilesystem() {
		super();
	}

	@Before
	public void makeFilesystem() {
		BlockStore backendStore = new SimpleBlockStore(dir);
		BlockStore blockStore = new LargeBlockStore(backendStore);
		fs = new Filesystem(blockStore);
	}

}