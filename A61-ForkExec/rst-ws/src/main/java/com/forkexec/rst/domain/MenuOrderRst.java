package com.forkexec.rst.domain;
import com.forkexec.rst.domain.exceptions.*;

import javax.naming.InsufficientResourcesException;

import java.util.List;



/**
 * Menu order
 *
 *
 */
public class MenuOrderRst {
	private MenuOrderIdRst menuOrderId;
	private MenuIdRst menuId;
	private int menuQuantity;

	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	public MenuOrderRst(MenuOrderIdRst menuOrderId, MenuIdRst menuId, int menuQuantity) {
        this.menuOrderId = menuOrderId;
        this.menuId = menuId;
		this.menuQuantity = menuQuantity;
	}

	public MenuOrderRst(){}


    public MenuOrderIdRst getMenuOrderId(){
        return this.menuOrderId;
    }

    public MenuIdRst getMenuId(){
        return this.menuId;
    }

	public int getQuantity() throws BadMenuIdException{
        return this.menuQuantity;
	}

	public void setOrderId(MenuOrderIdRst id){
		this.menuOrderId = id;
	}

	public void setId(MenuIdRst id){
		this.menuId = id;
	}

	public void setQuantity(int q){
		this.menuQuantity = q;
	}

}