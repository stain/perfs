package com.googlecode.perfs.fs.sesame.beans;

import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.URI_BASE + "File")
public interface File extends Resource {
	
	@rdf(Ontology.URI_BASE + "hasSize")
	public long getSize();

	public void setSize(long size);
	
}
