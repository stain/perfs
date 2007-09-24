package com.googlecode.perfs.fs;

import java.net.URI;


public class FileResource extends Resource {

	public FileResource(FileSystem filesystem, String uuid) {
		super(filesystem, uuid);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected URI getURIClass() {
		return URI.create("file/");
	}

}
