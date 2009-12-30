package com.googlecode.perfs.fs.sesame.beans;

import java.util.List;
import java.util.Set;

import org.openrdf.elmo.annotations.rdf;

@rdf(Ontology.Blocks)
public interface Blocks extends DomainEntity {
	@rdf(Ontology.hasSize)
	public long getSize();

	public void setSize(long size);

	@rdf(Ontology.hasHashSum)
	public Set<HashSum> getHashSums();

	public void setHashSums(Set<HashSum> hashSums);

	@rdf(Ontology.hasBlockList)
	public List<Block> getBlockList();
	
	public void setBlockList(List<Block> blockList);
	
}
