package com.googlecode.perfs.util;

import java.net.URI;
import java.util.ConcurrentModificationException;
import java.util.UUID;

/**
 * A resource that has an {@link UUID} identifier, available from
 * {@link #getUUID()} or {@link #getUUIDString()}, in full version a
 * {@link URI} exported by {@link #getURI()}.
 * <p>
 * Two {@link UUIDResource}s are considered {@link #equals(Object)} if they
 * have the same UUID. Subclasses might enforce further equality checks.
 * 
 * @author Stian Soiland-Reyes
 * 
 */
public class UUIDResource {

	public static final String UUID_URI_PREFIX = "urn:resource.identifier:";

	private UUID uuid;

	private URI uri = null;

	public UUIDResource() {
		this(UUID.randomUUID());
	}

	public UUIDResource(String uuid) {
		this(UUID.fromString(uuid));
	}

	public UUIDResource(UUID uuid) {
		if (uuid == null) {
			throw new NullPointerException("UUID can't be null");
		}
		this.uuid = uuid;
	}

	public URI getURI() throws ConcurrentModificationException {
		UUID originalUUID = uuid;
		if (uri == null) {
			this.uri = getURIPrefix().resolve(originalUUID.toString());
		}
		if (uuid != originalUUID) {
			throw new ConcurrentModificationException("UUID changed while getting URI");
		}
		return uri;
	}

	public URI getURIPrefix() {
		return URI.create(UUID_URI_PREFIX);
	}

	public UUID getUUID() {
		return uuid;
	}

	public String getUUIDString() {
		return getUUID().toString();
	}

	@Override
	public int hashCode() {
		return getUUID().hashCode();
	}

	protected synchronized void setUUID(UUID uuid) {
		this.uuid = uuid;
		this.uri = null;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UUIDResource)) {
			return false;
		}
		UUIDResource uuidResource = (UUIDResource) obj;
		return getUUID().equals(uuidResource.getUUID());
	}

	@Override
	public String toString() {
		return getURI().toASCIIString();
	}

}
