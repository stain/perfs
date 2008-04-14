package com.googlecode.perfs.fs.rdf.openanzo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.googlecode.perfs.fs.FileResource;

public class AnzoFileResource extends FileResource {

	public AnzoFileResource(AnzoFileSystem fileSystem, String uuid) {
		super(fileSystem, uuid);
		// TODO Auto-generated constructor stub
	}

	public AnzoFileResource(AnzoFileSystem fileSystem, UUID uuid) {
		super(fileSystem, uuid);
		// TODO Auto-generated constructor stub
	}

	public AnzoFileResource(AnzoFileSystem fileSystem) {
		super(fileSystem);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
