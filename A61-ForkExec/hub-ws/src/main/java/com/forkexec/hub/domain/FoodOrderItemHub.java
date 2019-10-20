package com.forkexec.hub.domain;

public class FoodOrderItemHub {

    protected FoodIdHub foodId;
    protected int foodQuantity;

    /**
     * Gets the value of the foodId property.
     *
     * @return possible object is
     *         {@link FoodIdHub }
     *
     */
    public FoodIdHub getFoodId() {
        return foodId;
    }

    /**
     * Sets the value of the foodId property.
     *
     * @param value
     *     allowed object is
     *     {@link FoodIdHub }
     *
     */
    public void setFoodId(FoodIdHub value) {
        this.foodId = value;
    }

    /**
     * Gets the value of the foodQuantity property.
     *
     */
    public int getFoodQuantity() {
        return foodQuantity;
    }

    /**
     * Sets the value of the foodQuantity property.
     *
     */
    public void setFoodQuantity(int value) {
        this.foodQuantity = value;
    }

}


