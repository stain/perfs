package com.googlecode.perfs.blocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


/**
 * Storage of large data by splitting it into smaller blocks.
 * 
 * @author Stian Soiland
 * 
 */
public class LargeBlockStore implements BlockStore {

	private static Logger logger = Logger.getLogger(LargeBlockStore.class);
	
	// Default block size
	private static final int BLOCKSIZE = 65536;

	private static final String HEADER = "perfs largestore v0.1";

	private static final String NEXT = " next ";

	private static final String SINGLE = " single";

	private static final int UUID_SIZE = UUID.randomUUID().toString().length();

	private final int MAX_SIZE_LAST;

	private final int MAX_SIZE_SINGLE;

	private final int MAX_SIZE;

	public final int blocksize;

	private BlockStore backendStore;

	public LargeBlockStore(BlockStore backendStore)
			throws IllegalArgumentException {
		this(backendStore, BLOCKSIZE);
	}

	public LargeBlockStore(BlockStore backendStore, int blocksize) {
		this.backendStore = backendStore;
		this.blocksize = blocksize;

		// pre-calculated limits:
		// HEADER + "\n"
		MAX_SIZE_LAST = blocksize - HEADER.length() - 1;
		// HEADER + " single" + "\n"
		MAX_SIZE_SINGLE = MAX_SIZE_LAST - SINGLE.length();
		// HEADER + " next " + uuid + "\n"
		MAX_SIZE = MAX_SIZE_LAST - NEXT.length() - UUID_SIZE;

	}

	public void clear() {
		backendStore.clear();
	}

	public boolean containsKey(String key) {
		return backendStore.containsKey(key);
	}

	public InputStream get(String key) {
		/*byte[] firstBlock = backendStore.get(key);
		if (firstBlock == null) {
			return null; // Not found
		}
		String header = firstAsciiLine(firstBlock);
		if (header.contains(SINGLE)) {
			int size = firstBlock.length - header.length() - 1;
			byte[] block = new byte[size];
			System.arraycopy(firstBlock, header.length() + 1, block, 0, size);
			return block;
		}

		List<String> blocks = blockList(key);
		if (blocks.isEmpty()) {
			return new byte[0];
		}
		// Need to guesstimate the size first.. We'll assume
		// all blocks except last one is full size
		// FIXME: int size max. 2 GB
		int size = (blocks.size() - 1) * blocksize;
		String lastBlock = blocks.get(blocks.size() - 1);
		System.out.println("Last block is " + lastBlock);
		size += backendStore.get(lastBlock).length;

		byte[] data = new byte[size];
		int dataPos = 0;
		for (String blockKey : blocks) {
			byte[] block = backendStore.get(blockKey);
			if (blockKey != lastBlock && block.length != blocksize) {
				throw new RuntimeException("Block " + blockKey
						+ " invalid size " + block.length + " in " + key);
			}
			if (dataPos + block.length > data.length) {
				throw new RuntimeException("Blocks exceeed expected size for "
						+ key);
			}
			System.arraycopy(block, 0, data, dataPos, block.length);
			dataPos += block.length;
		}
		return data;*/
		return null;
	}

	public List<String> blockList(String key) {
		List<String> blocks = new ArrayList<String>();
		// To avoid loops in faulty block lists
		Set<String> visited = new HashSet<String>();
		while (true) {
			if (visited.contains(key)) {
				throw new RuntimeException("Looping blocklist: " + key);
			}
			visited.add(key);
			InputStream data2 = backendStore.get(key);
			byte[] data;
			try {
				data = IOUtils.toByteArray(data2);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				logger.warn("An error occured", ex);
				return null;
			}
			String header = firstAsciiLine(data);
			if (!header.startsWith(HEADER)) {
				throw new RuntimeException("Unknown header for " + key + ": "
						+ header);
			}
			int blockStart = header.length() + 1;
			String block;
			try {
				block = new String(data, blockStart, data.length - blockStart,
						"ascii");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Unsupported encoding ASCII", e);
			}
			for (String blockKey : block.split("\n")) {
				if (blockKey.length() > 0) {
					blocks.add(blockKey);
				}
			}
			if (!header.contains(NEXT)) {
				return blocks;
			}
			key = header.split(NEXT, 2)[1];
		}
	}

