package com.googlecode.perfs.fs;

import org.junit.Before;

public abstract class AbstractFileSystemTest extends AbstractTempDirTest {

	protected FileSystem fs;
	protected DirectoryResource root;

	@Before
	public void makeFilesystem() {
		fs = makeFileSystem();
		root = fs.getRoot();
	}

	protected abstract FileSystem makeFileSystem();

}