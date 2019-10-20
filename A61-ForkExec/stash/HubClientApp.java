package com.forkexec.hub.ws.cli;

import java.util.List;

import com.forkexec.hub.ws.*;

/**
 * Client application. 
 * 
 * Looks for Hub using UDDI and arguments provided
 */
public class HubClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + HubClientApp.class.getName() + " wsURL OR uddiURL wsName");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		} else if (args.length >= 2) {
			uddiURL = args[0];
			wsName = args[1];
		}

		// Create client.
		HubClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new HubClient(wsURL);
		} else if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new HubClient(uddiURL, wsName);
		}

		// The following remote invocation is just a basic example.
		// The actual tests are made using JUnit.
		/*System.out.println("Activating account()...");
		client.activateAccount("daniela@lopes.com");*/
		System.out.println("Account balance()...");
		int i = client.accountBalance("daniela@lopes.com");
		System.out.println("balance: " + i);
		/*System.out.println("Clearing cart()...");
		client.clearCart("daniela@lopes.com");*/
		/*System.out.print("Result: ");
		System.out.println(result);*/
	}

}
