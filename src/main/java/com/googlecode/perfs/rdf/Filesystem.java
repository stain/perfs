package com.googlecode.perfs.rdf;

import com.googlecode.perfs.blocks.BlockStore;
import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.util.UUIDResource;


public class Filesystem extends UUIDResource {

	private BlockStore blockStore;
	private DirectoryResource root;
	
	public Filesystem(BlockStore blockStore, String uuid) {
		super(uuid);
		this.blockStore = blockStore;
	}

	public Filesystem(BlockStore blockStore) {
		super();
		this.blockStore = blockStore;
	}
	
	public DirectoryResource getRoot() {
		if (root == null) {
			root = new DirectoryResource(this, uuid);
		}
		return root;
	}
	

}
