package inventory;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * VC for controlling a part table view
 * @author Jason Philpy
 */
public class PartTableViewController extends ViewController {

    /**
     * Tableview of inventory parts
     */
    @FXML
    protected TableView partTableView;

    /**
     * Part table view ID column
     */
    @FXML
    private TableColumn partIdCol;

    /**
     * Part table view name column
     */
    @FXML
    private TableColumn partNameCol;

    /**
     * Part table view inventory column
     */
    @FXML
    private TableColumn partInvCol;

    /**
     * Part table view price column
     */
    @FXML
    private TableColumn partCostCol;

    /**
     * Search field for parts
     */
    @FXML
    private TextField searchParts;

    /**
     * Error field to display part errors
     */
    @FXML
    protected Label partErrorField;

    /**
     * Set up tableview and search functionality
     */
    public void initialize() {
        super.initialize();
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());

        searchParts.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    partErrorField.setText("");
                    if (searchParts.getText().equals("")) {
                        partTableView.setItems(Inventory.getAllParts());
                    } else {
                        try {
                            int id = Integer.parseInt(searchParts.getText());
                            Part foundPart = Inventory.lookupPart(id);
                            if (foundPart == null) {
                                if (Inventory.lookupPart(searchParts.getText()).size() == 0) {
                                    partErrorField.setText("No results found");
                                } else {
                                    partTableView.setItems(Inventory.lookupPart(searchParts.getText()));
                                }
                            } else {
                                partTableView.getSelectionModel().select(foundPart);
                            }
                        } catch (NumberFormatException e) {
                            if (Inventory.lookupPart(searchParts.getText()).size() == 0) {
                                partErrorField.setText("No results found");
                            } else {
                                partTableView.setItems(Inventory.lookupPart(searchParts.getText()));
                            }

                        }
                    }
                }
            }
        });
    }
}
