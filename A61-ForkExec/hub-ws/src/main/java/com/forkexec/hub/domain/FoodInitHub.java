package com.forkexec.hub.domain;

public class FoodInitHub {

    protected FoodHub food;
    protected int quantity;

    /**
     * Gets the value of the food property.
     *
     * @return
     *     possible object is
     *     {@link FoodHub }
     *
     */
    public FoodHub getFood() {
        return food;
    }

    /**
     * Sets the value of the food property.
     *
     * @param value
     *     allowed object is
     *     {@link FoodHub }
     *
     */
    public void setFood(FoodHub value) {
        this.food = value;
    }

    /**
     * Gets the value of the quantity property.
     *
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     *
     */
    public void setQuantity(int value) {
        this.quantity = value;
    }

}

