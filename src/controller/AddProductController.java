package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

//create accessible text fields, tables, buttons, and an observable list of parts

/**
 * Add a product to inventory.
 */
public class AddProductController implements Initializable {
    public TextField newProductName;
    public TextField newProductInventory;
    public TextField newProductPrice;
    public TextField newProductMin;
    public TextField newProductMax;
    public TextField searchParts;
    public TableView partsTable;
    public TableColumn partsIdCol;
    public TableColumn partsNameCol;
    public TableColumn partsInventoryLevelCol;
    public TableColumn partsPriceCol;
    public TableView productsTable;
    public TableColumn productsIdCol;
    public TableColumn productsNameCol;
    public TableColumn productsInventoryLevelCol;
    public TableColumn productsPriceCol;
    public Button removePart;
    public Button save;
    public Button cancel;
    public Button addPart;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    /**
     * Initialize the tables.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up the tables
        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(associatedParts);

        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }


    /**
     * Search algorithm that returns the searched part.
     *
     * @param actionEvent
     */
    public void onSearchParts(ActionEvent actionEvent){
        String queryPart = searchParts.getText();

        ObservableList<Part> parts = searchByPartName(queryPart);

        //if no string is available, try searching for an integer
        if(parts.size() == 0){
            try {
                int id = Integer.parseInt(queryPart);
                Part pt = getPartIdNumber(id);
                if (pt != null)
                    parts.add(pt);
            }
            catch (NumberFormatException e){
                //do nothing
            }
        }
        if (parts.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Part not found");
            alert.showAndWait();
            return;
        }

        partsTable.setItems(parts);

    }


    /**
     * Show allParts by partial String name search.
     *
     * @param partialName
     * @return
     */
    private ObservableList<Part> searchByPartName(String partialName){
        ObservableList<Part> namedPart = FXCollections.observableArrayList();


        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part pt : allParts){
            if(pt.getName().contains(partialName)){
                namedPart.add(pt);
            }

        }

        return namedPart;

    }


    /**
     * Show allParts by partial int ID search.
     *
     * @param id
     * @return
     */
    private Part getPartIdNumber(int id){
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(int i = 0; i < allParts.size(); i++){
            Part pt = allParts.get(i);

            if(pt.getId() == id){
                return pt;
            }

        }

        return null;
    }


    /**
     * Add the selected part to the products table.
     *
     * @param actionEvent
     */
    public void onAddPart(ActionEvent actionEvent) {

        //check if there is a selected part
        Part addPart = (Part) partsTable.getSelectionModel().getSelectedItem();

        if (addPart == null)
            return;

        //add the addPart to the associated product
        associatedParts.add(addPart);

    }


    /**
     * Remove a part from the selected product.
     *
     * @param actionEvent
     */
    public void onRemovePart(ActionEvent actionEvent) {

        //check if there is a selected product
        Part removeAssociatedPart = (Part) productsTable.getSelectionModel().getSelectedItem();

        if (removeAssociatedPart == null)
            return;

        //display a confirmation alert to the user
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove");
        alert.setContentText("Do you want to remove this part from your product?");
        Optional<ButtonType> action = alert.showAndWait();

        //remove the selected part from the product table if the user clicks "OK"
        if(action.get() == ButtonType.OK){
            associatedParts.remove(removeAssociatedPart);
        }
        else{
            return;
        }

    }


    /**
     * Save the new product to inventory.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onSave(ActionEvent actionEvent) throws IOException {

        //generate a unique random id number from 2000-2999 for a new product
        Random number = new Random();
        int id = 0;

        ArrayList<Integer> productList = new ArrayList<Integer>();

        for (int i=0; i < 1; i++){
            id = 2000 + number.nextInt(1000);

            if (!productList.contains(id)){
                productList.add(id);
            }

        }

        //get user inputs
        //get the new product name from user
        String name = newProductName.getText();

        //get inventory number from the user
        String stockStr = newProductInventory.getText();

        //Exception handling for non int
        //initialize inventory
        int stock = 0;

        try {
            stock = Integer.parseInt(stockStr);
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter the number in stock");
            alert.showAndWait();
            return;
        }

        //get price for new part from the user
        String priceStr = newProductPrice.getText();

        //Exception handling for non numbers
        double price = 0;

        try{
            price = Double.parseDouble(priceStr);
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a price for the part");
            alert.showAndWait();
            return;
        }

        //get minimum inventory required from the user
        String minStr = newProductMin.getText();

        //Exception handling for non int
        int min = 0;

        try{
            min = Integer.parseInt(minStr);
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a minimum number required in inventory");
            alert.showAndWait();
            return;
        }

        //get maximum inventory allowed from the user
        String maxStr = newProductMax.getText();

        //Exception handling for non int
        int max = 0;

        try{
            max = Integer.parseInt(maxStr);
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a maximum number allowed in inventory");
            alert.showAndWait();
            return;
        }

        //check for simple errors
        if (min > max){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Max must be greater than min");
            alert.showAndWait();
            return;
        }

        if (stock > max){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Inventory must be less than max");
            alert.showAndWait();
            return;
        }

        if (min > stock){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Inventory must be more than min");
            alert.showAndWait();
            return;
        }

        //create the new product
        Product newProduct = new Product(id, name, price, stock, min, max);

        //set the associated parts for the product
        newProduct.setAssociatedParts(associatedParts);

        //add the product to inventory
        Inventory.addProduct(newProduct);

        //go back to the main screen
        Stage primaryStage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        primaryStage.close();
    }


    /**
     * Go back to the main screen if the user cancels adding a new product.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        primaryStage.close();
    }

}
