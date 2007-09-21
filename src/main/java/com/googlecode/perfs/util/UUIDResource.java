package com.googlecode.perfs.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.apache.log4j.Logger;

public class UUIDResource {
	
	private static Logger logger = Logger.getLogger(UUIDResource.class);
	
	public static final String UUID_URI_PREFIX = "urn:resource.identifier:";

	private final UUID uuid;

	private URI uri = null;

	public UUIDResource() {
		this(UUID.randomUUID());
	}

	public UUIDResource(String uuid) {
		this(UUID.fromString(uuid));
	}

	public UUIDResource(UUID uuid) {
		this.uuid = uuid;
	}

	public URI getURI() {
		if (uri == null) {
			this.uri = getURIPrefix().resolve(uuid.toString());
		}
		return uri;
	}

	protected URI getURIPrefix() {
		return URI.create(UUID_URI_PREFIX);
	}

	public UUID getUUID() {
		return uuid;
	}

	public String getUUIDString() {
		return uuid.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getUUID() == null) ? 0 : getUUID().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return getURI().toASCIIString();
	}

}
