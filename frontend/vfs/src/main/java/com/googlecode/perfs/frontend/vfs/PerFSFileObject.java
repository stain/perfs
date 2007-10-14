package com.googlecode.perfs.frontend.vfs;

import java.io.InputStream;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileObject;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.Resource;

public class PerFSFileObject extends AbstractFileObject {

	private Resource perFSResource;

	protected PerFSFileObject(FileName name, PerFSFileSystem fs) {
		super(name, fs);
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
		if (! parent.getType().equals(FileType.FOLDER)) {
			throw new IllegalStateException("Parent was not a folder: " + parent);
		}
		DirectoryResource parentRes = (DirectoryResource) ((PerFSFileObject) parent).getPerFSResource();
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
			return ((DirectoryResource) perFSResource).resourceNames()
					.toArray(new String[0]);
		}
		return null;
	}

	public Resource getPerFSResource() {
		return perFSResource;
	}

}
