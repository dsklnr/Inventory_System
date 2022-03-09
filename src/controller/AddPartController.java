package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


/**
 * Add a part to inventory.
 */
public class AddPartController implements Initializable {
    public RadioButton inHouse;
    public RadioButton outSourced;
    public ToggleGroup toggleAdd;
    public TextField newPartName;
    public TextField newPartInventory;
    public TextField newPartPrice;
    public TextField newPartMin;
    public TextField newPartMax;
    public TextField newPartMachineId;
    public Label machineId;
    public Button onCancelAddPart;
    public Button saveAddPart;


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
     * If machineId is selected, set to true and set text to Machine ID.
     *
     * @param actionEvent
     */
    public void onInHouse(ActionEvent actionEvent) {
        inHouse.setSelected(true);
        machineId.setText("Machine ID");
    }


    /**
     * If Outsourced in selected, set to true and set text to Company Name.
     *
     * @param actionEvent
     */
    public void onOutSourced(ActionEvent actionEvent) {
        outSourced.setSelected(true);
        machineId.setText("Company Name");
        //newPartMachineId.setId(newPartCompanyName);


    }

    /**
     *FUTURE ENHANCEMENT: Add an add another part button if the user wants to add more than one part. If they want to add another part, save current part and display a blank add part form.
     *When the user clicks save, generate a random ID number, handle exceptions, and add the new part to the parts table.
     *
     * Save the new part to inventory.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveAddPart(ActionEvent actionEvent) throws IOException {

        //generate a unique random id number from 1000-1999 for a new part
        Random number = new Random();
        int id = 0;

        ArrayList<Integer> partList = new ArrayList<Integer>();

        for (int i = 0; i < 1; i++) {
            id = 1000 + number.nextInt(1000);

            if (!partList.contains(id)) {
                partList.add(id);
            }
        }

            //get user inputs

            //get the new part name from user
            String name = newPartName.getText();

            //get inventory number from the user
            String inventoryStr = newPartInventory.getText();

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
            String priceStr = newPartPrice.getText();

            //Exception handling for non numbers
            double price = 0;

            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a price for the part");
                alert.showAndWait();
                return;
            }

            //get minimum inventory required from the user
            String minStr = newPartMin.getText();

            //Exception handling for non int
            int min = 0;

            try {
                min = Integer.parseInt(minStr);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a minimum number required in inventory");
                alert.showAndWait();
                return;
            }

            //get maximum inventory allowed from the user
            String maxStr = newPartMax.getText();

            //Exception handling for non int
            int max = 0;

            try {
                max = Integer.parseInt(maxStr);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a maximum number allowed in inventory");
                alert.showAndWait();
                return;
            }

            //check for simple errors
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Max must be greater than min");
                alert.showAndWait();
                return;
            }

            if (inventory > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Inventory must be less than max");
                alert.showAndWait();
                return;
            }

            if (min > inventory) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Inventory must be more than min");
                alert.showAndWait();
                return;
            }

            //if InHouse is selected, handle exceptions and get the MachineID
            if (inHouse.isSelected()) {
                String machineIdStr = newPartMachineId.getText();
                //Exception handling for non int
                int machineId = 0;
                try {
                    machineId = Integer.parseInt(machineIdStr);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Please enter a number for the machine ID");
                    alert.showAndWait();
                    return;
                }
                //add the new in-house part to inventory
                Inventory.addPart(new InHouse(id, name, price, inventory, min, max, machineId));
            }
            //otherwise, get the company name from the user
            else {
                //get the company name from the user
                String companyName = newPartMachineId.getText();

                //add the new outsourced part to inventory
                Inventory.addPart((new OutSourced(id, name, price, inventory, min, max, companyName)));
            }

            //go back to the main screen
            Stage primaryStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            primaryStage.close();

        }


    /**
     * Go back to the main screen if the user cancels adding a new part.
     *
      * @param actionEvent
     * @throws IOException
     */
    public void onCancelAddPart(ActionEvent actionEvent) throws IOException {
        //return to main screen
        Stage primaryStage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        primaryStage.close();
    }


}
