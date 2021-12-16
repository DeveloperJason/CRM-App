package inventory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Contains basic view controller functionality for the others to inherit
 * @author Jason Philpy
 */
public class ViewController {

    /**
     * To communicate part data when modifying parts
     */
    protected Part part;

    /**
     * To communicate product data when modifying products
     */
    protected Product product;

    @FXML
    private Button exit;

    /**
     * Loads a new scene
     * @param fxml UI file to load
     * @param width window width
     * @param height window height
     * @throws IOException if cannot load scene
     */
    protected void loadScene(String fxml, int width, int height) throws IOException {
        Main.getPrimaryStage().close();
        if (part != null) {
            Main.getPrimaryStage().setUserData(part);
        }
        if (product != null) {
            Main.getPrimaryStage().setUserData(product);
        }
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Main.getPrimaryStage().setScene(new Scene(root, width, height));
        Main.getPrimaryStage().show();
    }

    /**
     * Exits current scene or program (if on main menu)
     * @throws IOException exception if couldn't load
     */
    protected void exitToMenu() throws IOException {
        if (this instanceof MainMenuController) {
            System.exit(0);
        } else {
            Main.getPrimaryStage().setUserData(null);
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Main.getPrimaryStage().setScene(new Scene(root, 1200, 600));

        }
    }

    /**
     * Sets exit button action
     */
    public void initialize() {
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    exitToMenu();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
