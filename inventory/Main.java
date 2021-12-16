package inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application class.
 * FUTURE ENHANCEMENT - include a SQL database for Inventory to have data persistence
 * Javadocs are in folder titled javadoc in top level folder of project
 * @author Jason Philpy
 */
public class Main extends Application {
    /**
     * For tracking apps stage
     */
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Inventory Project");
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
        this.primaryStage = primaryStage;

    }

    /**
     * Access to primary stage
     * @return the primaryStage
     */
    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
