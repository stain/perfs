package com.googlecode.perfs.frontend.rest;

import java.util.Date;

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

	public VFSResource(Context context, Request request, Response response) {
		super(context, request, response);
		try {
			mgr = VFS.getManager();
		} catch (FileSystemException e) {
			throw new RuntimeException("Can't initialise common-vfs", e);
		}

		String path = request.getResourceRef().getRemainingPart();
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
					response.append(child.getName().getBaseName() + "\r\n");
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
