package org.example.servers;

import org.example.objectProtocol.ClientWorker;
import org.services.ITeledonService;

import java.net.Socket;
import java.util.Properties;

public class ConcurrentServer extends AbstractServer {
    ITeledonService service;

    public ConcurrentServer(Properties serverProperties, ITeledonService service) {
        super(serverProperties);
        this.service = service;
    }

    @Override
    protected void processNewClient(Socket client) {
        ClientWorker worker = new ClientWorker(client, this.service);
        Thread thread = new Thread(worker);
        thread.start();
    }
}
