package com.googlecode.perfs.fs;

public class AlreadyExistsException extends IllegalStateException {

	private final String filename;

	public AlreadyExistsException(String filename) {
		super("Filename already exists: " + filename);
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

}