	private String firstAsciiLine(byte[] data) {
		int i;
		for (i = 0; i < data.length; i++) {
			if (data[i] == '\n' || data[i] > 127) {
				break;
			}
		}
		try {
			return new String(data, 0, i, "ascii");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Could not parse bytes as ascii");
		}
	}

	public String put(byte[] bytes) {
		String key = UUID.randomUUID().toString();
		put(key, bytes);
		return key;
	}

	public void put(String key, byte[] bytes) {
		// FIXME: int bytes.length size max. 2 GB
		// Special case, if the data fits in a single block,
		// we'll pop it in there.
		if (bytes.length <= MAX_SIZE_SINGLE) {
			putSingleBlock(key, bytes);
			return;
		}
		String blocks = "";
		int bytesPos = 0;
		while (bytesPos < bytes.length) {
			int size = Math.min(blocksize, bytes.length - bytesPos);
			byte[] block = new byte[size];
			System.arraycopy(bytes, bytesPos, block, 0, size);
			bytesPos += size;
			String blockId = backendStore.put(block);
			blocks += blockId + "\n";

			// Check if we need to store our blocklist already
			if (blocks.length() > MAX_SIZE) {
				// FIXME: Check if there's enough space to store this
				// as the very last block instead
				String nextBlockList = UUID.randomUUID().toString();
				try {
					putSingleBlock(key, HEADER + NEXT + nextBlockList, blocks
							.getBytes("ascii"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(
							"Could not encode blocklist as ascii", e);
				}
				key = nextBlockList;
				blocks = "";
			}
		}
		// Store last block (even if empty..)
		try {
			putSingleBlock(key, HEADER, blocks.getBytes("ascii"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Could not encode blocklist as ascii", e);
		}

	}

	private void putSingleBlock(String key, byte[] bytes) {
		putSingleBlock(key, HEADER + SINGLE, bytes);
	}

	private void putSingleBlock(String key, String header, byte[] bytes) {
		// Special case, if the data fits in a single block,
		// we'll pop it in there.
		header = header + "\n";
		byte[] block = new byte[header.length() + bytes.length];
		int blockPos = 0;
		try {
			for (byte b : header.getBytes("ascii")) {
				block[blockPos++] = b;
			}
		} catch (UnsupportedEncodingException e) {
			// Should never happen
			throw new RuntimeException("Could not encode header as ascii", e);
		}
		if (bytes.length + blockPos > block.length) {
			throw new RuntimeException("Invalid block content size "
					+ block.length);
		}
		for (byte b : bytes) {
			block[blockPos++] = b;
		}
		backendStore.put(key, block);
	}

	public void remove(String key) {
		// To avoid loops in faulty block lists
		Set<String> visited = new HashSet<String>();
		while (true) {
			if (visited.contains(key)) {
				throw new RuntimeException("Looping blocklist: " + key);
			}
			visited.add(key);
			InputStream data2 = backendStore.get(key);
			backendStore.remove(key);
			byte[] data;
			try {
				data = IOUtils.toByteArray(data2);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				logger.warn("An error occured", ex);
				return;
			}

			String header = firstAsciiLine(data);
			if (!header.startsWith(HEADER)) {
				throw new RuntimeException("Unknown header for " + key + ": "
						+ header);
			}
			if (!header.contains(NEXT)) {
				return;
			}
			String next = header.split(NEXT, 2)[1];
			remove(next);
		}
		// FIXME: Also de-reference/remove hashed datablocks
		// (we can't do this now because other files might
		// also refer to them)
	}

	public byte[] getBytes(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
