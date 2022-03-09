package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Inventory class.
 */
public class Inventory {


    /**
     * Create an observable list of parts called allParts.
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();


    /**
     * Create an observable list of products called allProducts.
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Add parts to the allParts table.
     * @param part
     */
    public static void addPart(Part part){
        allParts.add(part);
    }


    /**
     * Add products to the allProducts table.
     * @param product
     */
    public static void addProduct(Product product){
        allProducts.add(product);
    }


    /**
     * Search a part based on an integer of ID number.
     *
     * @param partId
     * @return
     */
    public Part lookupPart(int partId){
        for (Part p : allParts){
            if (p.getId() == partId)
                return p;
        }
        return null;
    }


    /**
     * Search a product based on an integer of ID number.
     *
     * @param productId
     * @return
     */
    public Product lookupProduct(int productId){
        for (Product a : allProducts){
            if (a.getId() == productId)
                return a;
        }
        return null;
    }


    /**
     * Search a part based on a string the user types.
     *
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> list = FXCollections.observableArrayList();

        for (Part p : allParts){
            if (p.getName().contains(partName)){
                list.add(p);
            }
        }

        return list;
    }


    /**
     * Get all parts from the observable list of part.
     *
     * @return
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }


    /**
     * Get all products from the observable list of product.
     * @return
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
