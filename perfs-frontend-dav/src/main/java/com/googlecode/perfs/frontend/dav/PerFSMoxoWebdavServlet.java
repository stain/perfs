package com.googlecode.perfs.frontend.dav;

import org.apache.commons.vfs.FileSystemException;

import com.thinkberg.moxo.dav.ResourceManager;
import com.thinkberg.moxo.servlet.MoxoWebdavServlet;

public class PerFSMoxoWebdavServlet extends MoxoWebdavServlet {

	@Override
	public ResourceManager getResourceManager() {
		try {
			return new PerFSResourceManager();
		} catch (FileSystemException e) {
			throw new RuntimeException("Can't initialise PerFSResourceManager", e);
		}
	}

}
