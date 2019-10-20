package com.forkexec.rst.domain;
import com.forkexec.rst.domain.exceptions.*;

import org.omg.CORBA.PRIVATE_MEMBER;


import javax.naming.InsufficientResourcesException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Restaurant
 *
 * A restaurant server.
 *
 */
public class Restaurant {
	//private ConcurrentHashMap<String, MenuRst> menus = new ConcurrentHashMap<>(); // menu do rst associado ao id do menu
	private ConcurrentHashMap<String, MenuRst> menus;
	private AtomicInteger count = new AtomicInteger(0);
	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */

	private Restaurant(){
		this.menus = new ConcurrentHashMap<>();
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Restaurant INSTANCE = new Restaurant();
	}

	public static synchronized Restaurant getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public synchronized MenuRst getMenu(MenuIdRst menuId) throws BadMenuIdException{
		if(!this.menus.containsKey(menuId.getMenuId())){
			throw new BadMenuIdException("Id not found: " + menuId.toString());
		}
		else{
			return this.menus.get(menuId.getMenuId());
		}
	}


	public synchronized void setMenus(ConcurrentHashMap<String, MenuRst> menus){
		this.menus = menus;
	}

	public synchronized List<MenuRst> searchMenus(String descriptionText) throws BadTextException{ 
		if(descriptionText.equals("") || descriptionText.contains(" ")){
			throw new BadTextException("Description not found.");
		}
		else{
		List<MenuRst> results = new ArrayList<>();
		Set<String> keys = this.menus.keySet();
			for(String key : keys){
				MenuRst m = this.menus.get(key);
				if(m.getEntryDish().contains(descriptionText) || m.getMainDish().contains(descriptionText) || m.getDessertDish().contains(descriptionText)){
					results.add(m);
				}
			}
			return results;
		}
	}

	public synchronized MenuOrderRst orderMenu(MenuIdRst menuId, int quantity) throws BadMenuIdException, BadQuantityException, InsufficientQuantityException{
		if(!this.menus.containsKey(menuId.getMenuId())){
			throw new BadMenuIdException("Id not found: " + menuId.toString());
		}
		else if(quantity < 1){
			throw new BadQuantityException("Invalid quantity: " + quantity);
		}
		else if(this.menus.get(menuId.getMenuId()).getQuantity() < quantity){
			throw new InsufficientQuantityException("Insufficient quantity: " + quantity);
		}
		else{
			count.incrementAndGet();
			MenuOrderIdRst id = new MenuOrderIdRst();
			id.setId(count.toString());
			this.menus.get(menuId.getMenuId()).setQuantity(this.menus.get(menuId.getMenuId()).getQuantity()-quantity);
			return new MenuOrderRst(id, menuId, quantity);
		}
	}

	public synchronized void ctrlClear(){
		this.menus.clear();
		this.count.set(0);
	}

	public synchronized void ctrlInit(List<MenuRst> initialMenus) throws BadInitException{
		for(MenuRst menu : initialMenus){
			this.menus.put(menu.getMenuId().getMenuId(), menu);
		}
	}
}
