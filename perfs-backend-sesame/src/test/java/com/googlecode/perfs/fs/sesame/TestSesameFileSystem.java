package com.googlecode.perfs.fs.sesame;


import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.TestFilesystem;

public class TestSesameFileSystem extends TestFilesystem {

	@Override
	protected FileSystem makeFileSystem() {
		return new SesameFileSystem();
	}

}
