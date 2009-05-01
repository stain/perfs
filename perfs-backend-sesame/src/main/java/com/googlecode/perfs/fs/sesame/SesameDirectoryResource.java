package com.googlecode.perfs.fs.sesame;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.googlecode.perfs.fs.AlreadyExistsException;
import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.Resource;
import com.googlecode.perfs.fs.sesame.beans.Folder;
import com.googlecode.perfs.fs.sesame.beans.FolderEntry;

public class SesameDirectoryResource extends DirectoryResource implements
		ElmoBeanBased<Folder> {

	private final Folder elmoBean;

	protected SesameDirectoryResource(SesameFileSystem sesameFileSystem,
			Folder elmoBean) {
		super(sesameFileSystem, elmoBean.getQName().getLocalPart());
		this.elmoBean = elmoBean;
	}

	@Override
	public void clear() {
		// TODO: Delete old FolderEntry's
		elmoBean.setFolderEntries(new HashSet<FolderEntry>());
	}

	@Override
	public boolean containsFilename(String filename) {
		// FIXME: Make more efficient using query!
		for (FolderEntry folderEntry : elmoBean.getFolderEntries()) {
			if (folderEntry.getFileName().equals(filename)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsResource(Resource resource) {
		// FIXME: Make more efficient using query!
		for (FolderEntry folderEntry : elmoBean.getFolderEntries()) {
			if (folderEntry.getResource().equals(resource)) {
				return true;
			}
		}
		return false;

	}

	@Override
	public Resource get(String filename) {
		for (FolderEntry folderEntry : elmoBean.getFolderEntries()) {
			if (folderEntry.getFileName().equals(filename)) {
				return getFileSystem().getResource(folderEntry.getResource());
			}
		}
		return null;
	}

	@Override
	public SesameFileSystem getFileSystem() {
		return (SesameFileSystem) super.getFileSystem();
	}

	@Override
	public DirectoryResource getParent() {
		for (FolderEntry folderEntry : getFileSystem().getElmoManager()
				.findAll(FolderEntry.class)) {
			if (folderEntry.getResource().equals(elmoBean)) {
				return (DirectoryResource) getFileSystem().getResource(
						folderEntry.getFolderEntryOf());
			}
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return elmoBean.getFolderEntries().isEmpty();
	}

	@Override
	public void put(String filename, Resource resource)
			throws AlreadyExistsException {
		FolderEntry folderEntry = getFileSystem().getElmoManager().create(
				FolderEntry.class);
		folderEntry.setFileName(filename);
		// FIXME: Avoid this mega-casting..
		com.googlecode.perfs.fs.sesame.beans.Resource resourceBean = 
			((ElmoBeanBased<? extends com.googlecode.perfs.fs.sesame.beans.Resource>) resource)
				.getElmoBean();
		folderEntry.setResource(resourceBean);
		folderEntry.setFolderEntryOf(getElmoBean());
	}

	@Override
	public void putAll(Map<String, ? extends Resource> entries)
			throws AlreadyExistsException {
		for (Entry<String, ? extends Resource> entry : entries.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Resource remove(String filename) {
		FolderEntry deleteEntry = null;
		for (FolderEntry folderEntry : getElmoBean().getFolderEntries()) {
			if (folderEntry.getFileName().equals(filename)) {
				deleteEntry = folderEntry;
			}
		}
		if (deleteEntry == null) {
			return null;
		}
		deleteEntry.setFolderEntryOf(null);
		getElmoBean().getFolderEntries().remove(deleteEntry);
		return getFileSystem().getResource(deleteEntry.getResource());
	}

	@Override
	public Collection<String> resourceNames() {
		Set<String> fileNames = new HashSet<String>();
		for (FolderEntry entry : getElmoBean().getFolderEntries()) {
			fileNames.add(entry.getFileName());
		}
		return fileNames;
	}

	@Override
	public Collection<Resource> resources() {
		Set<Resource> resources = new HashSet<Resource>();
		for (FolderEntry entry : getElmoBean().getFolderEntries()) {
			resources.add(getFileSystem().getResource(entry.getResource()));
		}
		return resources;
	}

	@Override
	protected void setParent(DirectoryResource parent) {
		throw new IllegalStateException("Invalid method");
	}

	@Override
	public int size() {
		return 0;
	}

	public Folder getElmoBean() {
		return elmoBean;
	}

}
