package com.googlecode.perfs.fs.sesame.beans;

import org.openrdf.elmo.annotations.inverseOf;
import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.URI_BASE + "FolderEntry")
public interface FolderEntry extends DomainEntity {

	@rdf(Ontology.URI_BASE + "entryOfFolder")
	@inverseOf(Ontology.URI_BASE + "hasFolderEntry")
	public Folder getFolderEntryOf();
	
	public void setFolderEntryOf(Folder folder);

	@rdf(Ontology.URI_BASE + "fileName")
	public String getFileName();

	public void setFileName(String fileName);

	@rdf(Ontology.URI_BASE + "entryForResource")
	public Resource getResource();

	public void setResource(Resource resource);

}
