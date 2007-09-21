package com.googlecode.perfs.fs;

import com.googlecode.perfs.util.UUIDResource;


public class Resource extends UUIDResource {

	private Filesystem filesystem;

	public Resource(Filesystem filesystem, String uuid) {
		super(uuid);
		this.filesystem = filesystem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((filesystem == null) ? 0 : filesystem.hashCode());
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
		if (filesystem == null) {
			if (other.filesystem != null)
				return false;
		} else if (!filesystem.equals(other.filesystem))
			return false;
		return true;
	}

	
	
}
