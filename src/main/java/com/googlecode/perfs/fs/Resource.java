package com.googlecode.perfs.fs;

import com.googlecode.perfs.rdf.Filesystem;
import com.googlecode.perfs.util.UUIDResource;


public class Resource extends UUIDResource {

	private Filesystem filesystem;

	public static final String UUID_URI = "urn:resource.identifier:";

	public Resource(Filesystem filesystem, String uuid) {
		super(uuid);
		this.filesystem = filesystem;
	}
	
	
	
}
