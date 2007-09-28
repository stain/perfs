package com.googlecode.perfs.fs;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileResource {

	public abstract InputStream getInputStream();

	public abstract OutputStream getOutputStream();

	public abstract long size();

}