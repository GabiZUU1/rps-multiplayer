package org.example.client.view;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.client.controller.ClientController;

public class GameScreen extends BorderPane
{
    private static final int SLOTS = 8;
    private static final int COLUMNS = 4;

    // the names match PlayerSlot.Icon, so a move can be turned into an icon directly
    private static final String[] MOVES = {"ROCK", "PAPER", "SCISSORS"};

    private static final String TIMER = "-fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold;";
    private static final String TIMER_LOW = "-fx-text-fill: #f38ba8; -fx-font-size: 40px; -fx-font-weight: bold;";

    private static final String MOVE_BASE =
            "-fx-text-fill: #1e1e2e; -fx-font-weight: bold; -fx-font-size: 14px;"
                    + "-fx-background-radius: 8; -fx-padding: 6 16;";
    private static final String MOVE_NORMAL = MOVE_BASE + "-fx-background-color: #89b4fa; -fx-cursor: hand;";
    private static final String MOVE_CHOSEN = MOVE_BASE + "-fx-background-color: #a6e3a1; -fx-cursor: default;";
    private static final String MOVE_DIMMED = MOVE_BASE + "-fx-background-color: #89b4fa; -fx-opacity: 0.4; -fx-cursor: default;";

    private final ClientController clientController;
    private final PlayerSlot[] slots = new PlayerSlot[SLOTS];
    private final Button[] moveButtons = new Button[MOVES.length];

    public GameScreen(ClientController clientController)
    {
        this.clientController = clientController;

        setStyle("-fx-background-color: #1e1e2e;");
        setPadding(new Insets(20));

        // top: the timer, with a status line under it
        Label timer = new Label();
        timer.textProperty().bind(this.clientController.timeLeftProperty().asString());

        Label status = new Label();
        status.setStyle("-fx-text-fill: #a6adc8; -fx-font-size: 16px;");
        status.textProperty().bind(this.clientController.statusProperty());

        VBox top = new VBox(2, timer, status);
        top.setAlignment(Pos.CENTER);
        setTop(top);

        // the timer turns red for the last 3 seconds
        updateTimerStyle(timer, this.clientController.timeLeftProperty().get());
        this.clientController.timeLeftProperty().addListener((obs, oldValue, newValue) -> updateTimerStyle(timer, newValue.intValue()));

        // center: the same 8 slots as the lobby, the icon shows the pick
        GridPane grid = new GridPane();
        grid.setHgap(28);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        for(int i = 0; i < SLOTS; i++)
        {
            this.slots[i] = new PlayerSlot();
            grid.add(this.slots[i], i % COLUMNS, i / COLUMNS);
        }
        setCenter(grid);

        // bottom: one button per move
        HBox bottom = new HBox(16);
        bottom.setAlignment(Pos.CENTER);
        BorderPane.setMargin(bottom, new Insets(12, 0, 0, 0));

        for(int i = 0; i < MOVES.length; i++)
        {
            String move = MOVES[i];

            ImageView icon = new ImageView(iconFor(move).image());
            icon.setFitWidth(32);
            icon.setFitHeight(32);
            icon.setPreserveRatio(true);
            icon.setSmooth(true);

            Button button = new Button(move.charAt(0) + move.substring(1).toLowerCase(), icon);
            button.setContentDisplay(ContentDisplay.LEFT);
            button.setGraphicTextGap(10);
            button.setPrefWidth(150);
            button.setStyle(MOVE_NORMAL);
            button.setOnAction(e -> this.clientController.pick(move));

            this.moveButtons[i] = button;
            bottom.getChildren().add(button);
        }
        setBottom(bottom);

        // redraw whenever something the screen shows changes
        this.clientController.getPlayers().addListener((ListChangeListener<String>) change -> refresh());
        this.clientController.getPicks().addListener((MapChangeListener<String, String>) change -> refresh());
        this.clientController.myPickProperty().addListener((obs, oldValue, newValue) -> refresh());
        refresh();
    }

    private void refresh()
    {
        ObservableList<String> players = this.clientController.getPlayers();
        String me = this.clientController.getName();
        String myPick = this.clientController.myPickProperty().get();

        // slots: names, your own slot highlighted, and each icon is the pick (dots = not shown yet)
        for(int i = 0; i < SLOTS; i++)
        {
            String name = i < players.size() ? players.get(i) : "";
            boolean mine = name.equals(me);

            // you see your own pick right away, the others only after the server reveals them
            String move = (mine && !myPick.isEmpty()) ? myPick : this.clientController.getPicks().get(name);

            this.slots[i].setPlayer(name, mine);
            this.slots[i].setIcon(iconFor(move));
        }

        // buttons: all normal until you pick, then the chosen one is green and the others are dimmed
        for(int i = 0; i < MOVES.length; i++)
        {
            if(myPick.isEmpty())
            {
                this.moveButtons[i].setStyle(MOVE_NORMAL);
            }
            else if(MOVES[i].equals(myPick))
            {
                this.moveButtons[i].setStyle(MOVE_CHOSEN);
            }
            else
            {
                this.moveButtons[i].setStyle(MOVE_DIMMED);
            }
        }
    }

    private static void updateTimerStyle(Label timer, int seconds)
    {
        timer.setStyle(seconds > 0 && seconds <= 3 ? TIMER_LOW : TIMER);
    }

    // "ROCK" -> rock icon; empty, null or an unknown word -> the three dots
    private static PlayerSlot.Icon iconFor(String move)
    {
        if(move == null || move.isEmpty())
        {
            return PlayerSlot.Icon.DOTS;
        }

        try
        {
            return PlayerSlot.Icon.valueOf(move);
        }
        catch(IllegalArgumentException e)
        {
            return PlayerSlot.Icon.DOTS;
        }
    }
}
