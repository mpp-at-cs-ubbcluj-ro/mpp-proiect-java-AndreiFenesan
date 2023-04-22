package org.example.objectProtocol;

import org.example.objectProtocol.requests.*;
import org.example.objectProtocol.responses.*;
import org.models.Donor;
import org.models.dtos.CharityCaseDto;
import org.models.dtos.DonorDtoBuilder;
import org.services.ITeledonService;
import org.services.Observable;
import org.services.Observer;
import org.services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TeledonServiceProxy implements ITeledonService, Observable {
    private Socket serverSocket;
    private final int port;
    private final String host;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private BlockingQueue<Response> responseBlockingQueue;
    private volatile boolean finished;
    private List<Observer> observers;

    public TeledonServiceProxy(int port, String host) {
        this.port = port;
        this.host = host;
        this.serverSocket = null;
        this.responseBlockingQueue = new LinkedBlockingQueue<>();
        this.observers = new ArrayList<>();
    }

    @Override
    public Iterable<CharityCaseDto> getAllCharityCases() {
        System.out.println("Sending getAllCharityCases request");
        sendRequest(new GetAllCharityCasesRequest());
        System.out.println("Sent getAllCharityCases request");
        Response response = readResponse();
        System.out.println("Response is " + response.getClass());
        if (response instanceof GetAllCharityCasesResponse) {
            return ((GetAllCharityCasesResponse) response).getCharityCasesDto();
        } else {
            return null;
        }
    }

    @Override
    public void addNewDonation(Long charityCaseId, String name, String emailAddress, String phoneNumber, Double donationAmount) {
        System.out.println("Sending add new donation request");
        sendRequest(new NewDonationRequest(charityCaseId, name, emailAddress, phoneNumber, donationAmount));
        Response response = readResponse();
        System.out.println("got response of type " + response.getClass());
        if (response instanceof ResponseError) {
            throw new ServiceException(((ResponseError) response).getError());
        }
    }

    @Override
    public Iterable<Donor> getDonorsWithNameContaining(String containsInName) {
        System.out.println("Sending getDonorsWithNameLike request");
        sendRequest(new GetDonorWithNameLikeRequest(containsInName));
        System.out.println("Sent getDonorsWithNameLike request");
        Response response = readResponse();
        if (response instanceof GetDonorWithNameLikeResponse) {
            System.out.println("Response of type: " + response.getClass());
            DonorDtoBuilder donorDtoBuilder = new DonorDtoBuilder();
            List<Donor> donors = new ArrayList<>();
            var donorsDto = ((GetDonorWithNameLikeResponse) response).getDonors();
            donorsDto.forEach(donorDto -> {
                donors.add(donorDtoBuilder.buildDonorFromDto(donorDto));
            });
            return donors;
        }
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

    @Override
    public void logout() {
        System.out.println("Logging out");
        this.finished = true;
        sendRequest(new LogOutRequest());
        try{
            this.inputStream.close();
            this.outputStream.close();
        } catch (IOException e) {
            System.out.println("Error in closing buffers");
        }
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

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyAllObservers() {
        System.out.println("Notifying all observers");
        this.observers.forEach(Observer::update);
    }

    private void handleUpdate() {
        System.out.println("Handeling update response");
        this.notifyAllObservers();
    }

    private class ResponseReader implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread started. Now in run method");
            while (!finished) {
                try {
                    Object response = inputStream.readObject();
                    Response response1 = (Response) response;
                    System.out.println("Response received of type " + response1.getClass());
                    if (response1 instanceof UpdateCharityCasesResponse) {
                        handleUpdate();
                    } else {
                        responseBlockingQueue.put((Response) response);
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    System.out.println("Error in response reader " + e.getMessage());
                }
            }
            System.out.println("Reader is out");
            System.exit(0);
        }

    }
}
