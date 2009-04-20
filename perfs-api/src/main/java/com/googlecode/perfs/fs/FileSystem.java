package com.googlecode.perfs.fs;

import java.net.URI;
import java.util.UUID;

import com.googlecode.perfs.util.UUIDResource;

public abstract class FileSystem extends UUIDResource {

	public static final String URI_BASE = "http://perfs.googlecode.com/2009/";
	
	private DirectoryResource root;

	public FileSystem() {
		super();
	}

	public FileSystem(String uuid) {
		super(uuid);
	}
	
	@Override
	public URI getURIPrefix() {
		return URI.create(URI_BASE).resolve("fs/resources/");
	}

	public DirectoryResource getRoot() {
		if (root == null) {
			root = findRoot();
		}
		return root;
	}

	protected abstract DirectoryResource findRoot();

	protected abstract void registerResource(Resource resource);
	
	public Resource getResource(String string) {
		return getResource(UUID.fromString(string));
	}

	public abstract Resource getResource(UUID uuid);

	public abstract FileResource makeFile();

	public abstract DirectoryResource makeDirectory();

}
