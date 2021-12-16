package inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

/**
 * VC to add or modify products in the inventory
 * @author Jason Philpy
 */
public class AddProductViewController extends PartTableViewController {

    /**
     * Index of products in array used for modifying existing product
     */
    private int prodIndex;

    /**
     * List of parts to be added or updated
     */
    private ObservableList<Part> partsToAdd = FXCollections.observableArrayList();

    /**
     * ID field (auto filled)
     */
    @FXML
    private TextField productId;

    /**
     * Name field
     */
    @FXML
    private TextField productName;

    /**
     * Max field
     */
    @FXML
    private TextField productMax;

    /**
     * Min field
     */
    @FXML
    private TextField productMin;

    /**
     * Price field
     */
    @FXML
    private TextField productPrice;

    /**
     * Inventory field
     */
    @FXML
    private TextField productInv;

    /**
     * Save/update part button
     */
    @FXML
    private Button save;

    /**
     * Add inventory part to current product button
     */
    @FXML
    private Button addPart;

    /**
     * Remove part from current product button
     */
    @FXML
    private Button removePart;

    /**
     * Main title (switches between Add Product and Modify Product)
     */
    @FXML
    private Label mainTitle;

    /**
     * Tableview of parts of current product
     */
    @FXML
    private TableView assocPartTableView;

    /**
     * Current parts table view id column
     */
    @FXML
    private TableColumn aPartIdCol;

    /**
     * Current parts table view name column
     */
    @FXML
    private TableColumn aPartNameCol;

    /**
     * Current parts table view inventory column
     */
    @FXML
    private TableColumn aPartInvCol;

    /**
     * Current parts table view price column
     */
    @FXML
    private TableColumn aPartCostCol;

    /**
     * Error field to display errors
     */
    @FXML
    private Label errorField;

    /**
     * Set up tableview, getting product data to modify if necessary, add/remove part and save button functionality
     */
    @Override
    public void initialize() {
        super.initialize();
        aPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        aPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        aPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        aPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        if (Main.getPrimaryStage().getUserData() != null) {
            product = (Product) Main.getPrimaryStage().getUserData();
            mainTitle.setText("Modify Product");
            save.setText("Update");
            prodIndex = Inventory.getAllProducts().indexOf(product);
            productId.setText(String.valueOf(product.getId()));
            productName.setText(product.getName());
            productInv.setText(String.valueOf(product.getStock()));
            productPrice.setText(String.valueOf(product.getPrice()));
            productMax.setText(String.valueOf(product.getMax()));
            productMin.setText(String.valueOf(product.getMin()));
            partsToAdd.addAll(product.getAllAssociatedParts());
        }
        assocPartTableView.setItems(partsToAdd);

        addPart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                partErrorField.setText("");
                if (partTableView.getSelectionModel().getSelectedItem() != null) {
                    Part part = (Part) partTableView.getSelectionModel().getSelectedItem();
                    partsToAdd.add(part);
                } else {
                    partErrorField.setText("No part selected to add");
                }
            }
        });
        removePart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                errorField.setText("");
                if (assocPartTableView.getSelectionModel().getSelectedItem() != null) {
                    Part part = (Part) assocPartTableView.getSelectionModel().getSelectedItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove part \"" + part.getName() + "\"?",ButtonType.CANCEL, ButtonType.YES);

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("dialog.css").toExternalForm());
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        partsToAdd.remove(part);
                    } else if (alert.getResult() == ButtonType.CANCEL) {
                        errorField.setText("Delete canceled");
                    }
                } else {
                    errorField.setText("No part selected to delete");
                }
            }
        });
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (validateData()) {
                    int id = 0;
                    if (product == null) {
                        id = Inventory.getProductId();
                    } else {
                        id = Integer.parseInt(productId.getText());
                    }
                    Product productToSave = new Product(id, productName.getText(), Double.parseDouble(productPrice.getText()), Integer.parseInt(productInv.getText()), Integer.parseInt(productMin.getText()), Integer.parseInt(productMax.getText()));
                    productToSave.setAssociatedParts(partsToAdd);
                    saveProduct(productToSave);
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
            inv = Integer.parseInt(productInv.getText());
        } catch (NumberFormatException e) {
            errorText = "\nError: Inventory must be a number.";
            isValid = false;
            inventoryIsNumber = false;
        }
        try {
            Double.parseDouble(productPrice.getText());
        } catch (NumberFormatException e) {
            errorText += "\nError: Price must be a number.";
            isValid = false;
        }
        boolean minMaxAreNumbers = true;
        int min = 0;
        int max = 0;
        try {
            max = Integer.parseInt(productMax.getText());
        } catch (NumberFormatException e) {
            errorText += "\nError: Max must be a number.";
            isValid = false;
            minMaxAreNumbers = false;
        }
        try {
            min = Integer.parseInt(productMin.getText());
        } catch (NumberFormatException e) {
            errorText += "\nError: Min must be a number.";
            isValid = false;
            minMaxAreNumbers = false;
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
     * Saves or updates the product to inventory
     * @param product to save to inventory
     */
    private void saveProduct(Product product) {
        if (this.product != null) {
            Inventory.updateProduct(prodIndex, product);
        } else {
            Inventory.addProduct(product);
        }
    }
}
