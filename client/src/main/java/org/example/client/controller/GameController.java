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

    public void join(String name, String portText) throws Exception
    {
        if(name.length() < 3)
        {
            throw new Exception("Name must have at least 3 characters!");
        }

        try
        {
            int port = Integer.parseInt(portText);
            if(port < 1 || port > 65535)
            {
                throw new Exception("The port must be a number between 1 and 65535.");
            }

            // if correct, connect and send join package
            this.client = new Client("127.0.0.1", 7777);
        }
        catch (NumberFormatException e)
        {
            throw new Exception(e.getMessage());
        }
    }

    public void exit()
    {
        Platform.exit();
    }
}
