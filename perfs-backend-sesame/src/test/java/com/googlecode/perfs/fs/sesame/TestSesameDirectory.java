package com.googlecode.perfs.fs.sesame;


import org.junit.Test;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.TestDirectory;

public class TestSesameDirectory extends TestDirectory {

	@Override
	protected FileSystem makeFileSystem() {
		return new SesameFileSystem();
	}

}
