package com.googlecode.perfs.frontend.vfs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractOriginatingFileProvider;

public class PerFSFileProvider extends AbstractOriginatingFileProvider {

	protected final static Collection<Capability> capabilities = Collections
			.unmodifiableCollection(Arrays.asList(new Capability[] {
					Capability.CREATE, Capability.DELETE, Capability.RENAME,
					Capability.GET_TYPE, Capability.LIST_CHILDREN,
					Capability.READ_CONTENT, Capability.URI,
					Capability.WRITE_CONTENT, Capability.GET_LAST_MODIFIED,
					Capability.ATTRIBUTES, Capability.RANDOM_ACCESS_READ }));

	@Override
	protected FileSystem doCreateFileSystem(FileName arg0,
			FileSystemOptions arg1) throws FileSystemException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Capability> getCapabilities() {
		return capabilities;
	}

}
