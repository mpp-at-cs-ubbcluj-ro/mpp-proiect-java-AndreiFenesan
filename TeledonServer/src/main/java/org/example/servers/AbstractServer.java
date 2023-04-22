package org.example.servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public abstract class AbstractServer {
    private final Properties serverProperties;
    protected ServerSocket serverSocket;

    public AbstractServer(Properties serverProperties) {
        this.serverProperties = serverProperties;
        this.serverSocket = null;
    }

    public void start() {
        int port = Integer.parseInt((String) serverProperties.get("port"));
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("Waiting for clients...");
                Socket client = serverSocket.accept();
                System.out.println("Accepted new client: " + client.getInetAddress());
                processNewClient(client);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            stopServer();
        }
    }

    private void stopServer() {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected abstract void processNewClient(Socket client);
}
