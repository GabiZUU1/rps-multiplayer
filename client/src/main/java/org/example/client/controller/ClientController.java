package org.example.client.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.client.App;
import org.example.client.model.Message;
import org.example.client.net.Client;
import org.example.client.net.Protocol;

public class ClientController
{
    private final StringProperty error = new SimpleStringProperty("");
    private final App parent;
    private final ObservableList<String> players = FXCollections.observableArrayList();
    private Client client;
    private String name;

    public ClientController(App parent)
    {
        this.parent = parent;
    }

    public StringProperty errorProperty()
    {
        return this.error;
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

    public void start()
    {
        if(this.client != null)
        {
            this.client.send(Protocol.start());
        }
    }

    public String getName()
    {
        return this.name;
    }

    public ObservableList<String> getPlayers()
    {
        return this.players;
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
                this.client.close();
            }
            case "OK" -> this.parent.showLobby();
            case "PLAYERS" -> this.players.setAll(m.arg().split(","));
        }
    }

    private String errorText(String code)
    {
        return switch (code)
        {
            case "ALREADY_EXISTS" -> "A player with this name already exists!";
            default -> "Error, code: " + code;
        };
    }
}
