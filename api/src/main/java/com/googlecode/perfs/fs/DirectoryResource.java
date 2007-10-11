package com.googlecode.perfs.fs;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public abstract class DirectoryResource extends Resource {

	public DirectoryResource(FileSystem fileSystem) {
		super(fileSystem);
	}

	public DirectoryResource(FileSystem fileSystem, String uuid) {
		super(fileSystem, uuid);
	}

	public DirectoryResource(FileSystem fileSystem, UUID uuid) {
		super(fileSystem, uuid);
	}

	public abstract void clear();

	public abstract boolean containsFilename(String filename);

	public abstract boolean containsResource(Resource resource);

	public abstract Resource get(String filename);

	public abstract DirectoryResource getParent();

	public abstract boolean isEmpty();

	/**
	 * Add a resource with a given filename.
	 * <p>
	 * If the resource is a
	 * {@link com.googlecode.perfs.fs.memory.MemoryDirectoryResource} it must
	 * not already have a parent, ie.
	 * {@link com.googlecode.perfs.fs.memory.MemoryDirectoryResource#getParent()}
	 * must be null.
	 * <p>
	 * The filename can't already exist in the directory, if it is an
	 * AlreadyExistsException is thrown.
	 * 
	 * @param filename
	 *            The filename for the resource
	 * @param resource
	 *            The referenced resource
	 * @throws AlreadyExistsException
	 *             If a resource already exists in the directory with the given
	 *             filename
	 */
	public abstract void put(String filename, Resource resource)
			throws AlreadyExistsException;

	public abstract void putAll(Map<? extends String, ? extends Resource> t)
			throws AlreadyExistsException;

	public abstract Resource remove(String filename);

	public abstract Collection<String> resourceNames();

	public abstract Collection<Resource> resources();
	
	public abstract int size();

	protected abstract void setParent(DirectoryResource parent);

}