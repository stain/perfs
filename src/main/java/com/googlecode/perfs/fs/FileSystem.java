package com.googlecode.perfs.fs;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.googlecode.perfs.blocks.BlockStore;
import com.googlecode.perfs.util.UUIDResource;

public class FileSystem extends UUIDResource {

	@SuppressWarnings("unused")
	private BlockStore blockStore;
	
	private DirectoryResource root;
	
	private Map<UUID, Resource> resources = new HashMap<UUID, Resource>();

	public FileSystem(BlockStore blockStore, String uuid) {
		super(uuid);
		this.blockStore = blockStore;
	}

	public FileSystem(BlockStore blockStore) {
		super();
		this.blockStore = blockStore;
	}

	@Override
	public URI getURIPrefix() {
		return URI.create("http://perfs.googlecode.com/").resolve("fs/");
	}

	public DirectoryResource getRoot() {
		if (root == null) {
			root = new DirectoryResource(this, getUUIDString());
		}
		return root;
	}

	protected void registerResource(Resource resource) {
		if (!this.equals(resource.getFileSystem())) {
			throw new IllegalArgumentException(
					"Resource must be bound to filesystem " + this);
		}
		resources.put(resource.getUUID(), resource);
	}

	public Resource getResource(String string) {
		return getResource(UUID.fromString(string));
	}

	public Resource getResource(UUID uuid) {
		return resources.get(uuid);
	}

}
