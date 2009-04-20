package com.googlecode.perfs.fs.memory;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.TestDirectory;

public class TestMemoryDirectory extends TestDirectory {

	@Override
	protected FileSystem makeFileSystem() {
		return new MemoryFileSystem();
	}

}
