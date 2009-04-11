package com.googlecode.perfs.fs.sesame;

import java.util.UUID;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.Resource;

public class SesameFileSystem extends FileSystem {

	private SesameManager elmoManager;

	public SesameFileSystem() {
		ElmoModule module = new ElmoModule();
		SesameManagerFactory factory = new SesameManagerFactory(module);
		elmoManager = factory.createElmoManager();
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
		return new SesameDirectoryResource(this);
	}

	@Override
	public FileResource makeFile() {
		return new SesameFileResource(this);
	}

	@Override
	protected void registerResource(Resource resource) {
		// TODO Auto-generated method stub
		
	}

}
