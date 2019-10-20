package com.forkexec.hub.domain;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class FoodOrderHub {

    protected FoodOrderId foodOrderId;
    @XmlElement(nillable = true)
    protected List<FoodOrderItemHub> items;

    public class FoodOrderId {

        protected String id;

        /**
         * Gets the value of the id property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setId(String value) {
            this.id = value;
        }

    }

    /**
     * Creates new instance of FoodOrderId.
     *
     * @param value
     *     allowed object is
     *     {@link String id }
     *
     */
    public FoodOrderId createFoodOrderId(String id) {
        FoodOrderId newId = new FoodOrderId();
        newId.setId(id);
        return newId;
    }


    /**
     * Gets the value of the foodOrderId property.
     *
     * @return
     *     possible object is
     *     {@link FoodOrderId }
     *
     */
    public FoodOrderId getFoodOrderId() {
        return foodOrderId;
    }

    /**
     * Sets the value of the foodOrderId property.
     *
     * @param value
     *     allowed object is
     *     {@link FoodOrderId }
     *
     */
    public void setFoodOrderId(FoodOrderId value) {
        this.foodOrderId = value;
    }

    /**
     * Gets the value of the items property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the items property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItems().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FoodOrderItemHub }
     *
     *
     */
    public List<FoodOrderItemHub> getItems() {
        if (items == null) {
            items = new ArrayList<FoodOrderItemHub>();
        }
        return this.items;
    }

    public void addItem(FoodOrderItemHub item) {
        getItems().add(item);
    }

}

