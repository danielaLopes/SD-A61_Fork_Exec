package com.forkexec.hub.ws.cli;

import com.forkexec.hub.ws.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateFoods {
    private List<Integer> quantities = Arrays.asList(6, 16, 8, 20, 13, 5, 7, 11, 22);

    private List<String> desserts = Arrays.asList("Gelado", "Tarte", "Brigadeiro", "Pudim", "Morangos", "Pudim", "Morangos", "Profiteroles", "Tarte");
    private List<String> entries = Arrays.asList("Pao de Alho", "Salada", "Azeitonas", "Queijo", "Pao", "Chourico", "Azeitonas", "Pao", "Salada");
    private List<String> mainDishes = Arrays.asList("Sushi", "Feijoada", "Pizza", "Sushi", "Pizza", "Ramen", "Sushi", "Filetes", "Arroz de Pato");
    private List<Integer> price = Arrays.asList(15, 11, 8, 26, 10, 25, 20, 12, 8);
    private List<Integer> prepTime = Arrays.asList(10, 20, 15, 20, 10, 30, 18, 10, 8);

    private List<FoodInit> foods;

    public CreateFoods(){

        foods = new ArrayList<>();

        for (int i = 0; i < quantities.size(); i++){
            Food food = new Food();
            FoodInit foodInit = new FoodInit();
            FoodId foodId = new FoodId();

            foodId.setMenuId("Menu" + i);
            foodId.setRestaurantId("A61_Restaurant1");

            food.setId(foodId);
            food.setDessert(desserts.get(i));
            food.setEntree(entries.get(i));
            food.setPlate(mainDishes.get(i));
            food.setPrice(price.get(i));
            food.setPreparationTime(prepTime.get(i));

            foodInit.setQuantity(quantities.get(i));
            foodInit.setFood(food);

            foods.add(foodInit);

        }

    }

    public List<FoodInit> getFoodInit(){
        return foods;
    }

    /*public void setfoodInit(){
        return foods;
    }*/
}
