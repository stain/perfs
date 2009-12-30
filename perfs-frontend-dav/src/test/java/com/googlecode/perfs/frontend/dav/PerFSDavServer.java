package com.googlecode.perfs.frontend.dav;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;

public class PerFSDavServer {

	public static void main(String[] args) throws Exception {
		
		Server server = new Server();
		SelectChannelConnector selectChannelConnector = new SelectChannelConnector();
		selectChannelConnector.setPort(8081);
		selectChannelConnector.setHost("0.0.0.0");
		selectChannelConnector.setMaxIdleTime(30000);
		selectChannelConnector.setAcceptors(2);
		selectChannelConnector.setConfidentialPort(8443);
		server.addConnector(selectChannelConnector);
		
		Context context = new Context();
		context.setContextPath("/");
		context.setResourceBase(".");
		context.addServlet(PerFSMoxoWebdavServlet.class, "/*");
		server.setHandler(context);
		server.start();
		server.join();
	}

}
