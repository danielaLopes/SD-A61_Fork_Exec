package com.forkexec.hub.domain;

import java.util.Comparator;

public class FoodHungryComparator implements Comparator<FoodHub> {

    public int compare(FoodHub food1, FoodHub food2) {
        int foodTime1 = food1.getPreparationTime();
        int foodTime2 = food2.getPreparationTime();

        return foodTime1 - foodTime2;
    }
}
