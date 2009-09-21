package com.googlecode.perfs.fs.sesame;

import java.util.Arrays;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoManagerFactory;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.Entity;
import org.openrdf.elmo.sesame.SesameManagerFactory;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.Resource;
import com.googlecode.perfs.fs.sesame.beans.File;
import com.googlecode.perfs.fs.sesame.beans.FileSystem;
import com.googlecode.perfs.fs.sesame.beans.Folder;
import com.googlecode.perfs.util.UUIDResource;

public class SesameFileSystem extends com.googlecode.perfs.fs.FileSystem
		implements ElmoBeanBased<FileSystem> {

	private static final String PREFIX = "perfs";

	private FileSystem elmoBean;
	private ElmoManager elmoManager;

	public SesameFileSystem() {
		super();
		initialize();
	}

	public SesameFileSystem(String uuid) {
		super(uuid);
		initialize();
	}

	@Override
	protected DirectoryResource findRoot() {
		return (DirectoryResource) getResource(getElmoBean().getRoot());
	}

	public FileSystem getElmoBean() {
		return elmoBean;
	}

	protected QName getNewQName() {
		return getQName(UUID.randomUUID());
	}

	protected QName getQName(UUID uuid) {
		return new QName(getURIPrefix().toASCIIString(), uuid.toString());
	}

	protected QName getQName(UUIDResource uuidResource) {
		if (uuidResource.getURIPrefix().equals(getURIPrefix())) {
			return new QName(uuidResource.getURIPrefix().toASCIIString(),
					uuidResource.getUUIDString(), PREFIX);
		} else {
			return new QName(uuidResource.getURIPrefix().toASCIIString(),
					uuidResource.getUUIDString());
		}
	}

	protected WeakHashMap<Entity, Resource> resourceCache = new WeakHashMap<Entity, Resource>();

	private static Logger logger = Logger.getLogger(SesameFileSystem.class);
 
	protected Resource getResource(Entity entity) {
		if (entity == null) {
			return null;
		}
		Resource resource = resourceCache.get(entity);
		if (resource != null) {
			return resource;
		}
		if (entity instanceof Folder) {
			resource = new SesameDirectoryResource(this, (Folder) entity);
		} else if (entity instanceof File) {
			resource = new SesameFileResource(this, (File) entity);
		} else {
			logger.info("Unknown resource for entity " + entity
					+ " -- implements "
					+ Arrays.asList(entity.getClass().getInterfaces()));
			// Basically means 'not found'
			return null;
		}
		synchronized (resourceCache) {
			if (resourceCache.containsKey(entity)) {
				// Someone else beat us to it - we'll return the cached resource
				return resourceCache.get(entity);
			}
			resourceCache.put(entity, resource);
			return resource;
		}
	}

	@Override
	public Resource getResource(UUID uuid) {
		Entity entity = getElmoManager().find(getQName(uuid));
		return getResource(entity);
	}

	protected void initialize() {
		// TODO: Move to spring..?
		ElmoModule module = new ElmoModule();
		SesameManagerFactory factory = new SesameManagerFactory(module);
		factory.setInferencingEnabled(true);
		setElmoManager(factory.createElmoManager());
		// Find and/or create file system and it's root directory
		Entity elmo = getElmoManager().find(getQName(this));
		if (elmo instanceof FileSystem) {
			elmoBean = (FileSystem) elmo;
		} else {
			elmoBean = getElmoManager().create(getQName(this), FileSystem.class);
			elmoBean.setRoot(makeDirectory().getElmoBean());
			makeDirectory();
		}
	}

	@Override
	public SesameDirectoryResource makeDirectory() {
		Folder entity = getElmoManager().create(getNewQName(), Folder.class);		
		return (SesameDirectoryResource) getResource(entity);
	}

	@Override
	public SesameFileResource makeFile() {
		File entity = getElmoManager().create(getNewQName(), File.class);
		return (SesameFileResource) getResource(entity);
	}

	@Override
	protected void registerResource(Resource resource) {
		if (resource.getFileSystem() != this) {
			throw new IllegalArgumentException("Resource must be using file system " + this);
		}
		// No further registration to be done
	}

	public void setElmoManager(ElmoManager elmoManager) {
		this.elmoManager = elmoManager;
	}

	public ElmoManager getElmoManager() {
		return elmoManager;
	}

}
