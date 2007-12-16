package com.googlecode.perfs.frontend.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.Protocol;

public class PerFSRESTApplication extends Application {

	private static Logger logger = Logger.getLogger(PerFSRESTApplication.class);

	private Component component = null;

	private String host;

	private int port;

	private String namespace;

	public PerFSRESTApplication(Context context) {
		super(context);
		init();
	}

	public PerFSRESTApplication(String host, int port) {
		super();
		this.host = host;
		this.port = port;
		init();

	}

	/**
	 * Do some pre-start initialisation.
	 * 
	 * This includes
	 * <ul>
	 * <li>Creating a default worker, so there is always at least 1</li>
	 * </ul>
	 * 
	 * @throws IOException
	 */
	private void init() {
		initializeRestletLogging();
		createExampleFiles();
	}

	private void createExampleFiles() {
//		new TestVFS().createEntries();
	}
	
	private void initializeRestletLogging() {

		Handler[] handlers = java.util.logging.Logger.getLogger("")
				.getHandlers();
		for (Handler handler : handlers) {
			handler.setFormatter(new ReallySimpleFormatter());
		}
		java.util.logging.Logger.getLogger("org.mortbay.log").setLevel(
				Level.WARNING);

	}

	@Override
	public Restlet createRoot() {
		return createComponent();
	}

	public void startServer() {
		stopServer();
		if (component == null) {
			component = createComponent();
		}
		component.getServers().add(Protocol.HTTP, port);

		// Now, let's start the component!
		// Note that the HTTP server connector is also automatically started.
		try {
			component.start();
		} catch (Exception ex) {
			logger.error("Error starting the server", ex);
		}
			}

	public void stopServer() {
		if (component != null && component.isStarted()) {
			try {
				component.stop();
			} catch (Exception e) {
				logger.warn("Could not stop server " + component, e);
			}
		}
		component = null;
	}

	protected void attachDataManager(Component component) {
		component.getDefaultHost().attach("/", VFSResource.class);
	}

	public Component createComponent() {
		Component component = new Component();
		attachDataManager(component);
		return component;
	}

}

class ReallySimpleFormatter extends java.util.logging.Formatter {
	@Override
	public String format(java.util.logging.LogRecord record) {
		String msg = record.getMessage() + "\n";
		StringWriter sw = new StringWriter();
		if (record.getThrown() != null) {
			record.getThrown().printStackTrace(new PrintWriter(sw));
		}
		return msg + sw;
	}
}