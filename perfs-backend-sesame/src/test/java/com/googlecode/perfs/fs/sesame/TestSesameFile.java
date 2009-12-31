package com.googlecode.perfs.fs.sesame;


import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.AbstractTestFile;

public class TestSesameFile extends AbstractTestFile {

	@Override
	protected FileSystem makeFileSystem() {
		return new SesameFileSystem();
	}

}
