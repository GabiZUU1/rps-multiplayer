package org.example.client.net;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class Client
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private final Consumer<String> onLine;

    public Client(String address, int port, Consumer<String> onLine) throws Exception
    {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), 3000);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));
        this.onLine = onLine;

        this.println("Connected to server!");
        this.startListening();
    }

    public synchronized void send(String line)
    {
        this.out.print(line + "\n");
        this.out.flush();
    }

    public void close()
    {
        try
        {
            this.socket.close();
        }
        catch (IOException ignored) {}
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
                    this.println("Server: " + line);

                    this.onLine.accept(line);
                }
            }
            catch (IOException e)
            {
                this.println(e.getMessage());
            }
            finally
            {
                this.println("Disconnected from server!");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void println(String x)
    {
        DateTimeFormatter frm = DateTimeFormatter.ofPattern("HH:mm:ss");

        System.out.print("[" + LocalTime.now().format(frm) + "] ");
        System.out.println(x);
    }
}
