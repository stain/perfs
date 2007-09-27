package com.googlecode.perfs.fs;

import java.net.URI;
import java.util.UUID;

import com.googlecode.perfs.fs.memory.MemoryDirectoryResource;
import com.googlecode.perfs.fs.memory.MemoryFileResource;
import com.googlecode.perfs.util.UUIDResource;

/**
 * A file system resource, such as a {@link MemoryFileResource},
 * {@link MemoryDirectoryResource}, etc. In short, a {@link Resource} is anything
 * that can be referred from a filename in a {@link MemoryDirectoryResource}.
 * 
 * @author Stian Soiland
 * 
 */
public abstract class Resource extends UUIDResource {

	private FileSystem fileSystem;

	@Override
	public URI getURIPrefix() {
		return fileSystem.getURIPrefix().resolve(getURIClass());
	}

	/**
	 * The relative URI class, as used by {@link #getURIPrefix()}, for instance
	 * <code>"file/"</code>
	 * 
	 * @return
	 */
	protected abstract URI getURIClass();

	public Resource(FileSystem fileSystem, String uuid) {
		super(uuid);
		init(fileSystem);
	}

	public Resource(FileSystem fileSystem, UUID uuid) {
		super(uuid);
		init(fileSystem);
	}

	public Resource(FileSystem fileSystem) {
		super(UUID.randomUUID());
		init(fileSystem);
	}

	private void init(FileSystem fileSystem) {
		if (fileSystem == null) {
			throw new NullPointerException("fileSystem can't be null");
		}
		this.fileSystem = fileSystem;
		fileSystem.registerResource(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getFileSystem() == null) ? 0 : getFileSystem().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Resource))
			return false;
		final Resource other = (Resource) obj;
		if (getFileSystem() == null) {
			if (other.getFileSystem() != null)
				return false;
		} else if (!getFileSystem().equals(other.getFileSystem()))
			return false;
		return true;
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	
	
}
