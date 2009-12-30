package com.googlecode.perfs.fs.sesame.beans;

import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.File)
public interface File extends Resource {

	@rdf(Ontology.hasBlockList)
	public Blocks getBlocks();

	public void setBlocks(Blocks blocks);

}
