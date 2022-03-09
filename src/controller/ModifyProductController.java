package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Modify a product in inventory.
 */
public class ModifyProductController implements Initializable {

    public TextField modifyProductName;
    public TextField modifyProductInventory;
    public TextField modifyProductPrice;
    public TextField modifyProductMin;
    public TextField modifyProductMax;
    public TableView<Part> partsTable;
    public TableColumn partsNameCol;
    public TableColumn partsInventoryLevelCol;
    public TableColumn partsPriceCol;
    public TableView productsTable;
    public TableColumn productsIdCol;
    public TableColumn productsNameCol;
    public TableColumn productsInventoryLevelCol;
    public TableColumn productsPriceCol;
    public Button removePart;
    public Button cancelModifyProduct;
    public TextField searchParts;
    public TableColumn partsIdCol;
    public TextField modifyProductId;
    private Product oldProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Initialize the parts table and associated parts for the selected product.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
     * Populate modify product form with the selected product's info.
     *
     * @param p
     */
    public void setProduct(Product p){

        oldProduct = p;

        //convert int or double to a string
        String id = Integer.toString(p.getId());
        String stock = Integer.toString(p.getStock());
        String price = Double.toString(p.getPrice());
        String min = Integer.toString(p.getMin());
        String max = Integer.toString(p.getMax());

        this.modifyProductId.setText(id);
        this.modifyProductName.setText(p.getName());
        this.modifyProductInventory.setText(stock);
        this.modifyProductPrice.setText(price);
        this.modifyProductMin.setText(min);
        this.modifyProductMax.setText(max);
        associatedParts.setAll(oldProduct.getAllAssociatedParts());

    }


    /**
     * Remove a part from the selected product.
     *
     * @param actionEvent
     */
    public void onRemovePart(ActionEvent actionEvent) {
        System.out.println("Clicked delete part");

        Part removeAssociatedPart = (Part) productsTable.getSelectionModel().getSelectedItem();

        if (removeAssociatedPart == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("Do you want to delete this product?");
        Optional<ButtonType> action = alert.showAndWait();

        if(action.get() == ButtonType.OK){
            associatedParts.remove(removeAssociatedPart);
        }
        else{
            return;
        }
    }


    /**
     * Go back to the main screen if the user cancels modifying a product.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelModifyProduct(ActionEvent actionEvent) throws IOException {

        //return to main screen
        Stage primaryStage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        primaryStage.close();

    }

    //set up our search algorithms

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
                //ignore
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
     * Add a new part to the selected product.
     *
     * @param actionEvent
     */
    public void onAddPart(ActionEvent actionEvent) {

        //check if there is a selected part
        Part addPart = (Part) partsTable.getSelectionModel().getSelectedItem();

        if (addPart == null)
            return;

        //grab selected part and add to the product table
        associatedParts.add(addPart);

    }


    /**
     * Save the modified product to inventory.
     *
     * @param actionEvent
     */
    public void onSaveModifyProduct(ActionEvent actionEvent) {

        //get user inputs

        //get the new part name from user
        String name = modifyProductName.getText();

        //get inventory number from the user
        String stockStr = modifyProductInventory.getText();

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
        String priceStr = modifyProductPrice.getText();

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
        String minStr = modifyProductMin.getText();

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
        String maxStr = modifyProductMax.getText();

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
        Product newModifyProduct = new Product(Integer.parseInt(modifyProductId.getText()), name, price, stock, min, max);

        //set the associated parts for the product
        newModifyProduct.setAssociatedParts(associatedParts);

        //add the updated product to inventory
        Inventory.addProduct(newModifyProduct);

        //remove the old product
        Inventory.getAllProducts().remove(oldProduct);

        //close this screen
        Stage primaryStage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        primaryStage.close();
    }

}
