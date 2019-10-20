package com.forkexec.hub.ws.cli;

import com.forkexec.hub.ws.FoodId;

import java.util.Scanner;

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

		/*System.out.println("Invoke ping()...");
		String result = client.ctrlPing("client");*/
		int res;
		int res2;
		CreateFoods foods = new CreateFoods();
		client.ctrlInitFood(foods.getFoodInit());

		while (true) {
			Scanner UsrIn = new Scanner(System.in);
			String input = UsrIn.nextLine();
			switch (input) {
				case "1":
					//Utilizador 1
					System.out.println("Activate taissa@gmail.com Account");
					client.activateAccount("taissa@gmail.com");
					System.out.println("Verify taissa@gmail.com Account Balance");
					res = client.accountBalance("taissa@gmail.com");
					System.out.println("taissa@gmail.com Account Balance: " + res);
					System.out.println();

					//Utilizador 2
					System.out.println("Activate dani@gmail.com Account");
					client.activateAccount("dani@gmail.com");
					System.out.println("Verify dani@gmail.com Account Balance");
					res = client.accountBalance("dani@gmail.com");
					System.out.println("dani@gmail.com Account Balance: " + res);
					System.out.println();

					//Utilizador 3
					System.out.println("Activate joana@gmail.com Account");
					client.activateAccount("joana@gmail.com");
					System.out.println("Verify joana@gmail.com Account Balance");
					res = client.accountBalance("joana@gmail.com");
					System.out.println("joana@gmail.com Account Balance: " + res);
					System.out.println();

					//Utilizador 4
					System.out.println("Activate lolita@gmail.com Account");
					client.activateAccount("lolita@gmail.com");
					System.out.println("Verify lolita@gmail.com Account Balance");
					res = client.accountBalance("lolita@gmail.com");
					System.out.println("lolita@gmail.com Account Balance: " + res);
					System.out.println();

					//Utilizador 5
					System.out.println("Activate jeremias@gmail.com Account");
					client.activateAccount("jeremias@gmail.com");
					System.out.println("Verify jeremias@gmail.com Account Balance");
					res = client.accountBalance("jeremias@gmail.com");
					System.out.println("jeremias@gmail.com Account Balance: " + res);
					System.out.println();

					break;
				case "2":
					System.out.println("Load taissa@gmail.com Account with 1000 Points");
					client.loadAccount("taissa@gmail.com", 10, "4024007102923926");
					System.out.println("Verify taissa@gmail.com Account Balance");
					res2 = client.accountBalance("taissa@gmail.com");
					System.out.println("taissa@gmail.com Account Balance: " + res2);
					System.out.println();
					break;
				case "3":
					//Utilizador 6
					System.out.println("Activate bambi@gmail.com Account");
					client.activateAccount("bambi@gmail.com");
					System.out.println("Verify bambi@gmail.com Account Balance");
					res = client.accountBalance("bambi@gmail.com");
					System.out.println("bambi@gmail.com Account Balance: " + res);
					System.out.println();
					break;
				case "4":
					System.out.println("Load dani@gmail.com Account with 1000 Points");
					client.loadAccount("dani@gmail.com", 10, "4024007102923926");
					System.out.println("Verify dani@gmail.com Account Balance");
					res2 = client.accountBalance("dani@gmail.com");
					System.out.println("dani@gmail.com Account Balance: " + res2);
					System.out.println();
					break;
				case "5":
					FoodId foodId = new FoodId();
					foodId.setRestaurantId("A61_Restaurant1");
					foodId.setMenuId("Menu3");
					System.out.println("Add a Menu to Order Cart of taissa@gmail.com User");
					client.addFoodToCart("taissa@gmail.com", foodId, 2);
					System.out.println("Buy Content of Order Cart of taissa@gmail.com User");
					client.orderCart("taissa@gmail.com");
					System.out.println("Check taissa@gmail.com Account Balance");
					res = client.accountBalance("taissa@gmail.com");
					System.out.println("taissa@gmail.com Account Balance: " + res);
					System.out.println();
					break;
				case "6":
					System.out.println("Server is going to Die");
					String msg = client.ctrlPing("die");
					System.out.println(msg);
					System.out.println();
					break;

				case "7":
					System.out.println("Server is going to Sleep");
					msg = client.ctrlPing("slow");
					System.out.println(msg);
					System.out.println();
					break;
				case "8":
					System.out.println("Server is going to say Hello");
					msg = client.ctrlPing("client");
					System.out.println(msg);
					System.out.println();
					break;
				case "9":
					//exception
					break;
				default:
					System.out.println("Invalid Key");
					System.out.println();
					break;
			}
		}
	}

}
