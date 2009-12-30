package com.googlecode.perfs.fs.sesame;

import java.io.InputStream;
import java.io.OutputStream;

import com.googlecode.perfs.fs.FileResource;
import com.googlecode.perfs.fs.sesame.BlockBasedOutputStream.BlockBasedOutputStreamCallback;
import com.googlecode.perfs.fs.sesame.beans.Blocks;
import com.googlecode.perfs.fs.sesame.beans.File;

public class SesameFileResource extends FileResource implements
		ElmoBeanBased<File>, BlockBasedOutputStreamCallback {

	@Override
	public SesameFileSystem getFileSystem() {
		return (SesameFileSystem)super.getFileSystem();
	}
	
	private final File elmoBean;

	public SesameFileResource(SesameFileSystem sesameFileSystem, File elmoBean) {
		super(sesameFileSystem, elmoBean.getQName().getLocalPart());
		this.elmoBean = elmoBean;
	}

	@Override
	public InputStream getInputStream() {
		return new BlockBasedInputStream(getElmoBean().getBlocks());
	}

	@Override
	public OutputStream getOutputStream() {
		return new BlockBasedOutputStream(getFileSystem(), getElmoBean().getBlocks().getBlockList(), this);
	}

	@Override
	public long size() {
		return getElmoBean().getBlocks().getSize();
	}

	public File getElmoBean() {
		return elmoBean;
	}

	public void closedFile(Blocks blocks) {
		getElmoBean().setBlocks(blocks);
	}

}
