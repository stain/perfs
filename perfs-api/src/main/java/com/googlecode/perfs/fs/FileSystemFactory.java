package com.googlecode.perfs.fs;

public class FileSystemFactory {

	private static FileSystem fileSystem = null;

	public static FileSystem getFileSystem() {
		if (fileSystem == null) {
			throw new IllegalStateException("File system has not been set");
		}
		return fileSystem;
	}
	
	public static void setFileSystem(FileSystem fileSystem) {
		FileSystemFactory.fileSystem = fileSystem;
	}

}
