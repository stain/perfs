package com.googlecode.perfs.frontend.vfs;

import java.util.Collection;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.log4j.Logger;

import com.googlecode.perfs.fs.FileSystem;

public class PerFSFileSystem extends AbstractFileSystem implements
		org.apache.commons.vfs.FileSystem {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PerFSFileSystem.class);

	private FileSystem backend;

	public PerFSFileSystem(com.googlecode.perfs.fs.FileSystem backend) {
		super(null, null, null);
		this.backend = backend;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addCapabilities(Collection capabilities) {
		capabilities.addAll(PerFSFileProvider.capabilities);
	}

	@Override
	protected FileObject createFile(FileName name) throws Exception {
		return new PerFSFileObject(name, this);
	}

	protected FileSystem getBackend() {
		return backend;
	}

}
