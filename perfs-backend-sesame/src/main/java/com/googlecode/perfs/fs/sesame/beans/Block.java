package com.googlecode.perfs.fs.sesame.beans;

import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.Blocks)
public interface Block {
	
	@rdf(Ontology.blockData)
	public byte[] getBlockData();
	public void setBlockData(byte[] blockData);
	
}
