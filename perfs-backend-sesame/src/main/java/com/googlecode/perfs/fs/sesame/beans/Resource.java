package com.googlecode.perfs.fs.sesame.beans;

import java.util.Set;

import org.openrdf.elmo.annotations.inverseOf;
import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.Resource)
public interface Resource extends DomainEntity {

	@rdf(Ontology.entryForResource)
	@inverseOf(Ontology.resourceOfEntry)
	public Set<FolderEntry> getEntriesForResource();
	
}
