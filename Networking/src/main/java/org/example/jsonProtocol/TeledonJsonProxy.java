package org.example.jsonProtocol;

import org.example.objectProtocol.TeledonServiceProxy;
import org.example.objectProtocol.responses.Response;
import org.example.objectProtocol.responses.UpdateCharityCasesResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.models.Donor;
import org.models.dtos.CharityCaseDto;
import org.services.ITeledonService;
import org.services.Observer;
import org.services.ServiceException;

import java.beans.IntrospectionException;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TeledonJsonProxy implements ITeledonService {
    private final int port;
    private final String host;
    private Socket server;
    private final BlockingQueue<String> responses;
    private OutputStream outputStream;
    private InputStream inputStream;
    private volatile boolean finished;
    List<Observer> observers;

    public TeledonJsonProxy(int port, String host) {
        this.port = port;
        this.host = host;
        this.responses = new LinkedBlockingQueue<>();
        inputStream = null;
        outputStream = null;
        server = null;
        this.observers = new ArrayList<>();
    }

    @Override
    public Iterable<CharityCaseDto> getAllCharityCases() {
        JSONObject object = new JSONObject();
        object.put("type", 2);
        writeString(outputStream, object.toString());
        String response = readResponse();
        JSONArray array = new JSONArray(response);
        List<CharityCaseDto> charityCaseDtos = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            long id = jsonObject.getLong("Id");
            String name = jsonObject.getString("CaseName");
            String description = jsonObject.getString("Description");
            double amountCollected = jsonObject.getDouble("TotalAmountCollected");
            charityCaseDtos.add(new CharityCaseDto(id, name, description, amountCollected));
        }
        return charityCaseDtos;
    }

    @Override
    public void addNewDonation(Long charityCaseId, String name, String emailAddress, String phoneNumber, Double donationAmount) throws ServiceException {
        JSONObject object = new JSONObject();
        object.put("type", 4);
        object.put("donorName", name);
        object.put("caseId", charityCaseId);
        object.put("email", emailAddress);
        object.put("phone", phoneNumber);
        object.put("amount", donationAmount);

        writeString(outputStream, object.toString());
        String response = readResponse();
        if (!isOkResponse(response)) {
            JSONObject error = new JSONObject(response);
            throw new ServiceException(error.getString("message"));
        }
    }

    @Override
    public Iterable<Donor> getDonorsWithNameContaining(String containsInName) {
        JSONObject object = new JSONObject();
        object.put("type", 3);
        object.put("nameContaining", containsInName);
        writeString(outputStream, object.toString());
        String response = readResponse();
        JSONArray array = new JSONArray(response);
        List<Donor> donors = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String name = jsonObject.getString("Name");
            String email = jsonObject.getString("EmailAddress");
            String phone = jsonObject.getString("PhoneNumber");
            donors.add(new Donor(name, email, phone));
        }
        return donors;
    }

    @Override
    public boolean authenticateVolunteer(String username, String password) {
        if (server == null) {
            initialiseConnection();
        }
        JSONObject object = new JSONObject();
        object.put("type", 1);
        object.put("username", username);
        object.put("password", password);
        writeString(outputStream, object.toString());
        String response = readResponse();
        return isOkResponse(response);

    }

    @Override
    public void logout() {

    }

    @Override
    public void addObserver(Observer observer) {
        System.out.println("Adding observer");
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyAllObservers() {
        System.out.println("Notifiying observers");
        this.observers.forEach(Observer::update);
    }

    private void initialiseConnection() {
        try {
            System.out.println("Initialising connection");
            this.server = new Socket(this.host, this.port);
            System.out.println("Connected");
            this.inputStream = server.getInputStream();
            this.outputStream = server.getOutputStream();
            outputStream.flush();
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

    private void writeString(OutputStream outputStream, String string) {
        try {
            System.out.println("Sending: " + string);
            outputStream.write(string.length());
            outputStream.write(string.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error in writing JSON");
        }
    }

    private String readString(InputStream inputStream) throws IOException {
        byte[] lengthData = inputStream.readNBytes(4);
        int length = ByteBuffer.wrap(lengthData).getInt();
        byte[] data = inputStream.readNBytes(length);
        return new String(data);
    }

    private boolean isOkResponse(String jsonResponse) {
        JSONObject object = new JSONObject(jsonResponse);
        String isOk = object.getString("response");
        return isOk.equals("OK");
    }

    private String readResponse() {
        try {
            String response = responses.take();
            return response;
        } catch (InterruptedException e) {
            System.out.println("Error in getting the response from queue " + e.getMessage());
        }
        return null;
    }

    private void handleUpdate() {
        System.out.println("Handeling update response");
        this.notifyAllObservers();
    }

    private boolean isUpdateResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            return object.getString("response").equals("update");
        } catch (JSONException e) {
            System.out.println("Error; It may be an array");
        }
        return false;
    }

    private class ResponseReader implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread started. Now in run method");
            while (!finished) {
                try {
                    String response1 = readString(inputStream);
                    System.out.println("Response received:" + response1);
                    if (isUpdateResponse(response1)) {
                        handleUpdate();
                    } else {
                        responses.put(response1);
                    }
                } catch (IOException | InterruptedException e) {
                    System.out.println("Error in response reader " + e.getMessage());
                }
            }
            System.out.println("Reader is out");
            System.exit(0);
        }

    }
}
