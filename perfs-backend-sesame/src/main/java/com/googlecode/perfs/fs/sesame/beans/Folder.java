package com.googlecode.perfs.fs.sesame.beans;

import java.util.Set;

import org.openrdf.elmo.annotations.inverseOf;
import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.URI_BASE + "Folder")
public interface Folder extends Resource {

	@rdf(Ontology.URI_BASE + "hasFolderEntry")
	@inverseOf(Ontology.URI_BASE + "entryForResource")
	public Set<FolderEntry> getFolderEntries();
	
	public void setFolderEntries(Set<FolderEntry> folderEntries);
	
}
