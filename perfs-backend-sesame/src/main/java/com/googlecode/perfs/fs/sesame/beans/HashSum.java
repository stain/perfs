package com.googlecode.perfs.fs.sesame.beans;

import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.HashSum)
public interface HashSum {

	@rdf(Ontology.hashMethod)
	public String getHashMethod();

	@rdf(Ontology.hashValue)
	public String getHashValue();

	public void setHashMethod(String hashMethod);

	public void setHashValue(String hashValue);
	
}
