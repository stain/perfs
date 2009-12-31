package com.googlecode.perfs.fs.memory;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.AbstractTestFile;

public class TestMemoryFile extends AbstractTestFile {

	@Override
	protected FileSystem makeFileSystem() {
		return new MemoryFileSystem();
	}

}
