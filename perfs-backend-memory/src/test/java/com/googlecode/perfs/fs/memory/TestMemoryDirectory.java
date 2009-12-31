package com.googlecode.perfs.fs.memory;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.AbstractTestDirectory;

public class TestMemoryDirectory extends AbstractTestDirectory {

	@Override
	protected FileSystem makeFileSystem() {
		return new MemoryFileSystem();
	}

}
