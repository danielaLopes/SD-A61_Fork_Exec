package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SearchMenusIT extends BaseIT {
    private static List<String> desserts = Arrays.asList("Gelado", "Pudim", "Morangos");
    private static List<String> entries = Arrays.asList("Pao de Alho", "Queijo", "Azeitonas");
    private static List<String> mainDishes = Arrays.asList("Sushi", "Sushi", "Sushi");
    private static List<String> menusId = Arrays.asList("Menu0", "Menu3", "Menu6");
    private static List<Integer> price = Arrays.asList(15, 26, 20);
    private static List<Integer> prepTime = Arrays.asList(10, 20, 18);
    private static List<Menu> menus = new ArrayList<>();

    @BeforeClass
    public static void populate() throws BadInitFault_Exception{

        CreateMenus initialMenus = new CreateMenus();

        client.ctrlInit(initialMenus.getMenuInit());
        /*for (int i = 0; i < desserts.size(); i++){
            Menu menu = new Menu();
            MenuId menuId = new MenuId();

            menuId.setId(menusId.get(i));
            menu.setId(menuId);
            menu.setDessert(desserts.get(i));
            menu.setEntree(entries.get(i));
            menu.setPlate(mainDishes.get(i));
            menu.setPrice(price.get(i));
            menu.setPreparationTime(prepTime.get(i));

            menus.add(menu);
        }*/
    }

    @Test
    public void successWithThree() throws BadInitFault_Exception, BadTextFault_Exception {

        List<Menu> menusCli = client.searchMenus("Sushi");
        assertEquals(3, menusCli.size());

        /*int i = 0; E SUPOSTO VERIFICAR TD????

        for (Menu menu: menusCli){
            Menu testMenu = menus.get(i);
            assertEquals(menu.getDessert(), testMenu.getDessert());
            assertEquals(menu.getEntree(), testMenu.getEntree());
            assertEquals(menu.getId().getId(), testMenu.getId().getId());
            assertEquals(menu.getPlate(), testMenu.getPlate());
            assertEquals(menu.getPreparationTime(), testMenu.getPreparationTime());
            assertEquals(menu.getPrice(), testMenu.getPrice());
            i++;
        }*/
    }

    @Test
    public void successWithTwo() throws BadTextFault_Exception {

        List<Menu> menusCli = client.searchMenus("Azeitonas");
        assertEquals(2, menusCli.size());
    }

    @Test
    public void successCaseSensitive() throws BadTextFault_Exception {

        List<Menu> menusCli = client.searchMenus("azeitonas");
        assertEquals(0, menusCli.size());
    }

    @Test(expected = BadTextFault_Exception.class)
    public void textWithSpaces () throws BadTextFault_Exception{

        client.searchMenus("Arroz de Pato");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void textEmpty () throws BadTextFault_Exception{

        client.searchMenus("");
    }
}
