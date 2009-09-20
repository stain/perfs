package com.googlecode.perfs.fs.sesame.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManagerFactory;

public class ElmoBeanTest {

	private static final String NAMESPACE = "urn:test:";
	private static final QName FOLDER = new QName(NAMESPACE, "folder");
	private static final QName ROOT = new QName(NAMESPACE, "root");
	private static final QName FS = new QName(NAMESPACE, "fs");
	private ElmoManager elmoManager;
	private FileSystem fs;
	private Folder root;

	@Before
	public void initialize() {
		createElmoManager();
		makeFileSystem();
		makeRootFolder();
	}
	


	protected void createElmoManager() {
		ElmoModule module = new ElmoModule();
		SesameManagerFactory factory = new SesameManagerFactory(module);
		factory.setInferencingEnabled(true);
		elmoManager = factory.createElmoManager();
	}

	protected void makeFileSystem() {
		fs = elmoManager.create(FS, FileSystem.class);
	}
	
	protected void makeRootFolder() {
		root = elmoManager.create(ROOT, Folder.class);
		fs.setRoot(root);
	}
	
	@Test
	public void findFS() throws Exception {
		FileSystem foundFS = (FileSystem) elmoManager.find(FS);
		assertNotSame(foundFS, fs);
		assertEquals(foundFS, fs);
		assertEquals(FS, foundFS.getQName());
	}

	@Test
	public void findRoot() throws Exception {
		FileSystem foundFS = (FileSystem) elmoManager.find(FS);
		assertNotSame(foundFS, fs);
		Folder foundRoot = foundFS.getRoot();
		assertEquals(ROOT, foundRoot.getQName());
		assertTrue(foundRoot.getFolderEntries().isEmpty());
	}

	@Test
	public void addSubFolderByEntry() throws Exception {
		Folder folder = elmoManager.create(FOLDER, Folder.class);
		
		FolderEntry folderEntry = elmoManager.create(FolderEntry.class);
		folderEntry.setFileName("folder1");
		folderEntry.setResource(folder);
		
		assertFalse(root.getFolderEntries().contains(folderEntry));
		
		folderEntry.setFolderEntryOf(root);
		
		assertEquals(folderEntry.getFolderEntryOf(), root);
		assertTrue("Root did not contain folder entry",
				root.getFolderEntries().contains(folderEntry));
		
		Folder foundRoot = (Folder) elmoManager.find(ROOT);
		foundRoot.getFolderEntries().isEmpty();
		assertTrue(foundRoot.getFolderEntries().contains(folderEntry));
	}

	
	@Test
	public void addSubFolderByEntrySet() throws Exception {
		Folder folder = elmoManager.create(FOLDER, Folder.class);
		
		FolderEntry folderEntry = elmoManager.create(FolderEntry.class);
		folderEntry.setFileName("folder2");
		folderEntry.setResource(folder);
		
		assertFalse(root.getFolderEntries().contains(folderEntry));
		
		root.getFolderEntries().add(folderEntry);
		
		assertEquals(folderEntry.getFolderEntryOf(), root);
		assertTrue(root.getFolderEntries().contains(folderEntry));
		
		Folder foundRoot = (Folder) elmoManager.find(ROOT);
		foundRoot.getFolderEntries().isEmpty();
		assertTrue(foundRoot.getFolderEntries().contains(folderEntry));
	}

	
	
}
