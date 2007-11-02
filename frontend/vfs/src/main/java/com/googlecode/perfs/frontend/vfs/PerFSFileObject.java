package com.googlecode.perfs.frontend.vfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.apache.log4j.Logger;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.Resource;

public class PerFSFileObject extends AbstractFileObject {

	private Resource perFSResource;

	private DirectoryResource parentRes;

	private long lastModTime;

	private static Logger logger = Logger.getLogger(PerFSFileObject.class);

	protected PerFSFileObject(FileName name, PerFSFileSystem fs) {
		super(name, fs);
		// fake lastModTime
		Date now = new Date();
		lastModTime = now.getTime();
	}

	private FileSystem getBackend() {
		return getFileSystem().getBackend();
	}

	@Override
	public PerFSFileSystem getFileSystem() {
		return (PerFSFileSystem) super.getFileSystem();
	}

	@Override
	protected void doAttach() throws Exception {
		FileName parentName = getName().getParent();
		if (parentName == null) {
			perFSResource = getBackend().getRoot();
			return;
		}
		FileObject parent = getFileSystem().resolveFile(parentName);
		if (!parent.getType().equals(FileType.FOLDER)) {
			throw new IllegalStateException("Parent was not a folder: "
					+ parent);
		}
		parentRes = (DirectoryResource) ((PerFSFileObject) parent)
				.getPerFSResource();
		perFSResource = parentRes.get(getName().getBaseName());
	}

	@Override
	protected long doGetContentSize() throws Exception {
		if (perFSResource instanceof FileResource) {
			return ((FileResource) perFSResource).size();
		}
		return -1;
	}

	@Override
	protected InputStream doGetInputStream() throws Exception {
		if (perFSResource instanceof FileResource) {
			return ((FileResource) perFSResource).getInputStream();
		}
		return null;
	}

	@Override
	protected void doCreateFolder() throws Exception {
		if (perFSResource == null && parentRes != null) {
			// Create a new directory
			perFSResource = parentRes.getFileSystem().makeDirectory();
			parentRes.put(getName().getBaseName(), perFSResource);
		}
		throw new IllegalStateException("Can't create folder " + perFSResource);
	}

	@Override
	protected OutputStream doGetOutputStream(boolean append) throws Exception {
		if (append) {
			throw new UnsupportedOperationException("Don't support append yet");
		}
		if (perFSResource == null && parentRes != null) {
			// Create a new file
			perFSResource = parentRes.getFileSystem().makeFile();
			parentRes.put(getName().getBaseName(), perFSResource);
		}
		if (perFSResource instanceof FileResource) {
			return ((FileResource) perFSResource).getOutputStream();
		}
		logger.warn("Attempt to doGetOutputStream on " + perFSResource);
		return null;
	}

	@Override
	protected void doSetLastModifiedTime(long modtime) throws Exception {
		this.lastModTime = modtime;
	}

	@Override
	protected FileType doGetType() throws Exception {
		if (perFSResource instanceof FileResource) {
			return FileType.FILE;
		} else if (perFSResource instanceof DirectoryResource) {
			return FileType.FOLDER;
		}
		return FileType.IMAGINARY;
	}

	@Override
	protected String[] doListChildren() throws Exception {
		if (perFSResource instanceof DirectoryResource) {
			return ((DirectoryResource) perFSResource).resourceNames().toArray(
					new String[0]);
		}
		return null;
	}

	public Resource getPerFSResource() {
		return perFSResource;
	}

	@Override
	protected long doGetLastModifiedTime() throws Exception {
		return lastModTime;
	}

}
