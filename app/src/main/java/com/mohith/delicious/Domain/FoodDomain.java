package com.mohith.delicious.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {

    private String title;
    private String pic;
    private String description;
    private int price;
    private int numberInCart;

    public FoodDomain(String title, String pic, String description, int price) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.price = price;
    }

    public FoodDomain(String title, String pic, String description, int price, int numberInCart) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.price = price;
        this.numberInCart = numberInCart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
