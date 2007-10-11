package com.googlecode.perfs.fs;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public abstract class FileResource extends Resource {

	protected FileResource(FileSystem fileSystem, String uuid) {
		super(fileSystem, uuid);
	}

	protected FileResource(FileSystem fileSystem, UUID uuid) {
		super(fileSystem, uuid);
	}

	protected FileResource(FileSystem fileSystem) {
		super(fileSystem);
	}

	public abstract InputStream getInputStream();

	public abstract OutputStream getOutputStream();

	public abstract long size();

}