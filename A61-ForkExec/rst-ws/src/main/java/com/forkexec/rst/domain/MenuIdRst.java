package com.forkexec.rst.domain;
import com.forkexec.rst.domain.exceptions.*;
//import com.forkexec.rst.domain.*;

import com.forkexec.rst.ws.*;

import java.util.List;



/**
 * Menu id
 *
 *
 */
public class MenuIdRst {
	String menuId;
	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private MenuIdRst(String menuId) {
        this.menuId = menuId;
	}

	public MenuIdRst(){}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final MenuIdRst INSTANCE = new MenuIdRst();
	}

	public static synchronized MenuIdRst getInstance() {
		return SingletonHolder.INSTANCE;
	}

    public String getMenuId(){
        return this.menuId;
	}
	
	public void setId(String id){
		this.menuId = id;
	}


}