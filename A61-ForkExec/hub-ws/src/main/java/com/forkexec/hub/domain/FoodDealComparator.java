package com.forkexec.hub.domain;

import java.util.Comparator;

public class FoodDealComparator implements Comparator<FoodHub> {

    public int compare(FoodHub food1, FoodHub food2) {
        int foodPrice1 = food1.getPrice();
        int foodPrice2 = food2.getPrice();

        return foodPrice1 - foodPrice2;
    }
}
