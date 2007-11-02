package com.googlecode.perfs.frontend.vfs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractOriginatingFileProvider;
import org.apache.log4j.Logger;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.FileSystemFactory;

public class PerFSFileProvider extends AbstractOriginatingFileProvider {

	private static Logger logger = Logger.getLogger(PerFSFileProvider.class);
	
	protected final static Collection<Capability> capabilities = Collections
			.unmodifiableCollection(Arrays.asList(new Capability[] {
					Capability.CREATE, Capability.DELETE, Capability.RENAME,
					Capability.GET_TYPE, Capability.LIST_CHILDREN,
					Capability.READ_CONTENT, Capability.URI,
					Capability.WRITE_CONTENT, Capability.GET_LAST_MODIFIED,
					Capability.ATTRIBUTES, Capability.RANDOM_ACCESS_READ }));

	@Override
	protected PerFSFileSystem doCreateFileSystem(final FileName rootName,
			final FileSystemOptions fileSystemOptions) {
		logger.debug("Root: " + rootName);
		logger.debug("Options:" + fileSystemOptions);
		FileSystem backend = FileSystemFactory.getFileSystem();	
		return new PerFSFileSystem(rootName, null, fileSystemOptions, backend);
	}

	public Collection<Capability> getCapabilities() {
		return capabilities;
	}

}
