package com.googlecode.perfs.fs.rdf.openanzo;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.openanzo.client.AnzoClient;
import org.openanzo.client.ClientGraph;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.rdf.Constants;
import org.openanzo.rdf.Statement;
import org.openanzo.rdf.URI;

import com.googlecode.perfs.fs.FileSystem;
import com.googlecode.perfs.fs.Resource;

public class AnzoFileSystem extends FileSystem {

	private static Logger logger = Logger.getLogger(AnzoFileSystem.class);

	private final File anzoDir;
	private AnzoClient datasetService;
	private DatasetServiceFactory datasetServiceFactory = DatasetServiceFactory
			.getInstance();

	public AnzoFileSystem(File anzoDir) throws IOException, AnzoException {
		super();
		this.anzoDir = anzoDir;
		setDatasetService();
	}

	public AnzoFileSystem(File anzoDir, String uuid) throws IOException,
			AnzoException {
		super(uuid);
		this.anzoDir = anzoDir;
		setDatasetService();
	}

	public synchronized AnzoClient getDatasetService() {
		return datasetService;
	}

	public AnzoDirectoryResource getResource(URI rootURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getResource(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	public URI getResourceURI() {
		return Constants.valueFactory.createURI(getURI().toASCIIString());
	}

	@Override
	public AnzoDirectoryResource makeDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnzoFileResource makeFile() {
		// TODO Auto-generated method stub
		return null;
	}

	private URI getHasRootDir() {
		return Constants.valueFactory.createURI(getURI().resolve("hasRootDir")
				.toASCIIString());
	}

	private void setDatasetService() throws IOException, AnzoException {
		datasetService = datasetServiceFactory
				.getEmbeddedDatasetService(anzoDir);
	}

	protected URI createRoot() {
		AnzoDirectoryResource rootDir = makeDirectory();
		setRoot(rootDir);
		return rootDir.getResourceURI();
	}

	@Override
	protected AnzoDirectoryResource findRoot() {
		AnzoClient dataset = getDatasetService();
		URI namedGraphURI = getResourceURI();

		ClientGraph graph;
		try {
			graph = dataset.getReplicaGraph(namedGraphURI);
		} catch (AnzoException e) {
			throw new RuntimeException("Can't find root", e);
		}
		URI rootURI = null;
		for (Statement statement : graph.find(namedGraphURI, getHasRootDir(),
				null)) {
			rootURI = (URI) statement.getObject();
			break;
		}
		if (rootURI == null) {
			rootURI = createRoot();
		}
		// FIXME: Too many different URIs
		return getResource(rootURI);
	}

	@Override
	protected void registerResource(Resource resource) {
		// TODO Auto-generated method stub
	}

	protected void setRoot(AnzoDirectoryResource rootDir) {
		AnzoClient dataset = getDatasetService();
		URI namedGraphURI = getResourceURI();
		try {
			dataset.begin();
			ClientGraph graph = dataset.getReplicaGraph(namedGraphURI);
			graph.add(namedGraphURI, getHasRootDir(), rootDir.getResourceURI());
			dataset.commit();
		} catch (AnzoException e) {
			logger.error("Could not set root of " + this + " to " + rootDir, e);

		}
	}

}
