package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Product Class.
 */
public class Product {

    /**
     * Create an observable list of parts for associated parts.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;


    /**
     * constructor for the product class.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /**
     * Get the product ID.
     *
     * @return
     */
    public int getId() {
        return id;
    }


    /**
     * Set the product ID.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the product name.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set the product name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the product price.
     *
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set the product price.
     *
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Get the product stock.
     *
     * @return
     */
    public int getStock() {
        return stock;
    }

    /**
     * Set the product stock.
     *
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Get the product minimum.
     *
     * @return
     */
    public int getMin() {
        return min;
    }

    /**
     * Set the product minimum.
     *
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Get the product maximum.
     *
     * @return
     */
    public int getMax() {
        return max;
    }

    /**
     * Set the product maximum.
     *
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Add an associated part to the product.
     *
     * @param part
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Get all associated parts of the product from the observable list of parts.
     *
     * @return
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

    /**
     * Delete an associated part from the product.
     *
     * @param selectedAssociatedPart
     * @return
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        return true;
    }

    /**
     * Set the associated parts based on the parts list for the product.
     *
     * @param list
     */
    public void setAssociatedParts(ObservableList<Part> list){
        associatedParts.setAll(list);
    }
}


