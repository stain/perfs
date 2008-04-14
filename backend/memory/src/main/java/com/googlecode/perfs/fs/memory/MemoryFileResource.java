package com.googlecode.perfs.fs.memory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.googlecode.perfs.fs.FileResource;

public class MemoryFileResource extends FileResource {

	private byte[] data = new byte[0];

	public MemoryFileResource(MemoryFileSystem fileSystem) {
		super(fileSystem);
	}
	
	public MemoryFileResource(MemoryFileSystem fileSystem, UUID uuid) {
		super(fileSystem, uuid);
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(data);
	}

	@Override
	public OutputStream getOutputStream() {
		ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
		return new CopyOnCloseOutputStream(os);
	}

	@Override
	public long size() {
		return data.length;
	}



	class CopyOnCloseOutputStream extends FilterOutputStream {
		private final ByteArrayOutputStream os;

		CopyOnCloseOutputStream(ByteArrayOutputStream os) {
			super(os);
			this.os = os;
		}

		@Override
		public void close() throws IOException {
			super.close();
			data = os.toByteArray();
		}
	}

}
