package com.googlecode.perfs.fs.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import com.googlecode.perfs.fs.AlreadyExistsException;
import com.googlecode.perfs.fs.DirectoryResource;
import com.googlecode.perfs.fs.Resource;

public class FileDirectoryResource extends DirectoryResource {

	private Map<String, UUID> entries = null;

	protected FileDirectoryResource(FileFileSystem fileSystem, String uuid) {
		super(fileSystem, uuid);
	}

	protected FileDirectoryResource(FileFileSystem fileSystem, UUID uuid) {
		super(fileSystem, uuid);
	}

	protected FileDirectoryResource(FileFileSystem fileSystem) {
		super(fileSystem);
	}

	@Override
	public void clear() {
		entries = new HashMap<String, UUID>();
		saveEntries();
	}

	@Override
	public boolean containsFilename(String filename) {
		loadEntries();
		return entries.containsKey(filename);
	}

	private synchronized void loadEntries() {
		@SuppressWarnings("unused")
		File directoryFile = getBlockFile();
		// TODO: What else?
	}

	@Override
	public boolean containsResource(Resource resource) {
		return entries.containsValue(resource.getUUID());
	}

	@Override
	public Resource get(String filename) {
		loadEntries();
		UUID entry = entries.get(filename);
		return getFileSystem().getResource(entry);
	}

	@Override
	public DirectoryResource getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized void put(String filename, Resource resource)
			throws AlreadyExistsException {
		loadEntries();
		putEntry(filename, resource);
		saveEntries();
	}

	protected void putEntry(String filename, Resource resource) {
		if (entries.containsKey(filename)) {
			throw new AlreadyExistsException(filename);
		}
		if (filename.contains("\n")) {
			throw new IllegalArgumentException("Invalid filename");
		}
		entries.put(filename, resource.getUUID());
	}

	private synchronized void saveEntries() {
		File directoryFile = getBlockFile();
		FileOutputStream outStream;
		File tmpFile = null;
		File renameFile = null;
		try {
			tmpFile = File.createTempFile(directoryFile.getName(), ".tmp",
					directoryFile.getParentFile());
			outStream = new FileOutputStream(tmpFile);
			BufferedWriter outWriter = new BufferedWriter(
					new OutputStreamWriter(outStream, "utf-8"));
			for (Entry<String, UUID> entry : entries.entrySet()) {
				outWriter.write(entry.getKey().toString());
				outWriter.write(' ');
				outWriter.write(entry.getKey());
				outWriter.write('\n');
			}
			outWriter.close();

			if (tmpFile.renameTo(directoryFile)) {
				return;
			} else {
				renameFile = new File(directoryFile.getParentFile(),
						directoryFile.getName() + ".old");
				renameFile.delete();
				directoryFile.renameTo(renameFile);
				tmpFile.renameTo(directoryFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public synchronized void putAll(Map<String, ? extends Resource> t)
			throws AlreadyExistsException {
		loadEntries();
		for (Entry<String, ? extends Resource> entry : t.entrySet()) {
			String filename = entry.getKey();
			Resource resource = entry.getValue();
			putEntry(filename, resource);
		}
		saveEntries();
	}

	@Override
	public Resource remove(String filename) {
		loadEntries();
		UUID existingEntry = entries.remove(filename);
		if (existingEntry == null) {
			// Did not exist, no need to save
			return null;
		}
		saveEntries();
		return getFileSystem().getResource(existingEntry);
	}

	@Override
	public Collection<String> resourceNames() {
		loadEntries();
		return entries.keySet();
	}

	@Override
	public Collection<Resource> resources() {
		loadEntries();
		ArrayList<Resource> resources = new ArrayList<Resource>();
		for (UUID uuid : entries.values()) {
			resources.add(getFileSystem().getResource(uuid));
		}
		return resources;
	}

	@Override
	protected void setParent(DirectoryResource parent) {
		// TODO Auto-generated method stub
	}

	protected File getBlockFile() {
		return getFileSystem().fileByUUID(getUUID());
	}

	@Override
	public FileFileSystem getFileSystem() {
		return (FileFileSystem) super.getFileSystem();
	}
	
	@Override
	public int size() {
		return 0;
	}

}
