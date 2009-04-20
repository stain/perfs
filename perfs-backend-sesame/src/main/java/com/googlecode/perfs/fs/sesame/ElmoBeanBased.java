package com.googlecode.perfs.fs.sesame;

import com.googlecode.perfs.fs.sesame.beans.DomainEntity;

public interface ElmoBeanBased<Concept extends DomainEntity> {
	
	public Concept getElmoBean();
	
}
