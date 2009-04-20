package com.googlecode.perfs.fs.sesame;


import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.TestFile;

public class TestSesameFile extends TestFile {

	@Override
	protected FileSystem makeFileSystem() {
		return new SesameFileSystem();
	}

}
