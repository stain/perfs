package com.googlecode.perfs.fs;

import java.net.URI;
import java.util.UUID;

import com.googlecode.perfs.util.UUIDResource;

/**
 * A file system resource, such as a {@link FileResource},
 * {@link DirectoryResource}, etc. In short, a {@link Resource} is anything
 * that can be referred from a filename in a {@link DirectoryResource}.
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

	public Resource(FileSystem filesystem, String uuid) {
		super(uuid);
		init(filesystem);
	}

	public Resource(FileSystem filesystem, UUID uuid) {
		super(uuid);
		init(filesystem);
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
