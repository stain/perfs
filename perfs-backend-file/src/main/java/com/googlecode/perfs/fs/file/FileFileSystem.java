package com.googlecode.perfs.fs.file;

import java.io.File;
import java.util.UUID;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.Resource;

public class FileFileSystem extends FileSystem {

	private final File baseDir;

	public FileFileSystem(File baseDir) {
		this.baseDir = baseDir;
		baseDir.mkdirs();
	}
	
	@Override
	protected DirectoryResource findRoot() {
		return new FileDirectoryResource(this);
	}

	@Override
	public Resource getResource(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryResource makeDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileResource makeFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void registerResource(Resource resource) {
		// TODO Auto-generated method stub

	}

	public File getBaseDir() {
		return baseDir;
	}

	public File fileByUUID(UUID uuid) {
		String uuidStr = uuid.toString();
		String prefix = uuidStr.substring(0, 2);
		File prefixDir = new File(getBaseDir(), prefix);
		prefixDir.mkdir();
		return new File(prefixDir, uuidStr);
	}

}
