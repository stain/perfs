package com.googlecode.perfs.blocks;

import java.io.InputStream;

public interface BlockStore {

	/**
	 * Reinitializes the block store, deletes all existing blocks.
	 * 
	 */
	public void clear();

	/**
	 * Check if given key exists in block store. 
	 * 
	 * @param key The identifier to check
	 * @return True if the key is retrievable by get(key)
	 */
	public boolean containsKey(String key);

	/**
	 * Get the block data stored at given key
	 * 
	 * @param key Identifier of block
	 * @return An InputStream reading from the given block
	 */
	public InputStream get(String key);

	public byte[] getBytes(String key);
	
	/**
	 * Store block of bytes by generating a new identifier. This
	 * identifier could be a hash of the bytes or a new
	 * UUID.
	 * 
	 * @param bytes Block to store
	 * @return New identifier.
	 */
	public String put(byte[] bytes);

	/**
	 * Store block of bytes using given identifier. The block can later be
	 * retrieved using get(key). If a block with identifier exists, it will
	 * be overwritten. 
	 * 
	 * @param key Identifier to store bytes as.
	 * @param bytes Block to store
	 */
	public void put(String key, byte[] bytes);

	/**
	 * Remove block stored under the given identifier.
	 * 
	 * @param key Identifier for block
	 */
	public void remove(String key);

}