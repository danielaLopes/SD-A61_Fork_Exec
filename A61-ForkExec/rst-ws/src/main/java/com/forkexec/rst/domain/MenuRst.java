package com.forkexec.rst.domain;
import com.forkexec.rst.domain.exceptions.*;
//import com.forkexec.rst.domain.*;


/**
 * Restaurant Menu
 *
 *
 */
public class MenuRst {
	private MenuIdRst menuId;
	private String entryDish, mainDish, dessertDish, description;
	private int quantity, prepTime, price;


	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	public MenuRst(MenuIdRst menuId, String entry, String main, String dessert, String description, int quantity, int prepTime, int price) {
    this.menuId = menuId;
    this.entryDish = entry;
    this.mainDish = main;
    this.dessertDish = dessert;
		this.description = description;
    this.quantity = quantity;
    this.prepTime = prepTime;
    this.price = price;
	}

	public MenuRst(){}

    public MenuIdRst getMenuId(){
        return this.menuId;
    }

    public String getEntryDish(){
        return this.entryDish;
    }

    public String getMainDish(){
        return this.mainDish;
    }

    public String getDessertDish(){
        return this.dessertDish;
    }

    public String getDescription(){
      return this.description;
    }

    public int getQuantity(){
      return this.quantity;
      }

    public int getPrepTime(){
      return this.prepTime;
    }

    public int getPrice(){
      return this.price;
    }
    
    public void setId(MenuIdRst id){
		  this.menuId = id;
    }
    
    public void setEntryDish(String entry){
		  this.entryDish = entry;
    }
    
    public void setMainDish(String main){
		  this.mainDish = main;
    }
    
    public void setDessertDish(String dessert){
		  this.dessertDish = dessert;
    }
    
    public void setDescription(String description){
		  this.description = description;
	  }

    public void setQuantity(int q){
        this.quantity = q;
    }

    public void setPrepTime(int time){
      this.prepTime = time;
    }

    public void setPrice(int price){
      this.price = price;
    }

}