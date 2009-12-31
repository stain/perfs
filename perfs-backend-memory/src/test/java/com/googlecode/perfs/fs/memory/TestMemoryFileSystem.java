package com.googlecode.perfs.fs.memory;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.AbstractTestFilesystem;

public class TestMemoryFileSystem extends AbstractTestFilesystem {

	@Override
	protected FileSystem makeFileSystem() {
		return new MemoryFileSystem();
	}

}
