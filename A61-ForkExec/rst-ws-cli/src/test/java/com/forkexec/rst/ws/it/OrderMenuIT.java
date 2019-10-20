package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.*;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class OrderMenuIT extends BaseIT {

    @BeforeClass
    public static void populate() throws BadInitFault_Exception {

        CreateMenus initialMenus = new CreateMenus();

        client.ctrlInit(initialMenus.getMenuInit());

    }

    @Test
    public void successWithOne() throws BadMenuIdFault_Exception, BadQuantityFault_Exception , InsufficientQuantityFault_Exception{
        MenuId menuId = new MenuId();
        menuId.setId("Menu5");

        MenuOrderId menuOrderId = new MenuOrderId();
        menuOrderId.setId("1");

        MenuOrder menuOrder = new MenuOrder();
        menuOrder.setId(menuOrderId);
        menuOrder.setMenuId(menuId);
        menuOrder.setMenuQuantity(5);


        MenuOrder menuOrderCli = client.orderMenu(menuId, 5);

        assertEquals(menuOrder.getId().getId(), menuOrderCli.getId().getId());
        assertEquals(menuOrder.getMenuId().getId(), menuOrderCli.getMenuId().getId());
        assertEquals(menuOrder.getMenuQuantity(), menuOrderCli.getMenuQuantity());

    }

    @Test
    public void successWithTwo() throws BadMenuIdFault_Exception, BadQuantityFault_Exception , InsufficientQuantityFault_Exception{
        MenuId menuId = new MenuId();
        menuId.setId("Menu7");

        MenuOrderId menuOrderId = new MenuOrderId();
        menuOrderId.setId("2");

        MenuOrder menuOrder = new MenuOrder();
        menuOrder.setId(menuOrderId);
        menuOrder.setMenuId(menuId);
        menuOrder.setMenuQuantity(5);


        MenuOrder menuOrderCli = client.orderMenu(menuId, 5);

        assertEquals(menuOrder.getId().getId(), menuOrderCli.getId().getId());
        assertEquals(menuOrder.getMenuId().getId(), menuOrderCli.getMenuId().getId());
        assertEquals(menuOrder.getMenuQuantity(), menuOrderCli.getMenuQuantity());

    }

    @Test(expected = InsufficientQuantityFault_Exception.class)
    public void insufficientQuantity() throws BadMenuIdFault_Exception, BadQuantityFault_Exception , InsufficientQuantityFault_Exception{
        MenuId menuId = new MenuId();
        menuId.setId("Menu1");


        client.orderMenu(menuId, 12);
    }

    @Test(expected = InsufficientQuantityFault_Exception.class)
    public void insufficientQuantitySecondTime() throws BadMenuIdFault_Exception, BadQuantityFault_Exception , InsufficientQuantityFault_Exception{
        MenuId menuId = new MenuId();
        menuId.setId("Menu7");


        client.orderMenu(menuId, 6);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void MenuIdDoesNotExist() throws BadMenuIdFault_Exception, BadQuantityFault_Exception , InsufficientQuantityFault_Exception{
        MenuId menuId = new MenuId();
        menuId.setId("Menu98");


        client.orderMenu(menuId, 5);
    }

    @Test(expected = BadQuantityFault_Exception.class)
    public void negativeQuantity() throws BadMenuIdFault_Exception, BadQuantityFault_Exception , InsufficientQuantityFault_Exception{
        MenuId menuId = new MenuId();
        menuId.setId("Menu1");


        client.orderMenu(menuId, -1);
    }
}
