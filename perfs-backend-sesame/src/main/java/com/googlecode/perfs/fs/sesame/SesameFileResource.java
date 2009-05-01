package com.googlecode.perfs.fs.sesame;

import java.io.InputStream;
import java.io.OutputStream;

import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.sesame.beans.File;

public class SesameFileResource extends FileResource implements
		ElmoBeanBased<File> {

	private final File elmoBean;

	public SesameFileResource(SesameFileSystem sesameFileSystem, File elmoBean) {
		super(sesameFileSystem, elmoBean.getQName().getLocalPart());
		this.elmoBean = elmoBean;
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public File getElmoBean() {
		return elmoBean;
	}

}
