package org.example.client.net;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String address, int port) throws Exception
    {
        this.socket = new Socket(address, port);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));

        System.out.println("Connected to server!");
        this.startListening();
    }

    private void startListening()
    {
        Thread t = new Thread(() ->
        {
            try
            {
                String line;
                while((line = this.in.readLine()) != null)
                {
                    System.out.println("Server: " + line);

                    
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                System.out.println("Disconnected from server!");
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
