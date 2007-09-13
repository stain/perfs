package com.googlecode.perfs.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class UUIDResource {
	public final String uuid;

	public final URI uri;

	public static final String UUID_URI_PREFIX = "urn:resource.identifier:";
	
	public UUIDResource() {
		this(UUID.randomUUID().toString());
	}
	
	public UUIDResource(String uuid) {
		this.uuid = uuid;
		try {
			this.uri = new URI(UUID_URI_PREFIX + uuid);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid UUID: " + uuid, e);
		}
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + uuid;
	}
	
}
