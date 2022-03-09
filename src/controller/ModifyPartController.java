package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Modify a part in inventory.
 */
public class ModifyPartController implements Initializable {

    public RadioButton inHouse;
    public ToggleGroup toggleAdd;
    public RadioButton outSourced;
    public Label machineId;
    public TextField modifyPartId;
    public TextField modifyPartName;
    public TextField modifyPartInventory;
    public TextField modifyPartPrice;
    public TextField modifyPartMin;
    public TextField modifyPartMax;
    public TextField modifyPartMachineId;
    public Button saveModifyPart;
    public Button cancelModifyPart;
    private Part oldPart;


    /**
     * Initialize the controller.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    /**
     * RUNTIME ERROR: I forgot to parse the integers to strings. Parsing allowed me to populate the modify part form.
     *
     * Populate the modify part form based on the selected part.
     *
     * @param p
     */
    public void setPart(Part p){

        oldPart = p;

        //convert int or double to a string
        String id = Integer.toString(p.getId());
        String stock = Integer.toString(p.getStock());
        String price = Double.toString(p.getPrice());
        String min = Integer.toString(p.getMin());
        String max = Integer.toString(p.getMax());


        //set text for in-house part
        if (p instanceof InHouse){

            inHouse.setSelected(true);
            this.modifyPartId.setText(id);
            this.modifyPartName.setText(p.getName());
            this.modifyPartInventory.setText(stock);
            this.modifyPartPrice.setText(price);
            this.modifyPartMin.setText(min);
            this.modifyPartMax.setText(max);
            this.modifyPartMachineId.setText(Integer.toString(((InHouse) p).getMachineId()));

        }

        //set text for outsourced part
        if (p instanceof OutSourced){

            outSourced.setSelected(true);
            this.modifyPartId.setText(id);
            this.modifyPartName.setText(p.getName());
            this.modifyPartInventory.setText(stock);
            this.modifyPartPrice.setText(price);
            this.modifyPartMin.setText(min);
            this.modifyPartMax.setText(max);
            this.modifyPartMachineId.setText(((OutSourced)p).getCompanyName());

        }

    }


    /**
     * Handle in-house form data based on the toggle button.
     *
     * @param actionEvent
     */
    public void onInHouse(ActionEvent actionEvent) {
        inHouse.setSelected(true);
        machineId.setText("Machine ID");
    }


    /**
     * Handle outsourced form data based on the toggle button.
     *
     * @param actionEvent
     */
    public void onOutSourced(ActionEvent actionEvent) {
        outSourced.setSelected(true);
        machineId.setText("Company Name");
    }


    /**
     * Save the modified part to inventory.
     *
     * @param actionEvent
     */
    public void onSaveModifyPart(ActionEvent actionEvent) {

        String idStr = modifyPartId.getText();
        int id = 0;
        id = Integer.parseInt(idStr);

        //get user inputs

        //get the new part name from user
        String name = modifyPartName.getText();

        //get inventory number from the user
        String inventoryStr = modifyPartInventory.getText();

        //Exception handling for non int
        //initialize inventory
        int inventory = 0;

        try {
            inventory = Integer.parseInt(inventoryStr);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter the number in stock");
            alert.showAndWait();
            return;
        }

        //get price for new part from the user
        String priceStr = modifyPartPrice.getText();

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
        String minStr = modifyPartMin.getText();

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
        String maxStr = modifyPartMax.getText();

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

        if (inventory > max){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Inventory must be less than max");
            alert.showAndWait();
            return;
        }

        if (min > inventory){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Inventory must be more than min");
            alert.showAndWait();
            return;
        }

        //if InHouse is selected, handle exceptions and get the MachineID
        if(inHouse.isSelected()) {
            String machineIdStr = modifyPartMachineId.getText();
            //Exception handling for non int
            int machineId = 0;
            try{
                machineId = Integer.parseInt(machineIdStr);
            }
            catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a number for the machine ID");
                alert.showAndWait();
                return;
            }
            Inventory.addPart(new InHouse(id, name, price, inventory, min, max, machineId));
            Inventory.getAllParts().remove(oldPart);
        }
        //otherwise, get the company name from the user
        else {
            //get the company name from the user
            String companyName = modifyPartMachineId.getText();
            Inventory.addPart((new OutSourced(id, name, price,  inventory, min, max, companyName)));
            Inventory.getAllParts().remove(oldPart);
        }

        //close the screen
        Stage primaryStage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        primaryStage.close();

    }


    /**
     * Go back to the main screen if the user cancels modifying a part.
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelModifyPart(ActionEvent actionEvent) throws IOException {
        //return to main screen
        Stage primaryStage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        primaryStage.close();

    }
}
