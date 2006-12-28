package com.googlecode.perfs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotSerializableException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;


/**
 * Store blocks (ie. bytes) in a directory structure. 
 * Slightly resembles @link{java.util.Map}.
 * <p>
 * The directory structure will be like this for storing three identified blocks:
 * <pre>
 *   root
 *       3d
 *           3d12312312313ffsdfsdf
 *           3dkjasdsfkjdfdfsdfkjs
 *       fa
 *       	 faasdkjasduasdiausdas    
 * </pre>
 * The identifier will be calculated using the @{link hash(byte[])} method
 * if you store the blocks using @link{put(byte[])}, which prevents storing
 * the same block content twice, as the block will be identified by its hash 
 * value.
 * 
 * @author Stian Soiland
 *
 */
public class BlockStore {

	private static Logger logger = Logger.getLogger(BlockStore.class);
	

	/**
	 * Meta information is stored in this filename in the root directory
	 */
	public static final String META = "perfs.meta";
	
	/**
	 * The header line of the META file.
	 * 
	 * @see META
	 */
	public static final String META_HEADER = "perfs blockstore v0.2";
	
	/**
	 * Use SUBDIRLEN first chars of identifier as subdirectory
	 * name.. ie. identifier "pxls92ksiks" is stored in "px"
	 */
	public static final int SUBDIRLEN = 2;
	
	/**
	 * Root directory, containing the META file and the subdirectories
	 * containing blocks.
	 */
	File root;

	/**
	 * Construct a BlockStore for the given directory. If the directory 
	 * does not yet exist or is empty, it will be initialized as a new
	 * empty BlockStore. If the directory is an existing BlockStore
	 * this instance will use the existing entries. If the directory
	 * is not a BlockStore directory or can't be read/written properly, 
	 * an IllegalArgumentException will be thrown.
	 * 
	 * @param root Base directory for storing blocks
	 * @throws IllegalArgumentException if root is a non-BlockStore directory
	 */
	public BlockStore(File root) throws IllegalArgumentException {
		this.root = root;
		if (! root.exists()) {
			root.mkdirs();
		}
		if (! root.isDirectory()) {
			logger.error("Not a directory " + root);
			throw new IllegalArgumentException("Not a directory: " + root);
		}
		checkMeta();
	}
	
	
	public String toString() {
		return "BlockStore " + root;
	}
	
	
	/**
	 * Reinitializes the block store, deletes all existing blocks.
	 * 
	 */
	public void clear() {
		logger.warn("Clearing block store " + this);
		try {
			FileUtils.deleteDirectory(root);
		} catch (IOException e) {
			logger.warn("Could not delete directory " + root, e);
		}
		checkMeta();
	}

	/**
	 * Default hash method for put(byte[]) is sha1
	 * 
	 * @param bytes Bytes to hash
	 * @return String Hash value in hex
	 */
	public String hash(byte[] bytes) {
		return DigestUtils.shaHex(bytes);
	}
	
	/**
	 * Check if given key exists in block store. 
	 * 
	 * @param key The identifier to check
	 * @return True if the key is retrievable by get(key)
	 */
	public boolean containsKey(String key) {
		return fileFor(key).exists();
	}

	/**
	 * Get the block data stored at given key
	 * 
	 * @param key Identifier of block
	 * @return A loaded byte[] array from the given block
	 */
	public byte[] get(String key) {
		File file = fileFor(key);
		if (! file.exists()) {
			return null;
		}
		try {
			return FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			logger.warn("Can't read " + file, e);
			return null;
		} 
	}
	
	/**
	 * Store block of bytes by using @link{hash(byte[])} as identifier.
	 * If the hash already exists as an identifier, the bytes are not
	 * rewritten to disk. Return the hash identifier.
	 * 
	 * @see hash(byte[]) 
	 * @param bytes Block to store
	 * @return Hash used as identifier.
	 */
	public String put(byte[] bytes) {
		String hash = hash(bytes);
		if (! containsKey(hash)) {
			// NOTE: Assumes hash exists
			put(hash, bytes);			
		}
		return hash;
	}

	/**
	 * Store block of bytes using given identifier. The block can later be
	 * retrieved using get(key). If a block with identifier exists, it will
	 * be overwritten. 
	 * 
	 * @param key Identifier to store bytes as.
	 * @param bytes Block to store
	 */
	public void put(String key, byte[] bytes) {
		try {
			FileUtils.writeByteArrayToFile(fileFor(key), bytes);
		} catch (IOException e) {
			logger.warn("Can't store " + key, e);
		}
	}

	/**
	 * Remove block stored under the given identifier.
	 * 
	 * @param key Identifier for block
	 */
	public void remove(String key) {
		fileFor(key).delete();
	}


	/**
	 * Find the file where the key is supposed to
	 * be stored. The parent directory will be created
	 * if needed, but not the file.
	 * 
	 * @param key
	 * @return Abstract file for given key
	 */
	File fileFor(String key) {
		return new File(subdirFor(key), key);
	}

	/**
	 * Find the directory where the file for given key
	 * would be stored. The directory is created if needed.
	 * 
	 * @param key
	 * @return Subdirectory for storing key
	 */
	File subdirFor(String key) {
		File subdir = new File(root, key.substring(0, SUBDIRLEN));
		subdir.mkdirs();
		return subdir;
	}


	/**
	 * Ensure that the meta file exists and is in the proper format.
	 * Create the file if it's missing and the root directory
	 * is empty.
	 * 
	 * @see META
	 * @throws IllegalArgumentException if root is a non-BlockStore directory
	 */
	private void checkMeta() throws IllegalArgumentException {
		File meta = new File(root, META);
		if (! meta.exists()) {
			if (root.list().length > 0) {
				throw new IllegalArgumentException("Not a perfs directory: " + root);
			}
			logger.info("Creating " + meta);
			try {
				FileUtils.writeStringToFile(meta, 
						META_HEADER+"\n", "utf8");
			} catch (IOException e) {
				logger.error("Could not write " + meta, e);
				throw new IllegalArgumentException("Could not write " + meta);
			}
		}
		String header;
		try {
			header = FileUtils.readFileToString(meta, "utf8");
		} catch (IOException e) {
			logger.error("Could not read " + meta, e);
			throw new IllegalArgumentException("Could not read " + meta);
		}
		if (! header.startsWith(META_HEADER + "\n")) {
			throw new IllegalArgumentException("Unknown header in meta file " + meta);
		}
	}

}
