package org.example.client.net;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client
{
    // manages the transportation of the data
    private Socket s = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    public Client(String address, int port) throws Exception
    {
        try
        {
            this.s = new Socket(address, port);
            this.in = new BufferedReader(new InputStreamReader(this.s.getInputStream(), StandardCharsets.UTF_8)); // to read data
            this.out = new PrintWriter(new OutputStreamWriter(this.s.getOutputStream(), StandardCharsets.UTF_8), true); // to send data

            // make a background thread for reading messages from the server, such as the HELLO handshake
        }
        catch (IOException e)
        {
            throw new Exception(e);
        }
        finally
        {
            try
            {
                if(this.out != null) this.out.close();
                if(this.in != null) this.in.close();
                if(this.s != null) this.s.close();
            }
            catch (IOException e)
            {
                throw new Exception(e);
            }
        }
    }

    public void sendMessage(String message)
    {
        this.out.print(message + "\n");
        this.out.flush();
    }
}
