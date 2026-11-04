package org.example.client.controller;

import javafx.application.Platform;
import org.example.client.App;
import org.example.client.net.Client;

public class GameController
{
    private final App parent;
    private Client client;

    public GameController(App parent)
    {
        this.parent = parent;
    }

    public void exit()
    {
        Platform.exit();
    }
}
