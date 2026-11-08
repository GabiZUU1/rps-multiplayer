package org.example.client.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.util.Duration;
import org.example.client.App;
import org.example.client.model.Message;
import org.example.client.net.Client;
import org.example.client.net.Protocol;

import java.sql.Time;

public class ClientController
{
    private final StringProperty error = new SimpleStringProperty("");
    private final StringProperty status = new SimpleStringProperty("");
    private final StringProperty myPick = new SimpleStringProperty("");
    private final IntegerProperty timeLeft = new SimpleIntegerProperty(0);
    private final ObservableMap<String, String> picks = FXCollections.observableHashMap();
    private final ObservableList<String> players = FXCollections.observableArrayList();

    private final App parent;
    private Client client;
    private String name;
    private boolean joined = false;
    private Timeline countdown;

    public ClientController(App parent)
    {
        this.parent = parent;
    }

    public StringProperty errorProperty()
    {
        return this.error;
    }

    public StringProperty statusProperty()
    {
        return this.status;
    }

    public StringProperty myPickProperty()
    {
        return this.myPick;
    }

    public IntegerProperty timeLeftProperty()
    {
        return this.timeLeft;
    }

    public ObservableMap<String, String> getPicks()
    {
        return this.picks;
    }

    public String getName()
    {
        return this.name;
    }

    public ObservableList<String> getPlayers()
    {
        return this.players;
    }

    public void join(String name, String ip, String port)
    {
        this.error.set("");

        if(name.length() < 3)
        {
            this.error.set("Name must have at least 3 characters!");
            return;
        }
        if(name.contains(" ") || name.contains(","))
        {
            this.error.set("The name cannot contain spaces or commas!");
            return;
        }

        try
        {
            this.name = name;

            if(this.client != null)
            {
                this.client.close();
            }

            // if correct, connect and send join package
            this.client = new Client(ip, Integer.parseInt(port), line -> Platform.runLater(() -> this.handleLine(line)));
        }
        catch (Exception e)
        {
            this.error.set("Could not connect: " + e.getMessage());
        }
    }

    public void pick(String move)
    {
        if(!this.myPick.get().isEmpty())
        {
            return;
        }

        this.myPick.set(move);
        if(this.client != null)
        {
            this.client.send(Protocol.move(move));
        }
    }

    public void start()
    {
        if(this.client != null)
        {
            this.client.send(Protocol.start());
        }
    }

    private void startRound(int seconds)
    {
        if(this.countdown != null)
        {
            this.countdown.stop();
        }

        this.picks.clear();
        this.myPick.set("");
        this.status.set("Pick your move!");
        this.timeLeft.set(seconds);

        this.countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> this.timeLeft.set(this.timeLeft.get() - 1)));
        this.countdown.setCycleCount(seconds);
        this.countdown.play();
    }

    private void handleLine(String line)
    {
        Message m = Protocol.parse(line);
        switch(m.command())
        {
            case "HELLO" -> this.client.send(Protocol.join(this.name));
            case "ERROR" ->
            {
                this.error.set(this.errorText(m.arg()));
                if(!this.joined) this.client.close();
            }
            case "OK" ->
            {
                this.joined = true;
                this.parent.showLobby();
            }
            case "PLAYERS" -> this.players.setAll(m.arg().split(","));
            case "GAME_START" ->
            {
                this.error.set("");
                this.parent.showGame();
            }
            case "ROUND" -> this.startRound(Integer.parseInt(m.arg()));
        }
    }

    private String errorText(String code)
    {
        return switch (code)
        {
            case "ALREADY_EXISTS" -> "A player with this name already exists!";
            case "GAME_RUNNING" -> "An game is already running, please wait until it ends!";
            case "NOT_ENOUGH_PLAYERS" -> "You need at least 2 players to start an game!";
            default -> "Error, code: " + code;
        };
    }
}
