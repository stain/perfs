package com.googlecode.perfs.fs.sesame;


import org.junit.After;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.OrganizedRDFXMLWriter;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.AbstractTestFilesystem;

public class TestSesameFileSystem extends AbstractTestFilesystem {

	@Override
	protected FileSystem makeFileSystem() {
		return new SesameFileSystem();
	}
	

}
