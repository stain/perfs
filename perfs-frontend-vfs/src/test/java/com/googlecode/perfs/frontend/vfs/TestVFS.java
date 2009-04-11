package com.googlecode.perfs.frontend.vfs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.FileSystemFactory;
import com.googlecode.perfs.fs.memory.MemoryFileSystem;

public class TestVFS {

	private static final String FILE_CONTENT = "This is a test";
	private FileSystemManager mgr;
	private FileObject cwd;
	private FileSystem backend;
	

	@Before
	public void findRoot() throws Exception {
		mgr = VFS.getManager();
		cwd = mgr.resolveFile("perfs:///");
	}
	
	@Before
	public void createEntries() {
		backend = new MemoryFileSystem();
		FileSystemFactory.setFileSystem(backend);
		DirectoryResource root = backend.getRoot();
		DirectoryResource dir = backend.makeDirectory();
		root.put("dir1", dir);
		
		DirectoryResource dir2 = backend.makeDirectory();
		root.put("dir2", dir2);
		
		FileResource file1 = backend.makeFile();
		dir2.put("file1", file1);
		OutputStream stream = file1.getOutputStream();
		try {
			stream.write(FILE_CONTENT.getBytes("utf8"));
			stream.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void root() throws FileSystemException {
		assertEquals(FileType.FOLDER, cwd.getType());
	}
	
	@Test
	public void findDirs() throws Exception {
		FileObject[] children = cwd.getChildren();
		assertEquals(2, children.length);
		if (children[0].getName().getBaseName().equals("dir1")) {
			assertEquals("dir2", children[1].getName().getBaseName());
		} else {
			assertEquals("dir2", children[0].getName().getBaseName());
			assertEquals("dir1", children[1].getName().getBaseName());			
		}
	}
	
	@Test
	public void getChild() throws Exception {
		FileObject dir2 = cwd.getChild("dir2");
		assertEquals(FileType.FOLDER, dir2.getType());
		assertEquals(1, dir2.getChildren().length);
		FileObject file1 = dir2.getChild("file1");
		assertEquals(FileType.FILE, file1.getType());
		assertEquals(file1, cwd.resolveFile("dir2/file1"));
		FileContent content = file1.getContent();
		assertEquals(FILE_CONTENT, IOUtils.toString(content.getInputStream()));	
	}
	
	@Test
	public void unknown() throws FileSystemException {
		assertEquals(null, cwd.getChild("unknown"));
	}
	
}
