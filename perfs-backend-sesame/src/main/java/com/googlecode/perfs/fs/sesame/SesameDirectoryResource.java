package com.googlecode.perfs.fs.sesame;

import java.util.Collection;
import java.util.Map;

import com.googlecode.perfs.fs.AlreadyExistsException;
import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.Resource;

public class SesameDirectoryResource extends DirectoryResource {

	public SesameDirectoryResource(SesameFileSystem sesameFileSystem) {
		super(sesameFileSystem);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean containsFilename(String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsResource(Resource resource) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Resource get(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryResource getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void put(String filename, Resource resource)
			throws AlreadyExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putAll(Map<String, ? extends Resource> t)
			throws AlreadyExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public Resource remove(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> resourceNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Resource> resources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setParent(DirectoryResource parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
