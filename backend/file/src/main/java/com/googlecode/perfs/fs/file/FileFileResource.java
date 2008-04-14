package com.googlecode.perfs.fs.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.googlecode.perfs.fs.FileResource;

public class FileFileResource extends FileResource {

	
	protected FileFileResource(FileFileSystem fileSystem, String uuid) {
		super(fileSystem, uuid);
	}

	protected FileFileResource(FileFileSystem fileSystem, UUID uuid) {
		super(fileSystem, uuid);
	}

	protected FileFileResource(FileFileSystem fileSystem) {
		super(fileSystem);
	}
	
	@Override
	public FileFileSystem getFileSystem() {
		return (FileFileSystem) super.getFileSystem();
	}

	@Override
	public InputStream getInputStream() {
		File blockFile = getBlockFile();
		try {
			return new FileInputStream(blockFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public OutputStream getOutputStream() {
		File blockFile = getBlockFile();
		try {
			return new FileOutputStream(blockFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	protected File getBlockFile() {
		return getFileSystem().fileByUUID(getUUID());
	}

	@Override
	public long size() {
		return getBlockFile().length();
	}


}
