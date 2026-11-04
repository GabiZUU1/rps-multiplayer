package org.example.client.controller;

import javafx.application.Platform;
import org.example.client.App;
import org.example.client.model.Message;
import org.example.client.net.Client;
import org.example.client.net.Protocol;

public class ClientController
{
    private final App parent;
    private Client client;

    private String name;

    public ClientController(App parent)
    {
        this.parent = parent;
    }

    public void join(String name, String ip, String port) throws Exception
    {
        if(name.length() < 3)
        {
            throw new Exception("Name must have at least 3 characters!");
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
        catch (NumberFormatException e)
        {
            throw new Exception(e.getMessage());
        }
    }

    private void handleLine(String line)
    {
        Message m = Protocol.parse(line);
        switch(m.command())
        {
            case "HELLO" -> this.client.send(Protocol.join(this.name));
        }
    }
}
