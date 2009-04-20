package com.googlecode.perfs.fs.sesame.beans;

import java.util.Set;

import org.openrdf.elmo.annotations.inverseOf;
import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.URI_BASE + "Resource")
public interface Resource extends DomainEntity {

	@rdf(Ontology.URI_BASE + "")
	@inverseOf(Ontology.URI_BASE + "isResource")
	public Set<FolderEntry> getFolderEntries();
	
}
