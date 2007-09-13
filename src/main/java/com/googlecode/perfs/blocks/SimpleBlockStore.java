package com.googlecode.perfs.blocks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

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
public class SimpleBlockStore implements BlockStore {

	private static Logger logger = Logger.getLogger(SimpleBlockStore.class);
	

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
	 * Construct a SimpleBlockStore for the given directory. If the directory 
	 * does not yet exist or is empty, it will be initialized as a new
	 * empty SimpleBlockStore. If the directory is an existing SimpleBlockStore
	 * this instance will use the existing entries. If the directory
	 * is not a SimpleBlockStore directory or can't be read/written properly, 
	 * an IllegalArgumentException will be thrown.
	 * 
	 * @param root Base directory for storing blocks
	 * @throws IllegalArgumentException if root is a non-SimpleBlockStore directory
	 */
	public SimpleBlockStore(File root) throws IllegalArgumentException {
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
	
	
	@Override
	public String toString() {
		return "SimpleBlockStore " + root;
	}
	
	
	/* (non-Javadoc)
	 * @see com.googlecode.perfs.BlockStore#clear()
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

	/* (non-Javadoc)
	 * @see com.googlecode.perfs.BlockStore#hash(byte[])
	 */
	public String hash(byte[] bytes) {
		return DigestUtils.shaHex(bytes);
	}
	
	/* (non-Javadoc)
	 * @see com.googlecode.perfs.BlockStore#containsKey(java.lang.String)
	 */
	public boolean containsKey(String key) {
		return fileFor(key).exists();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.perfs.BlockStore#get(java.lang.String)
	 */
	public InputStream get(String key) {
		File file = fileFor(key);
		if (! file.exists()) {
			return null;
		}
		try {
			return file.toURI().toURL().openStream();
		} catch (MalformedURLException ex) {
			// TODO Auto-generated catch block
			logger.warn("An error occured", ex);
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			logger.warn("An error occured", ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.googlecode.perfs.BlockStore#put(byte[])
	 */
	public String put(byte[] bytes) {
		String hash = hash(bytes);
		if (! containsKey(hash)) {
			// NOTE: Assumes hash exists
			put(hash, bytes);			
		}
		return hash;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.perfs.BlockStore#put(java.lang.String, byte[])
	 */
	public void put(String key, byte[] bytes) {
		try {
			FileUtils.writeByteArrayToFile(fileFor(key), bytes);
		} catch (IOException e) {
			logger.warn("Can't store " + key, e);
		}
	}

	/* (non-Javadoc)
	 * @see com.googlecode.perfs.BlockStore#remove(java.lang.String)
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
	 * @throws IllegalArgumentException if root is a non-SimpleBlockStore directory
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


	public byte[] getBytes(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
