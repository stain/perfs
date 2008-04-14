package com.googlecode.perfs.fs.rdf.openanzo;

import java.util.UUID;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.Resource;

public class AnzoFileSystem extends FileSystem {

	public AnzoFileSystem() {
		super();
	}

	public AnzoFileSystem(String uuid) {
		super(uuid);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected DirectoryResource findRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getResource(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryResource makeDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileResource makeFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void registerResource(Resource resource) {
		// TODO Auto-generated method stub		
	}

}
