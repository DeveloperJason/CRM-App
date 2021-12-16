package inventory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

/**
 * VC to add or modify parts in the inventory
 * @author Jason Philpy
 */
public class AddPartViewController extends ViewController {

    /**
     * Setting whether the part is in-house or outsourced
     */
    private boolean isInhouse = true;

    /**
     * Index of part in array used for modifying existing part
     */
    private int partIndex;

    /**
     * ID field (auto filled)
     */
    @FXML
    private TextField partId;

    /**
     * Name field
     */
    @FXML
    private TextField partName;

    /**
     * Max field
     */
    @FXML
    private TextField partMax;

    /**
     * Min field
     */
    @FXML
    private TextField partMin;

    /**
     * Price field
     */
    @FXML
    private TextField partPrice;

    /**
     * Inventory field
     */
    @FXML
    private TextField partInv;

    /**
     * Company name field
     */
    @FXML
    private TextField companyName;

    /**
     * Label for company name field
     */
    @FXML
    private Label companyNameLabel;

    /**
     * Machine ID field
     */
    @FXML
    private TextField machineId;

    /**
     * Label for machine ID field
     */
    @FXML
    private Label machineIdLabel;

    /**
     * Group toggle between in-house and outsourced
     */
    @FXML
    private ToggleGroup partType;

    /**
     * In-house selector
     */
    @FXML
    private RadioButton inHouseRadio;

    /**
     * Outsourced selector
     */
    @FXML
    private RadioButton outsourcedRadio;

    /**
     * Save/update part button
     */
    @FXML
    private Button save;

    /**
     * Main title (switches between Add Part and Modify Part)
     */
    @FXML
    private Label mainTitle;

    /**
     * Error field to display errors
     */
    @FXML
    private Label errorField;

    /**
     * Set up part type switching, getting part data to modify if necessary, and save button functionality
     */
    public void initialize() {
        super.initialize();
        if (Main.getPrimaryStage().getUserData() != null) {
            part = (Part) Main.getPrimaryStage().getUserData();
        }
        partType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton selectedBtn = (RadioButton) partType.getSelectedToggle();
                if (selectedBtn != null) {
                    if (selectedBtn.equals(inHouseRadio)) {
                        isInhouse = true;
                        companyNameLabel.setVisible(false);
                        companyName.setVisible(false);
                        machineIdLabel.setVisible(true);
                        machineId.setVisible(true);
                    } else {
                        isInhouse = false;
                        machineIdLabel.setVisible(false);
                        machineId.setVisible(false);
                        companyNameLabel.setVisible(true);
                        companyName.setVisible(true);
                    }
                }
            }
        });
        if (part != null) {
            mainTitle.setText("Modify Part");
            save.setText("Update");
            partIndex = Inventory.getAllParts().indexOf(part);
            partId.setText(String.valueOf(part.getId()));
            partName.setText(part.getName());
            partInv.setText(String.valueOf(part.getStock()));
            partPrice.setText(String.valueOf(part.getPrice()));
            partMax.setText(String.valueOf(part.getMax()));
            partMin.setText(String.valueOf(part.getMin()));
            if (part instanceof InHouse) {
                machineId.setText(String.valueOf(((InHouse) part).getMachineId()));
            } else {
                companyName.setText(((Outsourced) part).getCompanyName());
                outsourcedRadio.setSelected(true);
            }
        }
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (validateData()) {
                    int id = 0;
                    if (part == null) {
                        id = Inventory.getPartId();
                    } else {
                        id = Integer.parseInt(partId.getText());
                    }
                    if (isInhouse) {
                        InHouse inHouse = new InHouse(id, partName.getText(), Double.parseDouble(partPrice.getText()), Integer.parseInt(partInv.getText()), Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), Integer.parseInt(machineId.getText()));
                        savePart(inHouse);
                    } else {
                        Outsourced outsourced = new Outsourced(id, partName.getText(), Double.parseDouble(partPrice.getText()), Integer.parseInt(partInv.getText()), Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), companyName.getText());
                        savePart(outsourced);
                    }
                    try {
                        exitToMenu();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Checks if the fields are of the correct type and if min, max, and inventory are logically consistent
     * @return success value of field data
     */
    private boolean validateData() {
        errorField.setText("");
        String errorText = "";
        boolean isValid = true;
        boolean inventoryIsNumber = true;
        int inv = 0;
        try {
            inv = Integer.parseInt(partInv.getText());
        } catch (NumberFormatException e) {
            errorText = "\nError: Inventory must be a number.";
            isValid = false;
            inventoryIsNumber = false;
        }
        try {
            Double.parseDouble(partPrice.getText());
        } catch (NumberFormatException e) {
            errorText += "\nError: Price must be a number.";
            isValid = false;
        }
        boolean minMaxAreNumbers = true;
        int min = 0;
        int max = 0;
        try {
            max = Integer.parseInt(partMax.getText());
        } catch (NumberFormatException e) {
            errorText += "\nError: Max must be a number.";
            isValid = false;
            minMaxAreNumbers = false;
        }
        try {
            min = Integer.parseInt(partMin.getText());
        } catch (NumberFormatException e) {
            errorText += "\nError: Min must be a number.";
            isValid = false;
            minMaxAreNumbers = false;
        }
        if (isInhouse) {
            try {
                Integer.parseInt(machineId.getText());
            } catch (NumberFormatException e) {
                errorText += "\nError: Machine ID must be a number.";
                isValid = false;
            }
        }
        if (minMaxAreNumbers) {
            if (min >= max) {
                errorText += "\nError: Max must be greater than Min.";
                isValid = false;
            } else if (inventoryIsNumber) {
                if (inv < min || inv > max) {
                    errorText += "\nError: Inventory must be between Min and Max.";
                    isValid = false;
                }
            }
        }
        if (!isValid) {
            errorField.setText(errorText);
        }
        return isValid;
    }

    /**
     * Saves or updates the part to inventory
     * @param part to save to inventory
     */
    private void savePart(Part part) {
        if (this.part != null) {
            Inventory.updatePart(partIndex, part);
        } else {
            Inventory.addPart(part);
        }
    }
}
