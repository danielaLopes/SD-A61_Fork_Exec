package com.forkexec.rst.domain;
import com.forkexec.rst.domain.exceptions.*;
//import com.forkexec.rst.domain.*;

import com.forkexec.rst.ws.*;

import java.util.List;



/**
 * Menu order id
 *
 *
 */
public class MenuOrderIdRst {
	String menuOrderId;
	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private MenuOrderIdRst(String orderId) {
        this.menuOrderId = orderId;
	}

	public MenuOrderIdRst(){}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final MenuOrderIdRst INSTANCE = new MenuOrderIdRst();
	}

	public static synchronized MenuOrderIdRst getInstance() {
		return SingletonHolder.INSTANCE;
	}

    public String getMenuOrderId(){
        return this.menuOrderId;
	}
	
	public void setId(String id){
		this.menuOrderId = id;
	}


}