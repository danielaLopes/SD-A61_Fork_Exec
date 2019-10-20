package com.forkexec.hub.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jws.WebService;

import com.forkexec.hub.domain.*;
import com.forkexec.hub.domain.exceptions.*;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.hub.ws.HubPortType",
            wsdlLocation = "HubService.wsdl",
            name ="HubWebService",
            portName = "HubPort",
            targetNamespace="http://ws.hub.forkexec.com/",
            serviceName = "HubService"
)
public class HubPortImpl implements HubPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private HubEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public HubPortImpl(HubEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	@Override
	public void activateAccount(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().activateAccount(userId);
		} catch (InvalidUserId_Exception e) {
			throwInvalidUserId(e.getMessage());
		}
		
	}

	@Override
	public void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		try{
			Hub.getInstance().loadAccount(userId, moneyToAdd, creditCardNumber);
		} catch (InvalidUserId_Exception e) {
			throwInvalidUserId(e.getMessage());
		} catch (InvalidMoney_Exception e) {
			throwInvalidMoney(e.getMessage());
		} catch (InvalidCreditCard_Exception e) {
			throwInvalidCreditCard(e.getMessage());
		}

		
	}

	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		List<Food> foods = new ArrayList<>();
		try {

			foods = this.foodHubListToFoodList(Hub.getInstance().searchDeal(description));

		} catch (InvalidText_Exception e) {
			throwInvalidText(e.getMessage());
		}
		return Collections.unmodifiableList(foods);
	}
	
	@Override
	public List<Food> searchHungry(String description) throws InvalidTextFault_Exception {
		List<Food> foods = new ArrayList<>();
		try {
			foods = this.foodHubListToFoodList(Hub.getInstance().searchHungry(description));

		} catch (InvalidText_Exception e) {
        	throwInvalidText(e.getMessage());
		}
		return Collections.unmodifiableList(foods);
	}

	
	@Override
	public void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		try {
			FoodIdHub newFoodId = foodIdToFoodIdHub(foodId);
			Hub.getInstance().addFoodToCart(userId, newFoodId, foodQuantity);
		} catch (InvalidFoodId_Exception e) {
			throwInvalidFoodId(e.getMessage());
		} catch (InvalidFoodQuantity_Exception e) {
			throwInvalidFoodQuantity(e.getMessage());
		} catch (InvalidUserId_Exception e) {
			throwInvalidUserId(e.getMessage());
		}
		
	}

	@Override
	public void clearCart(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().clearCart(userId);
		} catch (InvalidUserId_Exception e) {
			throwInvalidUserId(e.getMessage());
		}

	}

	@Override
	public FoodOrder orderCart(String userId)
			throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception {
		FoodOrderHub foodOrder = new FoodOrderHub();
		try {
			foodOrder = Hub.getInstance().orderCart(userId);
		} catch (EmptyCart_Exception e) {
			throwEmptyCart(e.getMessage());
		} catch (InvalidUserId_Exception e) {
			throwInvalidUserId(e.getMessage());
		} catch (NotEnoughPoints_Exception e) {
			throwNotEnoughPoints(e.getMessage());
		}
		return foodOrderHubToFoodOrder(foodOrder);
	}

	@Override
	public int accountBalance(String userId) throws InvalidUserIdFault_Exception {
	    int balance = -1;
		try {
			balance = Hub.getInstance().accountBalance(userId);
		} catch (InvalidUserId_Exception e) {
			throwInvalidUserId(e.getMessage());
		}
		return balance;
	}

	@Override
	public Food getFood(FoodId foodId) throws InvalidFoodIdFault_Exception {
		Food food = new Food();
		try {
			food = foodHubToFood(Hub.getInstance().getFood(foodIdToFoodIdHub(foodId)));
		} catch (InvalidFoodId_Exception e) {
			throwInvalidFoodId(e.getMessage());
		}
		return food;
	}

	@Override
	public List<FoodOrderItem> cartContents(String userId) throws InvalidUserIdFault_Exception {
		List<FoodOrderItem> foodList = new ArrayList<>();
		try {
			foodList = foodOrderItemHubListToFoodOrderItemList(Hub.getInstance().cartContents(userId));
		} catch (InvalidUserId_Exception e) {
			throwInvalidUserId(e.getMessage());
		}
		return foodList;
	}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		if(inputMessage.equals("slow") || inputMessage.equals("die") || inputMessage.equals("exception")){
			Hub.getInstance().ctrlPing(inputMessage);
		}
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the service does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Hub";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Hub.getInstance().ctrlClear();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		try {
			Hub.getInstance().ctrlInitFood(foodInitListToFoodInitHubList(initialFoods));
		} catch (InvalidInit_Exception e) {
			throwInvalidInit(e.getMessage());
		}
	}
	
	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		try {
			Hub.getInstance().ctrlInitUserPoints(startPoints);
		} catch (InvalidInit_Exception e) {
			throwInvalidInit(e.getMessage());
		}
		
	}


	// View helpers ----------------------------------------------------------
	private List<FoodOrderItem> foodOrderItemHubListToFoodOrderItemList(List<FoodOrderItemHub> foodList) {
		List<FoodOrderItem> newFoodList = new ArrayList<>();
		for (FoodOrderItemHub foodItem : foodList) {
			FoodOrderItem newItem = foodOrderItemHubToFoodOrderItem(foodItem);
			newItem.setFoodId(foodIdHubToFoodId(foodItem.getFoodId()));
			newFoodList.add(newItem);
		}
		return newFoodList;
	}

	private FoodOrderItem foodOrderItemHubToFoodOrderItem(FoodOrderItemHub foodItem) {
		FoodOrderItem newInfo = new FoodOrderItem();
		newInfo.setFoodId(foodIdHubToFoodId(foodItem.getFoodId()));
		newInfo.setFoodQuantity(foodItem.getFoodQuantity());
		return newInfo;
	}

	private FoodOrder foodOrderHubToFoodOrder(FoodOrderHub foodOrder) {
		FoodOrder newOrder = new FoodOrder();
		FoodOrderId newId = new FoodOrderId();
		newId.setId(foodOrder.getFoodOrderId().getId());
		newOrder.setFoodOrderId(newId);
		for (FoodOrderItemHub item : foodOrder.getItems()) newOrder.getItems().add(foodOrderItemHubToFoodOrderItem(item));
		return newOrder;
	}

	private List<Food> foodHubListToFoodList(List<FoodHub> foodHubs) {
		List<Food> newFoods = new ArrayList<>();
		for (FoodHub foodHub : foodHubs) {
			newFoods.add(foodHubToFood(foodHub));
		}
		return newFoods;
	}

	private Food foodHubToFood(FoodHub food) {
		Food newInfo = new Food();
		newInfo.setId(foodIdHubToFoodId(food.getId()));
		newInfo.setEntree(food.getEntree());
		newInfo.setPlate(food.getPlate());
		newInfo.setDessert(food.getDessert());
		newInfo.setPrice(food.getPrice());
		newInfo.setPreparationTime(food.getPreparationTime());
		return newInfo;
	}

	private FoodId foodIdHubToFoodId(FoodIdHub foodIdHub) {
		FoodId newInfo = new FoodId();
		newInfo.setRestaurantId(foodIdHub.getRestaurantId());
		newInfo.setMenuId(foodIdHub.getMenuId());
		return newInfo;
	}

	private FoodIdHub foodIdToFoodIdHub(FoodId foodId) {
		FoodIdHub newInfo = new FoodIdHub();
		newInfo.setRestaurantId(foodId.getRestaurantId());
		newInfo.setMenuId(foodId.getMenuId());
		return newInfo;
	}

	private List<FoodInitHub> foodInitListToFoodInitHubList(List<FoodInit> foodList) {
		List<FoodInitHub> newList = new ArrayList<>();
		for (FoodInit food : foodList) {
			newList.add(foodInitToFoodInitHub(food));
		}
		return newList;
	}

	private FoodInitHub foodInitToFoodInitHub(FoodInit food) {
		FoodInitHub newFood = new FoodInitHub();
		newFood.setFood(foodToFoodHub(food.getFood()));
		newFood.setQuantity(food.getQuantity());
		return newFood;
	}

	private FoodHub foodToFoodHub(Food food) {
		FoodHub newFood = new FoodHub();
		newFood.setId(foodIdToFoodIdHub(food.getId()));
		newFood.setEntree(food.getEntree());
		newFood.setPlate(food.getPlate());
		newFood.setDessert(food.getDessert());
		newFood.setPrice(food.getPrice());
		newFood.setPreparationTime(food.getPreparationTime());
		return newFood;
	}

	
	// Exception helpers -----------------------------------------------------
	private void throwInvalidUserId(final String message) throws InvalidUserIdFault_Exception {
		InvalidUserIdFault faultInfo = new InvalidUserIdFault();
		faultInfo.message = message;
		throw new InvalidUserIdFault_Exception(message, faultInfo);
	}

	private void throwInvalidCreditCard(final String message) throws InvalidCreditCardFault_Exception {
		InvalidCreditCardFault faultInfo = new InvalidCreditCardFault();
		faultInfo.message = message;
		throw new InvalidCreditCardFault_Exception(message, faultInfo);
	}

	private void throwInvalidMoney(final String message) throws InvalidMoneyFault_Exception {
		InvalidMoneyFault faultInfo = new InvalidMoneyFault();
		faultInfo.message = message;
		throw new InvalidMoneyFault_Exception(message, faultInfo);
	}

	private void throwInvalidText(final String message) throws InvalidTextFault_Exception {
		InvalidTextFault faultInfo = new InvalidTextFault();
		faultInfo.message = message;
		throw new InvalidTextFault_Exception(message, faultInfo);
	}

	private void throwInvalidFoodId(final String message) throws InvalidFoodIdFault_Exception {
		InvalidFoodIdFault faultInfo = new InvalidFoodIdFault();
		faultInfo.message = message;
		throw new InvalidFoodIdFault_Exception(message, faultInfo);
	}

	private void throwInvalidFoodQuantity(final String message) throws InvalidFoodQuantityFault_Exception {
		InvalidFoodQuantityFault faultInfo = new InvalidFoodQuantityFault();
		faultInfo.message = message;
		throw new InvalidFoodQuantityFault_Exception(message, faultInfo);
	}

	private void throwEmptyCart(final String message) throws EmptyCartFault_Exception {
		EmptyCartFault faultInfo = new EmptyCartFault();
		faultInfo.message = message;
		throw new EmptyCartFault_Exception(message, faultInfo);
	}

	private void throwNotEnoughPoints(final String message) throws NotEnoughPointsFault_Exception {
		NotEnoughPointsFault faultInfo = new NotEnoughPointsFault();
		faultInfo.message = message;
		throw new NotEnoughPointsFault_Exception(message, faultInfo);
	}

	private void throwInvalidInit(final String message) throws InvalidInitFault_Exception {
		InvalidInitFault faultInfo = new InvalidInitFault();
		faultInfo.message = message;
		throw new InvalidInitFault_Exception(message, faultInfo);
	}


}
