package org.example.client.controller;

import org.example.client.App;
import org.example.client.net.Client;

public class ClientController
{
    private final App parent;
    private Client client;

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
            // if correct, connect and send join package
            this.client = new Client(ip, Integer.parseInt(port));
        }
        catch (NumberFormatException e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
