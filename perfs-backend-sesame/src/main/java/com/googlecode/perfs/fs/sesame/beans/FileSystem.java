package com.googlecode.perfs.fs.sesame.beans;

import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.FileSystem)
public interface FileSystem extends DomainEntity {
	
	@rdf(Ontology.hasRootFolder)
	public Folder getRoot();
	public void setRoot(Folder root);
	
}
