package com.googlecode.perfs.fs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class DirectoryResource extends Resource implements
		Map<String, Resource> {

	private Map<String, Resource> children = new HashMap<String, Resource>();
	private DirectoryResource parent;

	public DirectoryResource(Filesystem filesystem) {
		super(filesystem, UUID.randomUUID().toString());
	}

	protected DirectoryResource(Filesystem filesystem, String uuid) {
		super(filesystem, uuid);
	}

	public void clear() {
		children.clear();
	}

	public boolean containsKey(Object key) {
		return children.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return children.containsValue(value);
	}

	public Set<Entry<String, Resource>> entrySet() {
		return children.entrySet();
	}

	public Resource get(Object key) {
		return children.get(key);
	}

	public boolean isEmpty() {
		return children.isEmpty();
	}

	public Set<String> keySet() {
		return children.keySet();
	}

	public Resource put(String key, Resource resource) {
		if (resource instanceof DirectoryResource) {
			DirectoryResource subDir = (DirectoryResource) resource;
			synchronized (subDir) {
				if (subDir.getParent() != null) {
					throw new IllegalStateException("Inserted subdirectory "
							+ subDir + "already has a parent "
							+ subDir.getParent());
				}
				subDir.setParent(this);
			}
		}

		Resource old = children.put(key, resource);

		if (old instanceof DirectoryResource) {
			DirectoryResource oldDir = (DirectoryResource) old;
			synchronized (oldDir) {
				if (!this.equals(oldDir.getParent())) {
					throw new IllegalStateException("Replaced directory "
							+ oldDir + " didn't have parent " + this + ", but "
							+ oldDir.getParent());
				}
				oldDir.setParent(null);
			}
		}
		return old;
	}

	public void putAll(Map<? extends String, ? extends Resource> t) {
		children.putAll(t);
	}

	public Resource remove(Object key) {
		return children.remove(key);
	}

	public int size() {
		return children.size();
	}

	public Collection<Resource> values() {
		return children.values();
	}

	public DirectoryResource getParent() {
		return parent;
	}

	protected void setParent(DirectoryResource parent) {
		this.parent = parent;
	}

}
