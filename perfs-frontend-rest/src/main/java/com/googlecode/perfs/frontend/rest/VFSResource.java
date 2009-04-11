package com.googlecode.perfs.frontend.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.InputRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class VFSResource extends Resource {

	private FileSystemManager mgr;
	private FileObject cwd = null;
	private String realPath;

	public VFSResource(Context context, Request request, Response response) {
		super(context, request, response);
		try {
			mgr = VFS.getManager();
		} catch (FileSystemException e) {
			throw new RuntimeException("Can't initialise common-vfs", e);
		}

		String path = "/" + request.getResourceRef().getRemainingPart();
		try {
			cwd = mgr.resolveFile("perfs://" + path);
		} catch (FileSystemException e) {
			e.printStackTrace();
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
		try {
			if (cwd.exists()) {
				getVariants().add(new Variant(MediaType.TEXT_PLAIN));
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
			response.setStatus(Status.SERVER_ERROR_INTERNAL, e.getMessage());
		}
		realPath = cwd.getName().getPath();
		try {
			if (cwd.getType().equals(FileType.FOLDER)
					&& !realPath.endsWith("/")) {
				realPath += "/";
			}
			if (!realPath.equals(path)
					&& !cwd.getType().equals(FileType.IMAGINARY)) {
				response.redirectSeeOther(realPath);
			}
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean allowPut() {
		try {
			// Not allowed on folders
			return !(cwd.getType().equals(FileType.FOLDER));
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean allowPost() {
		try {
			// Only allowed on folders
			return ! cwd.getType().equals(FileType.FILE);
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			return false;
		}
	}

	@Override
	public void handlePost() {
		try {
			cwd.createFolder();
			getResponse().setStatus(Status.SUCCESS_CREATED);
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}

	@Override
	public void handlePut() {
		try {
			cwd.createFile();
			InputStream inStream = getRequest().getEntity().getStream();
			OutputStream outStream = cwd.getContent().getOutputStream();
			IOUtils.copy(inStream, outStream);
			outStream.close();
			inStream.close();
			getResponse().setStatus(Status.SUCCESS_CREATED);
		} catch (FileSystemException e) {
			e.printStackTrace();
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		} catch (IOException e) {
			e.printStackTrace();
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		}

	}

	@Override
	public void handleDelete() {
		try {
			cwd.delete();
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Representation getRepresentation(Variant variant) {
		try {
			if (cwd.getType().equals(FileType.FILE)) {
				FileContent content = cwd.getContent();
				InputRepresentation repr = new InputRepresentation(content
						.getInputStream(), MediaType.TEXT_PLAIN);
				repr.setSize(content.getSize());
				Date modificationDate = new Date(content.getLastModifiedTime());
				repr.setModificationDate(modificationDate);
				return repr;
			} else if (cwd.getType().equals(FileType.FOLDER)) {
				StringBuffer response = new StringBuffer();
				for (FileObject child : cwd.getChildren()) {
					String name = child.getName().getBaseName();
					if (child.getType().equals(FileType.FOLDER)) {
						name += "/";
					}
					response.append(name + "\r\n");
				}
				return new StringRepresentation(response,
						MediaType.TEXT_URI_LIST);
			} else {
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				return null;
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL,
					e.getMessage());
			return null;
		}
	}
}
