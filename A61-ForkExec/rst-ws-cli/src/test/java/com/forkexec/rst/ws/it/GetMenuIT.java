package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.*;

import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;



public class GetMenuIT extends BaseIT {


    @Test
    public void success() throws BadInitFault_Exception, BadMenuIdFault_Exception{
        CreateMenus initialMenus = new CreateMenus();

        client.ctrlInit(initialMenus.getMenuInit());

        MenuId menuId = new MenuId();
        menuId.setId("Menu0");
        Menu menu = new Menu();

        menu.setPreparationTime(10);
        menu.setPrice(15);
        menu.setPlate("Sushi");
        menu.setId(menuId);
        menu.setEntree("Pao de Alho");
        menu.setDessert("Gelado");

        Menu menuCli = client.getMenu(menuId);

        assertNotNull(menuCli);

        assertEquals(menuCli.getDessert(), menu.getDessert());
        assertEquals(menuCli.getEntree(), menu.getEntree());
        assertEquals(menuCli.getId().getId(), menu.getId().getId());
        assertEquals(menuCli.getPlate(), menu.getPlate());
        assertEquals(menuCli.getPreparationTime(), menu.getPreparationTime());
        assertEquals(menuCli.getPrice(), menu.getPrice());

    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void MenuIdDoesNotExist () throws BadMenuIdFault_Exception{
        MenuId menuId = new MenuId();
        menuId.setId("Menu89");

        // e suposto inicializar o menu td neste caso????
        Menu menuCli = client.getMenu(menuId);

        assertNotNull(menuCli);

    }
}
