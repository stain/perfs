package com.googlecode.perfs.fs.sesame.beans;

import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.URI_BASE + "FileSystem")
public interface FileSystem extends DomainEntity {
	
	@rdf(Ontology.URI_BASE + "hasRootFolder")
	public Folder getRoot();
	public void setRoot(Folder root);
	
}
