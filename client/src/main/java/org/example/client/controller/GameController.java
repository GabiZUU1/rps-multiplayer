package org.example.client.controller;

import javafx.application.Application;
import javafx.application.Platform;
import org.example.client.App;

public class GameController
{
    private final App parent;

    public GameController(App parent)
    {
        this.parent = parent;
    }

    public void join(String name, String ip, int port) throws Exception
    {
        if(name.length() < 3)
        {
            throw new Exception("Name must have at least 3 characters!");
        }
    }

    public void exit()
    {
        Platform.exit();
    }
}
