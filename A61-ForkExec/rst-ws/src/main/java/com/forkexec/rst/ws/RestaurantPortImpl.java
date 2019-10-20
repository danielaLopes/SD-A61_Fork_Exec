package com.forkexec.rst.ws;

import java.util.List;

import javax.jws.WebService;

import com.forkexec.rst.domain.*;
import com.forkexec.rst.domain.exceptions.*;

import java.util.List;
import java.util.ArrayList;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.rst.ws.RestaurantPortType",
            wsdlLocation = "RestaurantService.wsdl",
            name ="RestaurantWebService",
            portName = "RestaurantPort",
            targetNamespace="http://ws.rst.forkexec.com/",
            serviceName = "RestaurantService"
)
public class RestaurantPortImpl implements RestaurantPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private RestaurantEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public RestaurantPortImpl(RestaurantEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	@Override
	public Menu getMenu(MenuId menuId) throws BadMenuIdFault_Exception {
		MenuIdRst mId = null;
		Menu menu = null;
		try{
			mId = buildMenuIdRst(menuId);
			menu =  buildMenu(Restaurant.getInstance().getMenu(mId));
		}catch (BadMenuIdException e) {
			throwBadMenuId(e.getMessage());
		}
		return menu;
	}
	
	@Override
	public List<Menu> searchMenus(String descriptionText) throws BadTextFault_Exception {
		List<Menu> menus = new ArrayList<Menu>();
		try{
			menus = buildSearchMenus(Restaurant.getInstance().searchMenus(descriptionText));
		} catch(BadTextException e){
			throwBadTextException(e.getMessage());
		}
		return menus;
	}

	@Override
	public MenuOrder orderMenu(MenuId arg0, int arg1) throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
		MenuIdRst menuId = null;
		MenuOrder menuOrder = null;
		try{
			menuId = buildMenuIdRst(arg0);
			menuOrder = buildOrderMenu(Restaurant.getInstance().orderMenu(menuId, arg1));
		}catch(BadMenuIdException e){
			throwBadMenuId(e.getMessage());
		}catch(BadQuantityException e){
			throwBadQuantityException(e.getMessage());
		}catch(InsufficientQuantityException e){
			throwInsufficientQuantityException(e.getMessage());
		}
		return menuOrder;
	}

	

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the park does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Restaurant";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Restaurant.getInstance().ctrlClear();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInit(List<MenuInit> initialMenus) throws BadInitFault_Exception {
		List<MenuRst> menus = new ArrayList<>();
		try{
			for(MenuInit m : initialMenus){
				menus.add(buildMenuRst2(m));
			}
			Restaurant.getInstance().ctrlInit(menus);
		}catch(BadInitException e){
			throwBadInit(e.getMessage());
		}
	}

	// View helpers ----------------------------------------------------------

	// /** Helper to convert a domain object to a view. */

	private Menu buildMenu(MenuRst m) {
		Menu menu = new Menu();
		menu.setId(buildMenuId(m.getMenuId()));
		menu.setEntree(m.getEntryDish());
		menu.setPlate(m.getMainDish());
		menu.setDessert(m.getDessertDish());
		menu.setPrice(m.getPrice());
		menu.setPreparationTime(m.getPrepTime());
		return menu;
	}

	private MenuRst buildMenuRst(Menu m) {
		MenuRst menu = new MenuRst();
		menu.setId(buildMenuIdRst(m.getId()));
		menu.setEntryDish(m.getEntree());
		menu.setMainDish(m.getPlate());
		menu.setDessertDish(m.getDessert());
		menu.setPrice(m.getPrice());
		menu.setPrepTime(m.getPreparationTime());
		return menu;
	}

	private MenuRst buildMenuRst2(MenuInit m) {
		MenuRst menu = new MenuRst();
		menu.setId(buildMenuIdRst(m.getMenu().getId()));
		menu.setEntryDish(m.getMenu().getEntree());
		menu.setMainDish(m.getMenu().getPlate());
		menu.setDessertDish(m.getMenu().getDessert());
		menu.setPrice(m.getMenu().getPrice());
		menu.setPrepTime(m.getMenu().getPreparationTime());
		menu.setQuantity(m.getQuantity());
		return menu;
	}

	private List<Menu> buildSearchMenus(List<MenuRst> m){
		List<Menu> menus = new ArrayList<Menu>();
		for (MenuRst mRst : m){
			Menu menu = new Menu();
			menu.setId(buildMenuId(mRst.getMenuId()));
			menu.setEntree(mRst.getEntryDish());
			menu.setPlate(mRst.getMainDish());
			menu.setDessert(mRst.getDessertDish());
			menu.setPrice(mRst.getPrice());
			menu.setPreparationTime(mRst.getPrepTime());
			menus.add(menu);
		}
		return menus;
	}

	private MenuOrder buildOrderMenu(MenuOrderRst order) throws BadMenuIdException{
		MenuOrder mOrder = new MenuOrder();
		mOrder.setId(buildOrderId(order.getMenuOrderId()));
		mOrder.setMenuId(buildMenuId(order.getMenuId()));
		mOrder.setMenuQuantity(order.getQuantity());
		return mOrder;

	}

	private MenuId buildMenuId(MenuIdRst mId){
		MenuId menuId = new MenuId();
		menuId.setId(mId.getMenuId());
		return menuId;
	}

	private MenuIdRst buildMenuIdRst(MenuId mId){
		MenuIdRst menuId = new MenuIdRst();
		menuId.setId(mId.getId());
		return menuId;
	}

	private MenuOrderId buildOrderId(MenuOrderIdRst id){
		MenuOrderId orderId = new MenuOrderId();
		orderId.setId(id.getMenuOrderId());
		return orderId;
	}

	
	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}

	private void throwBadMenuId(final String message) throws BadMenuIdFault_Exception{
		BadMenuIdFault faultId = new BadMenuIdFault();
		faultId.message = message;
		throw new BadMenuIdFault_Exception(message, faultId);
	}

	private void throwBadTextException(final String message) throws BadTextFault_Exception{
		BadTextFault faultText = new BadTextFault();
		faultText.message = message;
		throw new BadTextFault_Exception(message, faultText);
	}

	private void throwInsufficientQuantityException(final String message) throws InsufficientQuantityFault_Exception{
		InsufficientQuantityFault faultQuantity = new InsufficientQuantityFault();
		faultQuantity.message = message;
		throw new InsufficientQuantityFault_Exception(message, faultQuantity);
	}

	private void throwBadQuantityException(final String message) throws BadQuantityFault_Exception{
		BadQuantityFault faultQuantity = new BadQuantityFault();
		faultQuantity.message = message;
		throw new BadQuantityFault_Exception(message, faultQuantity);
	}

}
