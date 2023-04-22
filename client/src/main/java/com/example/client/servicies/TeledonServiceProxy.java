package com.example.client.servicies;

import com.example.client.Requests.LoginRequest;
import com.example.client.Requests.Request;
import com.example.client.Responses.Response;
import com.example.client.Responses.ResponseOk;
import com.example.client.model.Donor;
import com.example.client.model.dtos.CharityCaseDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TeledonServiceProxy implements ITeledonService {
    private Socket serverSocket;
    private final int port;
    private final String host;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private BlockingQueue<Response> responseBlockingQueue;
    private volatile boolean finished;

    public TeledonServiceProxy(int port, String host) {
        this.port = port;
        this.host = host;
        this.serverSocket = null;
        this.responseBlockingQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public Iterable<CharityCaseDto> getAllCharityCases() {
        return null;
    }

    @Override
    public void addNewDonation(Long charityCaseId, String name, String emailAddress, String phoneNumber, Double donationAmount) {

    }

    @Override
    public Iterable<Donor> getDonorsWithNameContaining(String containsInName) {
        return null;
    }

    @Override
    public boolean authenticateVolunteer(String username, String password) {
        if (serverSocket == null) {
            initialiseConnection();
        }
        sendRequest(new LoginRequest(username, password));
        Response response = readResponse();
        if (response instanceof ResponseOk) {
            return true;
        }
        return false;
    }

    private void sendRequest(Request request) {
        try {
            System.out.println("Sending request: " + request.getClass());
            this.outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error in sending request " + e.getMessage());
        }
        System.out.println("Request sent to the server");
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = responseBlockingQueue.take();
        } catch (InterruptedException e) {
            System.out.println("Error in taking response from queue: " + e.getMessage());
        }
        return response;
    }

    private void initialiseConnection() {
        try {
            System.out.println("Initialising connection");
            this.serverSocket = new Socket(this.host, this.port);
            System.out.println("Connected");
            this.outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            outputStream.flush();
            this.inputStream = new ObjectInputStream(serverSocket.getInputStream());
            this.finished = false;
            System.out.println("Starting response reader thread");
            Thread readThread = new Thread(new ResponseReader());
            readThread.start();
            System.out.println("Thread started");

        } catch (IOException e) {
            System.out.println("Error in init connection: " + e.getMessage());
        }
        System.out.println("Connection initialised OK");
    }

    private class ResponseReader implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread started. Now in run method");
            while (!finished) {
                try {
                    Object response = inputStream.readObject();
                    System.out.println("Response received");
                    responseBlockingQueue.put((Response) response);
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    System.out.println("Error in response reader " + e.getMessage());
                }
            }
        }
    }
}
