package com.googlecode.perfs.frontend.vfs;

import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.log4j.Logger;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.Resource;

public class VFSFileSystem extends AbstractFileSystem {
	private final class VFSResource extends AbstractFileObject {
		private final Resource resource;

		private VFSResource(FileName name, AbstractFileSystem fs,
				Resource resource) {
			super(name, fs);
			this.resource = resource;
		}

		@Override
		protected long doGetContentSize() throws Exception {
			if (resource instanceof FileResource) {
				return ((FileResource) resource).size();
			}
			return -1;
		}

		@Override
		protected InputStream doGetInputStream() throws Exception {
			if (resource instanceof FileResource) {
				return ((FileResource) resource).getInputStream();
			}
			return null;
		}

		@Override
		protected FileType doGetType() throws Exception {
			if (resource instanceof FileResource) {
				return FileType.FILE;
			} else if (resource instanceof DirectoryResource) {
				return FileType.FOLDER;
			}
			return FileType.IMAGINARY;
		}

		@Override
		protected String[] doListChildren() throws Exception {
			if (resource instanceof DirectoryResource) {
				return ((DirectoryResource) resource).resourceNames()
						.toArray(new String[0]);
			}
			return null;
		}
	}

	private final class VFSDirectory extends AbstractFileObject {
		private final DirectoryResource resource;

		private VFSDirectory(FileName name, AbstractFileSystem fs,
				DirectoryResource root) {
			super(name, fs);
			this.resource = root;
		}

		@Override
		protected long doGetContentSize() throws Exception {
			return 0;
		}

		@Override
		protected InputStream doGetInputStream() throws Exception {
			return null;
		}

		@Override
		protected FileType doGetType() throws Exception {
			return FileType.FOLDER;
		}

		@Override
		protected String[] doListChildren() throws Exception {
			return resource.resourceNames().toArray(new String[0]);
		}

		public DirectoryResource getAsResource() {
			return resource;
		}
	}

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VFSFileSystem.class);
	private com.googlecode.perfs.fs.FileSystem backend;

	public VFSFileSystem(com.googlecode.perfs.fs.FileSystem backend) {
		super(null, null, null);
		this.backend = backend;
	}

	@Override
	public FileObject getRoot() throws FileSystemException {
		DirectoryResource root = backend.getRoot();
		return new VFSDirectory(null, this, root);
	}

	@Override
	protected void addCapabilities(Collection capabilities) {
	}

	@Override
	protected FileObject createFile(FileName name) throws Exception {
		FileObject parent = VFSFileSystem.this.resolveFile(name.getParent());
		if (!(parent instanceof VFSDirectory)) {
			throw new IllegalStateException("Parent was not a VFSDirectory");
		}
		DirectoryResource parentRes = ((VFSDirectory) parent).getAsResource();

		final Resource resource;
		if (name.getType().equals(FileType.FILE)) {
			resource = backend.makeFile();
		} else if (name.getType().equals(FileType.FOLDER)) {
			resource = backend.makeDirectory();
		} else {
			throw new IllegalArgumentException("Not implemented support for "
					+ name.getType());
		}
		parentRes.put(name.getPath(), resource);
		AbstractFileObject file = new VFSResource(name, this, resource);
		return file;
	}

}
