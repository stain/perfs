package com.googlecode.perfs.fs.sesame.beans;

import java.util.Set;

import org.openrdf.elmo.annotations.inverseOf;
import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.Folder)
public interface Folder extends Resource {

	@rdf(Ontology.hasFolderEntry)
	@inverseOf(Ontology.entryOfFolder)
	public Set<FolderEntry> getFolderEntries();
	
	public void setFolderEntries(Set<FolderEntry> folderEntries);
	
}
