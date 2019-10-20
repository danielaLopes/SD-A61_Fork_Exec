package com.forkexec.hub.domain;

public class FoodHub {

    private FoodIdHub id;
    private String entree;
    private String plate;
    private String dessert;
    private int price;
    private int preparationTime;

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link FoodIdHub }
     *
     */
    public FoodIdHub getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link FoodIdHub }
     *
     */
    public void setId(FoodIdHub value) {
        this.id = value;
    }

    /**
     * Gets the value of the entree property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEntree() {
        return entree;
    }

    /**
     * Sets the value of the entree property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEntree(String value) {
        this.entree = value;
    }

    /**
     * Gets the value of the plate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPlate() {
        return plate;
    }

    /**
     * Sets the value of the plate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPlate(String value) {
        this.plate = value;
    }

    /**
     * Gets the value of the dessert property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDessert() {
        return dessert;
    }

    /**
     * Sets the value of the dessert property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDessert(String value) {
        this.dessert = value;
    }

    /**
     * Gets the value of the price property.
     *
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     *
     */
    public void setPrice(int value) {
        this.price = value;
    }

    /**
     * Gets the value of the preparationTime property.
     *
     */
    public int getPreparationTime() {
        return preparationTime;
    }

    /**
     * Sets the value of the preparationTime property.
     *
     */
    public void setPreparationTime(int value) {
        this.preparationTime = value;
    }
}
