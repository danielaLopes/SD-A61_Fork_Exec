package com.forkexec.hub.domain;

import java.io.IOException;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.hub.domain.exceptions.*;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.pts.ws.*;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.pts.ws.cli.PointsFrontEnd;
import com.forkexec.pts.ws.cli.PointsFrontEndException;
import com.forkexec.rst.ws.*;
import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;
import com.forkexec.cc.ws.cli.CCClientException;
import com.forkexec.cc.ws.cli.CCClient;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

/**
 * Hub
 *
 * A restaurants hub server.
 *
 */
public class Hub {

	private UDDINaming uddiNaming;
	private String ccUrl;

	private static final List<Integer> MONEY_VALUES = Arrays.asList(10, 20, 30, 50);
	private static final List<Integer> POINT_VALUES = Arrays.asList(1000, 2100, 3300, 5500);

	private Map<String, List<FoodOrderItemHub>> cart = new ConcurrentHashMap<>();

	private Comparator<FoodHub> compareDeal = new FoodDealComparator();
	private Comparator<FoodHub> compareHungry = new FoodHungryComparator();

	private AtomicInteger foodOrderId = new AtomicInteger(0);

	private PointsFrontEnd frontEnd;

	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Hub() {}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Hub INSTANCE = new Hub();
	}

	public static synchronized Hub getInstance() {
		return SingletonHolder.INSTANCE;
	}

	// UDDIname is set in HubEndpointManager after publishing to UDDI
	public synchronized void setUddiNaming(UDDINaming name) { uddiNaming = name; }

	public synchronized UDDINaming getUddiName() { return uddiNaming; }

	// ccURL is set in HubEndpointManager after publishing to UDDI
	public synchronized void setCcUrl(String url) { ccUrl = url; }


	// frontEnd is created in HubEndpointManager after publishing to UDDI
	public synchronized void createPointsFrontEnd() {
		try {
			frontEnd = new PointsFrontEnd(uddiNaming);
		} catch(PointsFrontEndException e) {
			System.err.println("Error connecting to Points" + e);
			throw new RuntimeException(e);
		}

	}

	// Auxiliary operations --------------------------------------------------
	public synchronized void checkUserIdValidity(String userId) throws InvalidUserId_Exception {
		if (cart.containsKey(userId)) throw new InvalidUserId_Exception("Invalid user id: user already exists");
		else if (!userId.matches("^(([A-Za-z0-9]+\\.)*[A-Za-z0-9]+)@(([A-Za-z0-9]+\\.)*[A-Za-z0-9]+)$")) throw new InvalidUserId_Exception("Invalid user id: not a valid email");
	}

	public synchronized void checkUserIdExistence(String userId) throws InvalidUserId_Exception {
		if (!cart.containsKey(userId)) throw new InvalidUserId_Exception("Invalid user id: user does not exist");
	}

	public synchronized void checkDescriptionValidity(String description) throws InvalidText_Exception {
		if (description.isEmpty() ||  description.contains("\\s+")) throw new InvalidText_Exception("Invalid Description");
	}

	public synchronized void checkFoodQuantityValidity(int quantity) throws InvalidFoodQuantity_Exception {
		if (quantity <= 0) throw new InvalidFoodQuantity_Exception("Invalid food quantity");
	}

	public synchronized int moneyToPoints(int moneyToAdd) throws InvalidMoney_Exception {
		if (!MONEY_VALUES.contains(moneyToAdd)) throw new InvalidMoney_Exception("Invalid money");
		else return POINT_VALUES.get(MONEY_VALUES.indexOf(moneyToAdd));
	}

	public synchronized int computeCartCost(String userId, int points) throws NotEnoughPoints_Exception,
			InvalidFoodId_Exception {
		int cartCost = 0;
		int quantity;
		int individualCost;

		List<FoodOrderItemHub> foodInCart = cart.get(userId);
		for (FoodOrderItemHub foodItem : foodInCart) {
			quantity = foodItem.getFoodQuantity();
			individualCost = getFood(foodItem.getFoodId()).getPrice();
			cartCost += individualCost * quantity;
		}

		if (cartCost > points) throw new NotEnoughPoints_Exception("Not enough points");

		return cartCost;
	}

	// Main operations -------------------------------------------------------
	public synchronized void activateAccount(String userId) throws InvalidUserId_Exception {
		try {
			checkUserIdValidity(userId);

			frontEnd.activateUser(userId);

			cart.put(userId, new ArrayList<>());
		} catch (InvalidUserId_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		} catch(InvalidEmailFault_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		} catch(EmailAlreadyExistsFault_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		} catch(InterruptedException e) {
			System.err.println("Error in polling (frontEnd) " + e);
			throw new RuntimeException(e);
		}

	}

	public synchronized void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCard_Exception, InvalidMoney_Exception, InvalidUserId_Exception {
		try {

			// get CC url
			/*Properties properties = new Properties();
			properties.load(getClass().getResourceAsStream("hub.properties"));
			String ccUrl = properties.getProperty("cc.url");
			System.out.println("ccUrl: " + ccUrl);*/

			//checkUserIdExistence(userId);

			//wsUrl: "http://ws.sd.rnl.tecnico.ulisboa.pt:8080/cc"
			CCClient client = new CCClient(ccUrl);
			if (!client.validateNumber(creditCardNumber)) throw new InvalidCreditCard_Exception("Invalid credit card number");

			int pointsToAdd = moneyToPoints(moneyToAdd);

			frontEnd.addPoints(userId, pointsToAdd);

		} catch (InvalidCreditCard_Exception e) {
			throw new InvalidCreditCard_Exception(e.getMessage());
		} catch (InvalidMoney_Exception e) {
			throw new InvalidMoney_Exception(e.getMessage());
		} catch (InvalidPointsFault_Exception e) {
			throw new InvalidMoney_Exception(e.getMessage());
		} catch (InvalidEmailFault_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		} catch (CCClientException e) {
			System.err.println("Error connecting to CC" + e);
			throw new RuntimeException(e);
		} catch(InterruptedException e) {
			System.err.println("Error in polling (frontEnd) " + e);
			throw new RuntimeException(e);
		} /*catch (IOException e) {
			System.err.println("Error obtaining CC server url from pom" + e);
			throw new RuntimeException(e);
		}*/

	}

	public synchronized List<FoodHub> search(String description, Comparator<FoodHub> comp) throws InvalidText_Exception {
		List<FoodHub> foods = new ArrayList<>();
		String restaurantName = "";
		try {
			checkDescriptionValidity(description);

			Collection<UDDIRecord> restaurants;
			restaurants = uddiNaming.listRecords("A61_Restaurant%");
			for (UDDIRecord rest: restaurants) {
				RestaurantClient client = new RestaurantClient(rest.getUrl());
				restaurantName = rest.getOrgName(); //saber id de restaurant
				foods.addAll(menuListToFoodHubList(client.searchMenus(description), restaurantName));
			}
		} catch(InvalidText_Exception e) {
			throw new InvalidText_Exception(e.getMessage());
		} catch(BadTextFault_Exception e) {
			throw new InvalidText_Exception(e.getMessage());
		} catch(UDDINamingException e) {
			System.err.println("Error connecting to UDDI" + e);
			throw new RuntimeException(e);
		} catch(RestaurantClientException e) {
			System.err.println("Error connecting to restaurant" + restaurantName + e);
			throw new RuntimeException(e);
		}
		Collections.sort(foods, comp);
		return Collections.unmodifiableList(foods);
	}

	public synchronized List<FoodHub> searchHungry(String description) throws InvalidText_Exception {

		return search(description, compareHungry);
	}

	public synchronized List<FoodHub> searchDeal(String description) throws InvalidText_Exception {
		return search(description, compareDeal);
	}

	public synchronized void addFoodToCart(String userId, FoodIdHub foodId, int foodQuantity)
			throws InvalidFoodId_Exception, InvalidFoodQuantity_Exception, InvalidUserId_Exception {
		try {
			checkUserIdExistence(userId);
			checkFoodQuantityValidity(foodQuantity);

			String restaurantName = foodId.getRestaurantId();
			String restaurant = uddiNaming.lookup(restaurantName);
			RestaurantClient client = new RestaurantClient(restaurant);
			Menu menu = client.getMenu(foodIdHubToMenuId(foodId));
			// adds food to cart
			cart.get(userId).add(menuToFoodOrderItemHub(menu, restaurantName, foodQuantity));

		} catch(InvalidFoodQuantity_Exception e) {
			throw new InvalidFoodQuantity_Exception(e.getMessage());
		} catch(InvalidUserId_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		} catch(BadMenuIdFault_Exception e) {
			throw new InvalidFoodId_Exception(e.getMessage());
		} catch(UDDINamingException e) {
			System.err.println("Error connecting to UDDI" + e);
			throw new RuntimeException(e);
		} catch(RestaurantClientException e) {
			System.err.println("Error connecting to restaurant" + e);
			throw new RuntimeException(e);
		}
	}

	public synchronized void clearCart(String userId) throws InvalidUserId_Exception {
		try {
			checkUserIdExistence(userId);
			cart.get(userId).clear();
		} catch (InvalidUserId_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		}
	}

	public synchronized FoodOrderHub orderCart(String userId)
			throws EmptyCart_Exception, InvalidUserId_Exception, NotEnoughPoints_Exception {
		List<FoodOrderItemHub> tempCart = new ArrayList<>();
		try {
			checkUserIdExistence(userId);

			if(cart.get(userId).isEmpty()) throw new EmptyCart_Exception("Empty cart");

			// checks if the user has enough points to buy everything in the corresponding cart
			int pointsToSpend = computeCartCost(userId, accountBalance(userId));

			// contacts restaurants to order the menus
			List<FoodOrderItemHub> foodInCart = cart.get(userId);
			for (FoodOrderItemHub foodItem : foodInCart) {
				String restaurantName = foodItem.getFoodId().getRestaurantId();
				String rest = uddiNaming.lookup(restaurantName);
				RestaurantClient clientRestaurant = new RestaurantClient(rest);
				clientRestaurant.orderMenu(foodIdHubToMenuId(foodItem.getFoodId()), foodItem.getFoodQuantity());
			}

			tempCart.addAll(cart.get(userId));
			clearCart(userId);

			// spends user points
			frontEnd.spendPoints(userId, pointsToSpend);

		} catch (EmptyCart_Exception e) {
			throw new EmptyCart_Exception("Cart is empty");
		} catch (InvalidUserId_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		} catch (InvalidEmailFault_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		} catch (BadMenuIdFault_Exception e) {
			System.err.println("Invalid menu id" + e);
			throw new RuntimeException(e);
		} catch (InvalidFoodId_Exception e) {
			System.err.println("Invalid food id" + e);
			throw new RuntimeException(e);
		}catch (BadQuantityFault_Exception e) {
			System.err.println("Invalid quantity" + e);
			throw new RuntimeException(e);
		} catch (InsufficientQuantityFault_Exception e) {
			System.err.println("Not enough quantity available" + e);
			throw new RuntimeException(e);
		} catch (InvalidPointsFault_Exception e) {
			throw new NotEnoughPoints_Exception(e.getMessage());
		} catch (NotEnoughPoints_Exception e) {
			throw new NotEnoughPoints_Exception(e.getMessage());
		} catch(UDDINamingException e) {
			System.err.println("Error connecting to UDDI" + e);
			throw new RuntimeException(e);
		} catch(RestaurantClientException e) {
			System.err.println("Error connecting to restaurant" + e);
			throw new RuntimeException(e);
		} catch(InterruptedException e) {
			System.err.println("Error in polling (frontEnd) " + e);
			throw new RuntimeException(e);
		}

		return foodOrderItemHubListToFoodOrderHub(tempCart);
	}

	public synchronized int accountBalance(String userId) throws InvalidUserId_Exception {
		int balance;
		try{
			balance = frontEnd.accountBalance(userId);
		}catch(InvalidEmailFault_Exception e){
			throw new InvalidUserId_Exception(e.getMessage());
		}
		catch(InterruptedException e){
			throw new InvalidUserId_Exception(e.getMessage());
		}

		return balance;
	}

	public synchronized FoodHub getFood(FoodIdHub foodId) throws InvalidFoodId_Exception {
		FoodHub food;
		try {
			String restaurantName = uddiNaming.lookup(foodId.getRestaurantId());
			RestaurantClient client = new RestaurantClient(restaurantName);
			food = menuToFoodHub(client.getMenu(foodIdHubToMenuId(foodId)), foodId.getRestaurantId());
		} catch (BadMenuIdFault_Exception e) {
			throw new InvalidFoodId_Exception(e.getMessage());
		} catch (UDDINamingException e) {
			System.err.println("Error connecting to UDDI" + e);
			throw new RuntimeException(e);
		} catch (RestaurantClientException e) {
			System.err.println("Error connecting to restaurant" + e);
			throw new RuntimeException(e);
		}

		return food;
	}

	public synchronized List<FoodOrderItemHub> cartContents(String userId) throws InvalidUserId_Exception {
		try {
			checkUserIdExistence(userId);
		} catch(InvalidUserId_Exception e) {
			throw new InvalidUserId_Exception(e.getMessage());
		}

		return cart.get(userId);
	}

	public void ctrlClear() {
		try{
			// ctrlClear Points
			String points = uddiNaming.lookup("A61_Points%");
			PointsClient clientPoints = new PointsClient(points);
			clientPoints.ctrlClear();

			// ctrlClear Restaurants
			Collection<UDDIRecord> restaurants = uddiNaming.listRecords("A61_Restaurant%");
			for (UDDIRecord rest: restaurants) {
				RestaurantClient clientRest = new RestaurantClient(rest.getUrl());
				clientRest.ctrlClear();
			}

			// ctrlClear Hub
			cart.clear();
			foodOrderId.set(0);
		} catch(UDDINamingException e) {
			System.err.println("Error connecting to UDDI" + e);
			throw new RuntimeException(e);
		} catch(RestaurantClientException e) {
			System.err.println("Error connecting to restaurant" + e);
			throw new RuntimeException(e);
		} catch(PointsClientException e) {
			System.err.println("Error connecting to Points" + e);
			throw new RuntimeException(e);
		}
	}

	public void ctrlInitFood(List<FoodInitHub> initialFoods) throws InvalidInit_Exception {
		try {
			Collection<UDDIRecord> restaurants = uddiNaming.listRecords("A61_Restaurant%");
			for (UDDIRecord rest : restaurants) {
				RestaurantClient client = new RestaurantClient(rest.getUrl());
				List<MenuInit> restMenus = new ArrayList<>();
				for (FoodInitHub food : initialFoods) {
					// checks if the food corresponds to a restaurant
					if (food.getFood().getId().getRestaurantId().equals(rest.getOrgName())) {
						restMenus.add(foodInitHubToMenuInit(food));
					}
				}
				client.ctrlInit(restMenus);
			}
		} catch (BadInitFault_Exception e) {
			throw new InvalidInit_Exception(e.getMessage());
		} catch (UDDINamingException e) {
			System.err.println("Error connecting to UDDI" + e);
			throw new RuntimeException(e);
		} catch (RestaurantClientException e) {
			System.err.println("Error connecting to restaurant" + e);
			throw new RuntimeException(e);
		}
	}


	public void ctrlInitUserPoints(int startPoints) throws InvalidInit_Exception {
		try {
			String points = uddiNaming.lookup("A61_Points%");
			PointsClient clientPoints = new PointsClient(points);
			clientPoints.ctrlInit(startPoints);
		} catch (com.forkexec.pts.ws.BadInitFault_Exception e) {
			throw new InvalidInit_Exception(e.getMessage());
		} catch(UDDINamingException e) {
			System.err.println("Error connecting to UDDI" + e);
			throw new RuntimeException(e);
		}  catch(PointsClientException e) {
			System.err.println("Error connecting to Points" + e);
			throw new RuntimeException(e);
		}
	}

	public String ctrlPing(String message){
		String msg = "";
		try {
			 msg = frontEnd.ctrlPing(message);
		} catch (ExecutionException e) {
			System.err.println("Error in execution " + e);
			throw new RuntimeException(e);
		} catch(InterruptedException e) {
			System.err.println("Error in polling (frontEnd) " + e);
			throw new RuntimeException(e);
		}
		return msg;
	}

	// View helpers ----------------------------------------------------------
	private FoodOrderHub foodOrderItemHubListToFoodOrderHub(List<FoodOrderItemHub> itemsList) {
		FoodOrderHub foodOrder = new FoodOrderHub();
		int newId = foodOrderId.incrementAndGet();
		foodOrder.setFoodOrderId(foodOrder.createFoodOrderId(Integer.toString(newId)));
		for (FoodOrderItemHub item : itemsList) {
			foodOrder.addItem(item);
		}
		return foodOrder;
	}

	private List<FoodHub> menuListToFoodHubList(List<Menu> menus, String restaurantId) {
		List<FoodHub> newMenus = new ArrayList<>();
		for (Menu menu : menus) {
			newMenus.add(menuToFoodHub(menu, restaurantId));
		}
		return newMenus;
	}

	private FoodHub menuToFoodHub(Menu menu, String restaurantId) {
		FoodHub newInfo = new FoodHub();
		newInfo.setId(menuIdToFoodHubId(menu.getId(), restaurantId));
		newInfo.setEntree(menu.getEntree());
		newInfo.setPlate(menu.getPlate());
		newInfo.setDessert(menu.getDessert());
		newInfo.setPrice(menu.getPrice());
		newInfo.setPreparationTime(menu.getPreparationTime());
		return newInfo;
	}

	private FoodOrderItemHub menuToFoodOrderItemHub(Menu menu, String restaurantId, int quantity) {
		FoodOrderItemHub newItem = new FoodOrderItemHub();
		newItem.setFoodId(menuIdToFoodHubId(menu.getId(), restaurantId));
		newItem.setFoodQuantity(quantity);
		return newItem;
	}

	private FoodIdHub menuIdToFoodHubId(MenuId menuId, String restaurantId) {
		FoodIdHub newInfo = new FoodIdHub();
		newInfo.setMenuId(menuId.getId());
		newInfo.setRestaurantId(restaurantId);
		return newInfo;
	}

	private MenuId foodIdHubToMenuId(FoodIdHub foodId) {
		MenuId newInfo = new MenuId();
		newInfo.setId(foodId.getMenuId());
		return newInfo;
	}

	private Menu foodHubToMenu(FoodHub food) {
		Menu newInfo = new Menu();
		newInfo.setId(foodIdHubToMenuId(food.getId()));
		newInfo.setEntree(food.getEntree());
		newInfo.setPlate(food.getPlate());
		newInfo.setDessert(food.getDessert());
		newInfo.setPrice(food.getPrice());
		newInfo.setPreparationTime(food.getPreparationTime());
		return newInfo;
	}

	private MenuInit foodInitHubToMenuInit(FoodInitHub food) {
		MenuInit menu = new MenuInit();
		menu.setMenu(foodHubToMenu(food.getFood()));
		menu.setQuantity(food.getQuantity());
		return menu;
	}
	
}
