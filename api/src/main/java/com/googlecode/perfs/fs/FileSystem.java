package com.googlecode.perfs.fs;

import java.net.URI;
import java.util.UUID;

import com.googlecode.perfs.util.UUIDResource;

public abstract class FileSystem extends UUIDResource {

	private DirectoryResource root;

	public FileSystem(String uuid) {
		super(uuid);
	}

	public FileSystem() {
		super();
	}

	@Override
	public URI getURIPrefix() {
		return URI.create("http://perfs.googlecode.com/").resolve("fs/");
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
