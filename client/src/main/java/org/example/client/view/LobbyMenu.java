package org.example.client.view;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.example.client.controller.ClientController;
import org.example.client.controller.GameController;

public class LobbyMenu extends BorderPane
{
    private static final int SLOTS = 8;
    private static final int COLUMNS = 4;

    private static final String BUTTON =
            "-fx-background-color: #89b4fa; -fx-text-fill: #1e1e2e; -fx-font-weight: bold;"
                    + "-fx-background-radius: 6; -fx-padding: 8 20; -fx-cursor: hand;";

    private final GameController gameController;
    private final ClientController clientController;
    private final PlayerSlot[] slots = new PlayerSlot[SLOTS];

    public LobbyMenu(GameController gameController, ClientController clientController)
    {
        this.gameController = gameController;
        this.clientController = clientController;

        setStyle("-fx-background-color: #1e1e2e;");
        setPadding(new Insets(20));

        // center: all 8 slots are always visible, 2 rows of 4
        GridPane grid = new GridPane();
        grid.setHgap(28);
        grid.setVgap(28);
        grid.setAlignment(Pos.CENTER);

        for(int i = 0; i < SLOTS; i++)
        {
            this.slots[i] = new PlayerSlot();
            grid.add(this.slots[i], i % COLUMNS, i / COLUMNS);
        }
        setCenter(grid);

        // bottom: Exit on the left, Start on the right
        Button exit = new Button("Exit");
        exit.setStyle(BUTTON.replace("#89b4fa", "#585b70").replace("#1e1e2e;", "white;"));
        exit.setPrefWidth(160);

        Button start = new Button("Start");
        start.setStyle(BUTTON);
        start.setPrefWidth(160);

        BorderPane bottom = new BorderPane();
        bottom.setLeft(exit);
        bottom.setRight(start);
        setBottom(bottom);

        exit.setOnAction(e -> this.gameController.exit());
        start.setOnAction(e -> this.clientController.start());    // everyone can press it, the server decides

        // fill the slots now, and again every time the list of players changes
        ObservableList<String> players = this.clientController.getPlayers();
        refresh(players);
        players.addListener((ListChangeListener<String>) change -> refresh(players));
    }

    private void refresh(ObservableList<String> players)
    {
        String me = this.clientController.getName();

        for(int i = 0; i < SLOTS; i++)
        {
            String player = i < players.size() ? players.get(i) : "";
            this.slots[i].setPlayer(player, player.equals(me));
        }
    }
}
