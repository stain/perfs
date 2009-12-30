package com.googlecode.perfs.fs.sesame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.googlecode.perfs.fs.sesame.beans.Block;
import com.googlecode.perfs.fs.sesame.beans.Blocks;

public class BlockBasedInputStream extends InputStream {

	private Blocks blocks;
	private Iterator<Block> blockIterator;
	private Block block;
	private ByteArrayInputStream blockStream;

	public BlockBasedInputStream(Blocks blocks) {
		this.blocks = blocks;
		blockIterator = blocks.getBlockList().iterator();
	}

	@Override
	public int read() throws IOException {
		if (blocks == null) {
			throw new IOException("Stream is closed");
		}
		if (blockStream == null) {
			// Load first block
			if (! nextBlock()) {
				// This stream must be empty
				return -1;
			}
		}
		// Read byte
		int value = blockStream.read();
		if (value != -1) {
			// Not yet finished
			return value;
		}
		// If we are here, the block ran out
		if (! nextBlock()) {
			// And no more blocks
			return -1;
		}
		// Assume there's no empty blocks in the middle..
		return blockStream.read();
	}

	protected boolean nextBlock() throws IOException {
		if (! blockIterator.hasNext()) {
			return false;
		}
		block = blockIterator.next();
		if (blockStream != null) {
			blockStream.close();
		}
		blockStream = new ByteArrayInputStream(block.getBlockData());
		return true;
	}
	
	@Override
	public void close() throws IOException {
		if (blockStream != null) {
			blockStream.close();
			blockIterator = null;
			blockStream = null;
			blocks = null;
			block = null;
		}
	}
	

}
