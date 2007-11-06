package com.googlecode.perfs.fs;

public class AlreadyExistsException extends IllegalStateException {

	private static final long serialVersionUID = -6188441877588121147L;
	private final String filename;

	public AlreadyExistsException(String filename) {
		super("Filename already exists: " + filename);
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

}
