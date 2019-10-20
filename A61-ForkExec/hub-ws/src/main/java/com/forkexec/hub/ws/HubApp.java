package com.forkexec.hub.ws;


import com.forkexec.hub.domain.Hub;

/**
 * The application is where the service starts running. The program arguments
 * are processed here. Other configurations can also be done here.
 */
public class HubApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length == 0 || args.length == 2) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + HubApp.class.getName() + " wsURL OR uddiURL wsName wsURL");
			return;
		}

		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		String ccURL = null;

		// Create server implementation object, according to options
		HubEndpointManager endpoint = null;
		if (args.length == 1) {
			wsURL = args[0];
			endpoint = new HubEndpointManager(wsURL);

		} else if (args.length >= 3) {
			uddiURL = args[0];
			wsName = args[1];
			wsURL = args[2];
			ccURL = args[3];
			endpoint = new HubEndpointManager(uddiURL, wsName, wsURL, ccURL);
		}

		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}

	}

}