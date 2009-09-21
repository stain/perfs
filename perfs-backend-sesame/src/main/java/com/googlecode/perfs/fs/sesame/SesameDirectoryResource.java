package com.googlecode.perfs.fs.sesame;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.openrdf.elmo.ElmoManager;

import com.googlecode.perfs.fs.AlreadyExistsException;
import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.sesame.beans.Folder;
import com.googlecode.perfs.fs.sesame.beans.FolderEntry;
import com.googlecode.perfs.fs.sesame.beans.Resource;

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
	public boolean containsResource(com.googlecode.perfs.fs.Resource resource) {		
		if (! (resource instanceof ElmoBeanBased)) {
			return false;
		}
		Resource elmoResource = ((ElmoBeanBased<Resource>)resource).getElmoBean();
		for (FolderEntry folderEntry : elmoResource.getEntriesForResource()) {
			// Typically a resource lives only in one folder
			if (folderEntry.getFolderEntryOf().equals(elmoBean)) {
				return true;
			}
		}
		return false;

	}

	@Override
	public com.googlecode.perfs.fs.Resource get(String filename) {
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
		Iterator<FolderEntry> entriesIt = elmoBean.getEntriesForResource().iterator();
		if (! entriesIt.hasNext()) {
			return null;
		}
		FolderEntry parentEntry = entriesIt.next();
		if (entriesIt.hasNext()) {
			throw new IllegalStateException("Folder can't have multiple parents: " + elmoBean.getQName());
		}
		Folder folderEntryOf = parentEntry.getFolderEntryOf();
		
		return (DirectoryResource) getFileSystem().getResource(folderEntryOf);
	}

	@Override
	public boolean isEmpty() {
		return elmoBean.getFolderEntries().isEmpty();
	}

	@Override
	public void put(String filename, com.googlecode.perfs.fs.Resource resource)
			throws AlreadyExistsException {
		if (containsFilename(filename)) {
			throw new AlreadyExistsException(filename);
		}
		if (resource instanceof DirectoryResource) {
			 DirectoryResource directoryResource = (DirectoryResource)resource;
			 if (directoryResource.getParent() != null) {
				 throw new IllegalStateException("A directory can't have two parents");
			 }
		}
		
		FolderEntry folderEntry = getFileSystem().getElmoManager().create(
				FolderEntry.class);
		folderEntry.setFileName(filename);
		// FIXME: Avoid this mega-casting..
		Resource resourceBean = 
			((ElmoBeanBased<? extends Resource>) resource)
				.getElmoBean();
		folderEntry.setResource(resourceBean);
		folderEntry.setFolderEntryOf(getElmoBean());
		
	}

	@Override
	public void putAll(Map<String, ? extends com.googlecode.perfs.fs.Resource> entries)
			throws AlreadyExistsException {
		for (Entry<String, ? extends com.googlecode.perfs.fs.Resource> entry : entries.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public com.googlecode.perfs.fs.Resource remove(String filename) {
		FolderEntry deleteEntry = null;
		for (FolderEntry folderEntry : getElmoBean().getFolderEntries()) {
			if (folderEntry.getFileName().equals(filename)) {
				deleteEntry = folderEntry;
				break;
			}
		}
		if (deleteEntry == null) {
			return null;
		}
		
		ElmoManager elmoManager = getElmoBean().getElmoManager();
		Resource resource = deleteEntry.getResource();
		Folder folderEntryOf = deleteEntry.getFolderEntryOf();
		elmoManager.remove(deleteEntry);
		elmoManager.refresh(resource);
		elmoManager.refresh(folderEntryOf);
		elmoManager.refresh(elmoBean);		
		return getFileSystem().getResource(resource);
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
	public Collection<com.googlecode.perfs.fs.Resource> resources() {
		Set<com.googlecode.perfs.fs.Resource> resources = new HashSet<com.googlecode.perfs.fs.Resource>();
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
		return getElmoBean().getFolderEntries().size();
	}

	public Folder getElmoBean() {
		return elmoBean;
	}

}
