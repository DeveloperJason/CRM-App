package inventory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

/**
 * VC for the main menu
 * @author Jason Philpy
 */
public class MainMenuController extends PartTableViewController {

    /**
     * Product table view ID column
     */
    @FXML
    private TableColumn productIdCol;

    /**
     * Product table view name column
     */
    @FXML
    private TableColumn productNameCol;

    /**
     * Product table view inventory column
     */
    @FXML
    private TableColumn productInvCol;

    /**
     * Product table view price column
     */
    @FXML
    private TableColumn productCostCol;

    /**
     * Tableview of inventory products
     */
    @FXML
    private TableView productTableView;

    /**
     * Open the scene for adding a part button
     */
    @FXML
    private Button addPart;

    /**
     * Open the scene for modifying a part button
     */
    @FXML
    private Button modifyPart;

    /**
     * Delete part from inventory button
     */
    @FXML
    private Button deletePart;

    /**
     * Open the scene for adding a product button
     */
    @FXML
    private Button addProduct;

    /**
     * Open the scene for modifying a product button
     */
    @FXML
    private Button modifyProduct;

    /**
     * Delete product from inventory button
     */
    @FXML
    private Button deleteProduct;

    /**
     * Search field for products
     */
    @FXML
    private TextField searchProducts;

    /**
     * Error field to display product errors
     */
    @FXML
    private Label productErrorField;

    /**
     * Set up tableview, search functionality, add/modify/delete part/product functionality
     */
    public void initialize() {
        super.initialize();
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTableView.setItems(Inventory.getAllProducts());

        searchProducts.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    productErrorField.setText("");
                    if (searchProducts.getText().equals("")) {
                        productTableView.setItems(Inventory.getAllProducts());
                    } else {
                        try {
                            int id = Integer.parseInt(searchProducts.getText());
                            Product foundProduct = Inventory.lookupProduct(id);
                            if (foundProduct == null) {
                                if (Inventory.lookupProduct(searchProducts.getText()).size() == 0) {
                                    productErrorField.setText("No results found");
                                } else {
                                    productTableView.setItems(Inventory.lookupProduct(searchProducts.getText()));
                                }
                            } else {
                                productTableView.getSelectionModel().select(foundProduct);
                            }
                        } catch (NumberFormatException e) {
                            if (Inventory.lookupProduct(searchProducts.getText()).size() == 0) {
                                productErrorField.setText("No results found");
                            } else {
                                productTableView.setItems(Inventory.lookupProduct(searchProducts.getText()));
                            }
                        }
                    }
                }
            }
        });
        addPart.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                try {
                    product = null;
                    part = null;
                    loadScene("addPart.fxml", 600, 600);
                } catch (IOException e) {
                    partErrorField.setText("Error loading scene");
                }
            }
        });

        modifyPart.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {

                if (partTableView.getSelectionModel().getSelectedItem() != null) {
                    product = null;
                    part = (Part) partTableView.getSelectionModel().getSelectedItem();
                    try {
                        loadScene("addPart.fxml", 600, 600);
                    } catch (IOException e) {
                        partErrorField.setText("Error loading scene");
                    }
                } else {
                    partErrorField.setText("No part selected to modify");
                }
            }
        });
        deletePart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                partErrorField.setText("");
                if (partTableView.getSelectionModel().getSelectedItem() != null) {
                    Part partToDelete = (Part) partTableView.getSelectionModel().getSelectedItem();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete part \"" + partToDelete.getName() + "\"?",ButtonType.CANCEL, ButtonType.YES);

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("dialog.css").toExternalForm());
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        Inventory.deletePart(partToDelete);
                    } else if (alert.getResult() == ButtonType.CANCEL) {
                        partErrorField.setText("Delete canceled");
                    }
                } else {
                    partErrorField.setText("No part selected to delete");
                }

            }
        });

        addProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                try {
                    product = null;
                    part = null;
                    loadScene("addProduct.fxml", 900, 600);
                } catch (IOException e) {
                    partErrorField.setText("Error loading scene");
                }
            }
        });
        modifyProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {

                if (productTableView.getSelectionModel().getSelectedItem() != null) {
                    part = null;
                    product = (Product) productTableView.getSelectionModel().getSelectedItem();
                    try {
                        loadScene("addProduct.fxml", 900, 600);
                    } catch (IOException e) {
                        productErrorField.setText("Error loading scene");
                    }
                } else {
                    productErrorField.setText("No product selected to modify");
                }
            }
        });
        deleteProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                productErrorField.setText("");
                if (productTableView.getSelectionModel().getSelectedItem() != null) {
                    Product prodToDelete = (Product) productTableView.getSelectionModel().getSelectedItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete product \"" + prodToDelete.getName() + "\"?",ButtonType.CANCEL, ButtonType.YES);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("dialog.css").toExternalForm());
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        if (prodToDelete.getAllAssociatedParts().size() > 0) {
                            productErrorField.setText("Could not delete, product has parts");
                        } else {
                            Inventory.deleteProduct(prodToDelete);
                        }
                    } else if (alert.getResult() == ButtonType.CANCEL) {
                        productErrorField.setText("Delete canceled");
                    }
                } else {
                    productErrorField.setText("No product selected to delete");
                }

            }
        });
    }

}
