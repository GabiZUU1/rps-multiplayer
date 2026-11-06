package org.example.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.client.controller.ClientController;
import org.example.client.controller.GameController;
import org.example.client.view.MenuScreen;

public class App extends Application
{
    private StackPane root;
    private GameController gameController;
    private ClientController clientController;

    @Override
    public void start(Stage stage)
    {
        this.gameController = new GameController(this);
        this.clientController = new ClientController(this);

        this.root = new StackPane();
        showMainMenu();

        stage.setTitle("Rock, Paper, Scissors — Main Menu");
        stage.setScene(new Scene(this.root, 640, 480));
        // stage.setMaximized(true);
        stage.show();
    }

    public void showMainMenu()
    {
        this.root.getChildren().setAll(new MenuScreen(this.gameController, this.clientController));
    }
}
