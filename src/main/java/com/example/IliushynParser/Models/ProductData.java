package com.example.IliushynParser.Models;

public class ProductData {
    private String search;
    private String productLink;
    private String internalNumber;
    private String description;
    private String price;
    private String availability;
    public String GetProductLink() {
        return productLink;
    }
    public String GetSearch() {
        return search;
    }
    public String GetInternalNumber() {
        return internalNumber;
    }
    public String GetDescription() {
        return description;
    }
    public String GetPrice() {
        return price;
    }
    public String GetAvailability() {
        return availability;
    }

    public void SetSearch(String search) {
        this.search = search;
    }
    public void SetProductLink(String productLink) {
        this.productLink = productLink;
    }
    public void SetInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }
    public void SetDescription(String description) {
        this.description = description;
    }
    public void SetPrice(String price) {
        this.price = price;
    }
    public void SetAvailability(String availability) {
        this.availability = availability;
    }
}
