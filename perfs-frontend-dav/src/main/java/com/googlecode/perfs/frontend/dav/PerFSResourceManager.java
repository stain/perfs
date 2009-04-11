package com.googlecode.perfs.frontend.dav;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.FileSystemFactory;
import com.googlecode.perfs.fs.memory.MemoryFileSystem;
import com.thinkberg.moxo.dav.ResourceManager;

public class PerFSResourceManager extends ResourceManager {

	private MemoryFileSystem backend;
	private FileSystemManager mgr;
	private FileObject root;

	public PerFSResourceManager() throws FileSystemException {
		backend = new MemoryFileSystem();
		FileSystemFactory.setFileSystem(backend);
		mgr = VFS.getManager();
		root = mgr.resolveFile("perfs:///");
		
		DirectoryResource root = backend.getRoot();
		DirectoryResource dir = backend.makeDirectory();
		root.put("dir1", dir);
		
		DirectoryResource dir2 = backend.makeDirectory();
		root.put("theotherone", dir2);
		
		FileResource file1 = backend.makeFile();
		dir2.put("file.txt", file1);
		OutputStream stream = file1.getOutputStream();
		try {
			stream.write("Hello there!\n".getBytes("utf8"));
			stream.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public FileObject getFileObject(String path) throws FileSystemException {
		return root.resolveFile(path);
	}

}
