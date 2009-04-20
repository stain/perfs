package com.googlecode.perfs.fs.memory;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.TestFilesystem;

public class TestMemoryFileSystem extends TestFilesystem {

	@Override
	protected FileSystem makeFileSystem() {
		return new MemoryFileSystem();
	}

}
