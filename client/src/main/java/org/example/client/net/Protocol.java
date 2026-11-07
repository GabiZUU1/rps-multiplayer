package org.example.client.net;

import org.example.client.model.Message;

public class Protocol
{
    public Protocol(){}

    public static Message parse(String line)
    {
        String[] p = line.trim().split(" ", 2);
        return new Message(p[0], p.length > 1 ? p[1] : "");
    }

    public static String join(String name)
    {
        return "JOIN " + name;
    }

    public static String start()
    {
        return "START";
    }
}
