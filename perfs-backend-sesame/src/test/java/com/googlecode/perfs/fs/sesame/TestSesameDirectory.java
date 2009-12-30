package com.googlecode.perfs.fs.sesame;


import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.contextaware.ContextAwareConnection;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.OrganizedRDFWriter;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.rio.rdfxml.util.OrganizedRDFXMLWriter;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.TestDirectory;
import com.googlecode.perfs.fs.sesame.beans.Ontology;

public class TestSesameDirectory extends TestDirectory {

	@Override
	protected FileSystem makeFileSystem() {
		return new SesameFileSystem();
	}

	@After
	public void printOutStuff() throws RepositoryException, RDFHandlerException {
		SesameManager elmoManager = (SesameManager) ((SesameFileSystem)fs).getElmoManager();
		ContextAwareConnection connection = elmoManager.getConnection();
		connection.setNamespace("fs", Ontology.URI_BASE);
		connection.setNamespace("resources",  fs.getURIPrefix().toASCIIString());

//		elmoManager.getConnection().export(new OrganizedRDFXMLWriter(System.out));
		connection.export(new OrganizedRDFWriter(new N3Writer(System.out)));
		System.out.println("\n####\n\n");
	}

}
