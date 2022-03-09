package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

//create table views, table columns, buttons, and search bars

/**
 * The main screen controller.
 */
public class MainController implements Initializable {
    public TableView<Part> partsTable;
    public TableColumn partsIdCol;
    public TableColumn partsNameCol;
    public TableColumn partsInventoryLevelCol;
    public TableColumn partsPriceCol;
    public TableView<Product> productsTable;
    public TableColumn productsIdCol;
    public TableColumn productsNameCol;
    public TableColumn productsInventoryLevelCol;
    public TableColumn productsPriceCol;
    public Button addPart;
    public Button addProduct;
    public TextField searchParts;
    public Button modifyPart;
    public Button modifyProduct;
    public Button deleteProduct;
    public Button deletePart;
    public TextField searchProducts;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    /**
     * The first addition of table data is set to true.
     */
    private static boolean firstAddition = true;

    //create test data for the project

    /**
     * Only output table data once.
     */
    public static void testData(){
        //if this is not the first addition of data, do nothing
        if(!firstAddition){
            return;
        }
        firstAddition = false;

        Inventory.addPart(new InHouse(1, "Handlebar", 10.55, 33, 5, 50, 55));
        Inventory.addPart(new InHouse(2, "Rim", 110, 20, 5, 60, 33));
        Inventory.addPart(new InHouse(3, "Brake", 24.50, 33, 5, 50, 1));
        Inventory.addPart(new OutSourced(4, "Seat", 10.55, 33, 5, 50, "first"));
        Inventory.addPart(new OutSourced(5, "Tire", 10.55, 33, 5, 50, "Second"));

        Inventory.addProduct(new Product(1,"Electric Bike", 500, 4, 5, 7));
    }


    /**
     * Initialize the main screen.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){

        //input parts and products into the tables
        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProducts());

        //set up the tables
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //add test data to the table
        testData();

        }


    /**
     * Add a part to the part table.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        //set the add part scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddPartScreen.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Add Product");
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }

    //add Product to Products Table

    /**
     * Add a product to the product table.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        //set the add part scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProductScreen.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Add Product");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();

    }


    /**
     * Delete the selected part from the part table.
     *
     * @param actionEvent
     */
    public void onDeletePart(ActionEvent actionEvent){
        System.out.println("Clicked delete part");

        Part deleteAssociatedPart = partsTable.getSelectionModel().getSelectedItem();

        if (deleteAssociatedPart == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("Do you want to delete this product?");
        Optional <ButtonType> action = alert.showAndWait();

        if(action.get() == ButtonType.OK){
            Inventory.getAllParts().remove(deleteAssociatedPart);
        }
        else{
            return;
        }

    }


    /**
     * Delete the product from the product table.
     *
     * @param actionEvent
     */
    public void onDeleteProduct(ActionEvent actionEvent){

        Product deleteAssociatedProduct = productsTable.getSelectionModel().getSelectedItem();

        if(deleteAssociatedProduct == null)
            return;


        if (deleteAssociatedProduct != null) {
            if (deleteAssociatedProduct.getAllAssociatedParts().size() > 0){

                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Delete");
                alert2.setContentText("Cannot delete a product with linked parts. Please remove associated parts first.");
                alert2.showAndWait();

            }

            else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete");
                alert.setContentText("Do you want to delete this product?");
                Optional<ButtonType> action = alert.showAndWait();

                if (action.get() == ButtonType.OK) {
                    Inventory.getAllProducts().remove(deleteAssociatedProduct);
                } else {
                    return;
                }
            }
        }


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
     * Show all parts by partial string name search.
     *
     * @param partialName
     * @return
     */
    private ObservableList<Part> searchByPartName(String partialName){
        ObservableList<Part> namedPart = FXCollections.observableArrayList();


        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part prt : allParts){
            if(prt.getName().contains(partialName)){
                namedPart.add(prt);
            }

        }

        return namedPart;

    }

    /**
     * Show all parts by partial int ID search.
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
     * Pass selected part data into the modify part form when the user chooses to modify a part.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyPart(ActionEvent actionEvent) throws IOException {

        Part modifyAssociatedPart = partsTable.getSelectionModel().getSelectedItem();

        if (modifyAssociatedPart == null) {
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPartScreen.fxml"));
            Parent root = loader.load();

            ModifyPartController modifyPart = loader.getController();
            modifyPart.setPart(modifyAssociatedPart);

            Stage stage = new Stage();
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root, 600, 600));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Pass selected product data into the modify product form when the user chooses to modify a product.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {

        Product modifyAssociatedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (modifyAssociatedProduct == null) {
            return;
        }


        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProductScreen.fxml"));
            Parent root = loader.load();

            ModifyProductController modifyProduct = loader.getController();
            modifyProduct.setProduct(modifyAssociatedProduct);

            Stage stage = new Stage();
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Search algorithm that returns the searched product.
     *
     * @param actionEvent
     */
    public void onSearchProducts(ActionEvent actionEvent) {
        String queryProduct = searchProducts.getText();

        ObservableList<Product> product = searchByProductName(queryProduct);

        //if no string is available, try searching for an integer
        if(product.size() == 0){
            try {
                int id = Integer.parseInt(queryProduct);
                Product pdct = getProductIdNumber(id);
                if (pdct != null)
                    product.add(pdct);
            }
            catch (NumberFormatException e){
                //ignore
            }
        }

        if (product.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Product not found");
            alert.showAndWait();
            return;
        }
        productsTable.setItems(product);
    }


    /**
     * Show all products by partial string name search.
     *
     * @param partialName
     * @return
     */
    private ObservableList<Product> searchByProductName(String partialName) {
        ObservableList<Product> namedProduct = FXCollections.observableArrayList();


        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(Product pdct : allProducts){
            if(pdct.getName().contains(partialName)){
                namedProduct.add(pdct);
            }

        }

        return namedProduct;

    }


    /**
     * Show all products by partial integer ID search.
     *
     * @param id
     * @return
     */
    private Product getProductIdNumber(int id){
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(int i = 0; i < allProducts.size(); i++){
            Product pdct = allProducts.get(i);

            if(pdct.getId() == id){
                return pdct;
            }

        }

        return null;
    }


    /**
     * Alert the user they are about to close the application.
     *
     * @param actionEvent
     */
    public void onExit(ActionEvent actionEvent) {


        Alert close = new Alert(Alert.AlertType.CONFIRMATION);
        close.setTitle("Close Application");
        close.setHeaderText("You're about to close the application");
        close.setContentText("Are you sure you want to close the application?");

        if (close.showAndWait().get() == ButtonType.OK){
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        else{
            return;
        }

    }
}
