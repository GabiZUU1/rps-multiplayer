package org.example.client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.example.client.controller.GameController;

public class MenuScreen extends BorderPane
{
    private final GameController controller;

    private static final String TEXT = "-fx-text-fill: white;";
    private static final String FIELD =
            "-fx-background-color: #313244; -fx-text-fill: white;"
                    + "-fx-prompt-text-fill: #7f849c; -fx-background-radius: 6; -fx-padding: 7 10;";
    private static final String BUTTON =
            "-fx-background-color: #89b4fa; -fx-text-fill: #1e1e2e; -fx-font-weight: bold;"
                    + "-fx-background-radius: 6; -fx-padding: 8 20; -fx-cursor: hand;";

    public MenuScreen(GameController controller) {
        this.controller = controller;

        setStyle("-fx-background-color: #1e1e2e;");
        setPadding(new Insets(20));

        // top
        Label title = new Label("Rock Paper Scissors");
        title.setStyle(TEXT + "-fx-font-size: 52px; -fx-font-weight: bold;");

        Label statsTitle = new Label("Network");
        statsTitle.setStyle(TEXT + "-fx-font-weight: bold;");

        TextField ipField = new TextField("localhost");
        ipField.setEditable(false);
        ipField.setStyle(FIELD);
        ipField.setPrefColumnCount(10);

        TextField portField = new TextField("5000");
        portField.setStyle(FIELD);
        portField.setPrefColumnCount(10);

        Label ipLabel = new Label("IP:");
        Label portLabel = new Label("Port:");
        ipLabel.setStyle(TEXT);
        portLabel.setStyle(TEXT);

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(8);
        statsGrid.setVgap(8);
        statsGrid.addRow(0, ipLabel, ipField);
        statsGrid.addRow(1, portLabel, portField);

        VBox stats = new VBox(10, statsTitle, statsGrid);
        stats.setPadding(new Insets(12));
        stats.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        stats.setStyle("-fx-background-color: #181825; -fx-background-radius: 8;"
                + "-fx-border-color: #45475a; -fx-border-radius: 8;");

        StackPane top = new StackPane(title, stats);
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane.setMargin(title, new Insets(30, 0, 0, 0));
        StackPane.setAlignment(stats, Pos.TOP_RIGHT);
        setTop(top);

        // center
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle(TEXT);
        TextField nameField = new TextField();
        nameField.setPromptText("Your name");
        nameField.setStyle(FIELD);
        HBox.setHgrow(nameField, Priority.ALWAYS);

        HBox nameRow = new HBox(10, nameLabel, nameField);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #f38ba8;");

        Button join = new Button("Join");
        join.setStyle(BUTTON);
        join.setPrefWidth(160);

        Button exit = new Button("Exit");
        exit.setStyle(BUTTON.replace("#89b4fa", "#585b70").replace("#1e1e2e;", "white;"));
        exit.setPrefWidth(160);

        exit.setOnAction(e -> this.controller.exit());

        VBox center = new VBox(18, nameRow, join, exit, errorLabel);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(320);
        setCenter(center);
    }
}