package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateMenus {
    private List<String> desserts = Arrays.asList("Gelado", "Tarte", "Brigadeiro", "Pudim", "Morangos", "Pudim", "Morangos", "Profiteroles", "Tarte");
    private List<String> entries = Arrays.asList("Pao de Alho", "Salada", "Azeitonas", "Queijo", "Pao", "Chourico", "Azeitonas", "Pao", "Salada");
    private List<String> mainDishes = Arrays.asList("Sushi", "Feijoada", "Pizza", "Sushi", "Pizza", "Ramen", "Sushi", "Filetes", "Arroz de Pato");
    private List<Integer> price = Arrays.asList(15, 11, 8, 26, 10, 25, 20, 12, 8);
    private List<Integer> prepTime = Arrays.asList(10, 20, 15, 20, 10, 30, 18, 10, 8);

    private List<MenuInit> menus;

    public CreateMenus(){

        menus = new ArrayList<>();

        for (int i = 0; i < desserts.size(); i++){
            Menu menu = new Menu();
            MenuInit menuInit = new MenuInit();
            MenuId menuId = new MenuId();

            menuId.setId("Menu" + i);
            menu.setId(menuId);
            menu.setDessert(desserts.get(i));
            menu.setEntree(entries.get(i));
            menu.setPlate(mainDishes.get(i));
            menu.setPrice(price.get(i));
            menu.setPreparationTime(prepTime.get(i));
            menuInit.setMenu(menu);
            menuInit.setQuantity(10);

            menus.add(menuInit);
        }

    }

    public List<MenuInit> getMenuInit(){
        return menus;
    }

    /*public void setMenuInit(){
        return menus;
    }*/

}
