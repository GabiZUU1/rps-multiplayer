package org.example.client.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;

// one square with an icon and the player's name under it
// used by the lobby now, and it can show rock/paper/scissors in the game screen later
public class PlayerSlot extends VBox
{
    public enum Icon
    {
        DOTS("dots"),
        ROCK("rock"),
        PAPER("paper"),
        SCISSORS("scissors");

        private final String file;
        private Image image;    // loaded the first time it is needed

        Icon(String file)
        {
            this.file = file;
        }

        Image image()
        {
            if(this.image == null)
            {
                String path = "/icons/" + this.file + ".png";
                InputStream in = PlayerSlot.class.getResourceAsStream(path);
                if(in == null)
                {
                    throw new IllegalStateException("Missing icon: " + path + " (is it in src/main/resources/icons?)");
                }
                this.image = new Image(in, 128, 128, true, true);    // keep ratio, smooth scaling
            }
            return this.image;
        }
    }

    private static final String BOX_EMPTY =
            "-fx-background-color: #181825; -fx-background-radius: 12;"
                    + "-fx-border-color: #45475a; -fx-border-radius: 12; -fx-border-width: 2;";
    private static final String BOX_TAKEN =
            "-fx-background-color: #181825; -fx-background-radius: 12;"
                    + "-fx-border-color: #89b4fa; -fx-border-radius: 12; -fx-border-width: 2;";

    private static final String BOX_ME =
            "-fx-background-color: #1f2a28; -fx-background-radius: 12;"
                    + "-fx-border-color: #a6e3a1; -fx-border-radius: 12; -fx-border-width: 2;";

    private final StackPane box = new StackPane();
    private final ImageView iconView = new ImageView();
    private final Label nameLabel = new Label();

    public PlayerSlot()
    {
        super(8);
        setAlignment(Pos.TOP_CENTER);
        setPrefWidth(110);
        setMinWidth(110);

        this.iconView.setFitWidth(64);
        this.iconView.setFitHeight(64);
        this.iconView.setPreserveRatio(true);
        this.iconView.setSmooth(true);

        this.box.setPrefSize(100, 100);
        this.box.setMinSize(100, 100);
        this.box.setMaxSize(100, 100);
        this.box.getChildren().add(this.iconView);

        this.nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        this.nameLabel.setMaxWidth(110);
        this.nameLabel.setMinHeight(22);    // an empty name still takes up its line, so the grid doesn't jump
        this.nameLabel.setAlignment(Pos.CENTER);

        getChildren().addAll(this.box, this.nameLabel);

        setIcon(Icon.DOTS);
        setPlayer("");
    }

    public void setPlayer(String name)
    {
        setPlayer(name, false);
    }

    // me = true highlights the slot of the player who is using this client
    public void setPlayer(String name, boolean me)
    {
        boolean taken = name != null && !name.isEmpty();

        String style = BOX_EMPTY;
        if(taken)
        {
            style = me ? BOX_ME : BOX_TAKEN;
        }

        this.nameLabel.setText(taken ? name : "");
        this.box.setStyle(style);
        this.iconView.setOpacity(taken ? 1.0 : 0.45);
    }

    public void setIcon(Icon icon)
    {
        this.iconView.setImage(icon.image());
    }
}
