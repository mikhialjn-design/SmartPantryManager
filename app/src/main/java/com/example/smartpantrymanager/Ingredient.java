package com.example.smartpantrymanager;

/**
 * Represents one ingredient stored in the user's pantry.
 * This class holds the information needed to save, display
 * and later compare pantry ingredients with recipe requirements.
 */
public class Ingredient {

    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    // Used when an ingredient already exists in the SQLite database.
    public Ingredient(int id, String name, double quantity,
                      String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Used when creating a new ingredient before SQLite assigns its ID.
    public Ingredient(String name, double quantity,
                      String unit, String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
