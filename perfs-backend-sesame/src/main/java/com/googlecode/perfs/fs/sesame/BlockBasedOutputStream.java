package com.googlecode.perfs.fs.sesame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.perfs.fs.sesame.beans.Block;
import com.googlecode.perfs.fs.sesame.beans.Blocks;

public class BlockBasedOutputStream extends OutputStream {

	private final List<Block> blockList;
	private final SesameFileSystem sesameFileSystem;
	private final BlockBasedOutputStreamCallback callback;
	private ByteArrayOutputStream blockStream;

	public BlockBasedOutputStream(SesameFileSystem sesameFileSystem, List<Block> blockList,
			BlockBasedOutputStreamCallback callback) {
		this.sesameFileSystem = sesameFileSystem;
		this.callback = callback;
		// TODO: Support overwrite of existing blocks
		this.blockList = new ArrayList<Block>();
	}

	@Override
	public void write(int b) throws IOException {
		if (blockStream == null || blockStream.size() >= sesameFileSystem.getBlockSize()) {
			nextBlockStream();
		}
		blockStream.write(b);
	}

	private void nextBlockStream() {
		storeBlockStream();
		blockStream = new ByteArrayOutputStream(sesameFileSystem.getBlockSize());
	}

	private void storeBlockStream() {
		if (blockStream == null) {
			return;
		}
		Block block = sesameFileSystem.getElmoManager().create(sesameFileSystem.getNewQName(), Block.class);
		block.setBlockData(blockStream.toByteArray());
		blockList.add(block);
	}

	@Override
	public void close() throws IOException {
		storeBlockStream();		
		Blocks blocks = sesameFileSystem.getElmoManager().create(sesameFileSystem.getNewQName(), Blocks.class);
		blocks.setBlockList(blockList);
		sesameFileSystem.calculateSize(blocks);
		sesameFileSystem.calculateHashes(blocks);
		callback.closedFile(blocks);
	};
	

	public interface BlockBasedOutputStreamCallback {
		public void closedFile(Blocks blocks);		
	}

}
