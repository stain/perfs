package com.googlecode.perfs.fs.memory;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.TestFile;

public class TestMemoryFile extends TestFile {

	@Override
	protected FileSystem makeFileSystem() {
		return new MemoryFileSystem();
	}

}
