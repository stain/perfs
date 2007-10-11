package com.googlecode.perfs.fs.memory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import com.googlecode.perfs.fs.AlreadyExistsException;
import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.Resource;


/**
 * A directory is a mapping from filenames to resources.
 * <p>
 * Note that although this class resembles the {@link Map} interface it violates
 * it by not including the *Set() methods, and that key/value has been renamed
 * to filename/resource). In addition, put() on existing filenames is not
 * allowed.
 * 
 * @author Stian Soiland
 * 
 */
public class MemoryDirectoryResource extends DirectoryResource {

	private Map<String, Resource> children = new HashMap<String, Resource>();
	
	private DirectoryResource parent;

	public MemoryDirectoryResource(FileSystem filesystem) {
		super(filesystem);
	}

	protected MemoryDirectoryResource(FileSystem filesystem, UUID uuid) {
		super(filesystem, uuid);
	}
	
	protected MemoryDirectoryResource(FileSystem filesystem, String uuid) {
		super(filesystem, uuid);
	}

	@Override
	public void clear() {
		for (String filename : new ArrayList<String>(children.keySet())) {
			remove(filename);
		}
	}

	@Override
	public boolean containsFilename(String filename) {
		return children.containsKey(filename);
	}

	@Override
	public boolean containsResource(Resource resource) {
		return children.containsValue(resource);
	}

	@Override
	public Resource get(String filename) {
		return children.get(filename);
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty();
	}

	/**
	 * Add a resource with a given filename.
	 * <p>
	 * If the resource is a {@link MemoryDirectoryResource} it must not already have a
	 * parent, ie. {@link MemoryDirectoryResource#getParent()} must be null.
	 * <p>
	 * The filename can't already exist in the directory, if it is an
	 * AlreadyExistsException is thrown.
	 * 
	 * @param filename The filename for the resource
	 * @param resource The referenced resource
	 * @throws AlreadyExistsException
	 *             If a resource already exists in the directory with the given
	 *             filename
	 */
	@Override
	public void put(String filename, Resource resource) throws AlreadyExistsException {
		if (children.containsKey(filename)) {
			throw new AlreadyExistsException(filename);
		}
		if (resource instanceof MemoryDirectoryResource) {
			MemoryDirectoryResource subDir = (MemoryDirectoryResource) resource;
			synchronized (subDir) {
				if (subDir.getParent() != null) {
					throw new IllegalStateException("Inserted subdirectory "
							+ subDir + "already has a parent "
							+ subDir.getParent());
				}
				subDir.setParent(this);
			}
		}
		Resource old = children.put(filename, resource);
		if (old instanceof MemoryDirectoryResource) {
			throw new AlreadyExistsException(filename);
		}
	}

	@Override
	public void putAll(Map<? extends String, ? extends Resource> t) throws AlreadyExistsException {
		for (Entry<? extends String, ? extends Resource> entry : t.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Resource remove(String filename) {
		Resource resource = children.remove(filename);
		if (resource instanceof MemoryDirectoryResource) {
			MemoryDirectoryResource dir = (MemoryDirectoryResource) resource;
			synchronized (dir) {
				if (!this.equals(dir.getParent())) {
					// Unexpected
					throw new IllegalStateException("Removed directory "
							+ dir + " didn't have parent " + this + ", but "
							+ dir.getParent());
				}
				dir.setParent(null);
			}
		}
		return resource;
	}

	@Override
	public int size() {
		return children.size();
	}

	@Override
	public Collection<Resource> resources() {
		return children.values();
	}

	@Override
	public Collection<String> resourceNames() {
		return children.keySet();
	}

	@Override
	public DirectoryResource getParent() {
		return parent;
	}

	/**
	 * Set parent 
	 * 
	 * @param parent
	 */
	@Override
	protected void setParent(DirectoryResource parent) {
		if (parent != null && this.parent != null) {
			throw new IllegalStateException("Directory " + this
					+ " already has parent " + this.parent);
		}
		this.parent = parent;
	}

	@Override
	protected URI getURIClass() {
		return URI.create("dir/");
	}

}
