package com.googlecode.perfs.fs.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.googlecode.perfs.blocks.BlockStore;
import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.Resource;

public class MemoryFileSystem extends FileSystem {

	@SuppressWarnings("unused")
	private BlockStore blockStore;
	
	private Map<UUID, Resource> resources = new HashMap<UUID, Resource>();

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MemoryFileSystem.class);

	@Override
	protected void registerResource(Resource resource) {
		if (!this.equals(resource.getFileSystem())) {
			throw new IllegalArgumentException(
					"Resource must be bound to filesystem " + this);
		}
		resources.put(resource.getUUID(), resource);
	}

	@Override
	public Resource getResource(UUID uuid) {
		return resources.get(uuid);
	}

	@Override
	protected DirectoryResource findRoot() {
		return new MemoryDirectoryResource(this);
	}
}
