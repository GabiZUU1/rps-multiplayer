package org.example.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    // manages the transportation of the data
    private Socket s = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Client(String address, int port)
    {
        try
        {
            this.s = new Socket(address, port);
            this.in = new DataInputStream(s.getInputStream()); // to read data
            this.out = new DataOutputStream(s.getOutputStream()); // to send data

            this.out.writeUTF("Sunt client!");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
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
                throw new RuntimeException(e);
            }
        }
    }
}
