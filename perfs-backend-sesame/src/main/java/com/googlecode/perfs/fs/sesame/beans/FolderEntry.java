package com.googlecode.perfs.fs.sesame.beans;

import org.openrdf.elmo.annotations.inverseOf;
import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.FolderEntry)
public interface FolderEntry extends DomainEntity {

	@rdf(Ontology.entryOfFolder)
	@inverseOf(Ontology.hasFolderEntry)
	public Folder getFolderEntryOf();

	public void setFolderEntryOf(Folder folder);

	@rdf(Ontology.fileName)
	public String getFileName();

	public void setFileName(String fileName);

	@rdf(Ontology.resourceOfEntry)
	@inverseOf(Ontology.entryForResource)
	public Resource getResource();

	public void setResource(Resource resource);

}
