package org.example.objectProtocol;

import org.example.objectProtocol.requests.*;
import org.example.objectProtocol.responses.*;
import org.models.Donor;
import org.models.dtos.CharityCaseDto;
import org.models.dtos.DonorDto;
import org.models.dtos.DonorDtoBuilder;
import org.services.ITeledonService;
import org.services.Observer;
import org.services.ServiceException;
import org.services.ValidationError;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientWorker implements Runnable, Observer {
    private final Socket client;
    private ITeledonService teledonService;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private volatile boolean connected;

    public ClientWorker(Socket client, ITeledonService teledonService) {
        this.client = client;
        this.teledonService = teledonService;
        try {
            this.inputStream = new ObjectInputStream(client.getInputStream());
            this.outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.flush();
            connected = true;
        } catch (IOException e) {
            System.out.println("Exception in client worker: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Request request = (Request) inputStream.readObject();
                System.out.println("Got a request " + request.getClass());
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error in Client worker run: " + e.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            this.inputStream.close();
            this.outputStream.close();
            client.close();
        } catch (IOException e) {
            System.out.println("Error in closing connection " + e.getMessage());
        }

    }

    private void sendResponse(Response response) {
        try {
            this.outputStream.writeObject(response);
            this.outputStream.flush();
        } catch (IOException exception) {
            System.out.println("Error in ending response: " + exception.getMessage());
        }
    }

    private Response handleRequest(Request request) {
        if (request instanceof LoginRequest) {
            return processLogin((LoginRequest) request);
        } else if (request instanceof NewDonationRequest) {
            return processNewDonation((NewDonationRequest) request);
        } else if (request instanceof GetAllCharityCasesRequest) {
            return processGetAllCharityCases((GetAllCharityCasesRequest) request);
        } else if (request instanceof GetDonorWithNameLikeRequest) {
            return processDonorNameLike((GetDonorWithNameLikeRequest) request);
        } else if (request instanceof LogOutRequest) {
            closeConnection();
        }
        return null;
    }

    private void closeConnection() {
        System.out.println("Closing connection");
        try {
            this.inputStream.close();
            this.outputStream.close();
            this.teledonService.removeObserver(this);
            this.connected = false;
        } catch (IOException e) {
            System.out.println("Error in closing connection");
        }
    }

    private Response processDonorNameLike(GetDonorWithNameLikeRequest request) {
        Iterable<Donor> donors = this.teledonService.getDonorsWithNameContaining(request.getNameContains());
        List<DonorDto> donorDtos = new ArrayList<>();
        DonorDtoBuilder builder = new DonorDtoBuilder();
        for (var donor : donors) {
            donorDtos.add(builder.buildFromDonor(donor));
        }
        return new GetDonorWithNameLikeResponse(donorDtos);
    }

    private Response processGetAllCharityCases(GetAllCharityCasesRequest request) {
        Iterable<CharityCaseDto> charityCases = this.teledonService.getAllCharityCases();
        return new GetAllCharityCasesResponse(charityCases);
    }

    private Response processNewDonation(NewDonationRequest request) {
        Long charityCaseId = request.getCharityCaseId();
        String phoneNumber = request.getPhoneNumber();
        String email = request.getEmail();
        String name = request.getName();
        Double amount = request.getAmount();
        try {
            this.teledonService.addNewDonation(charityCaseId, name, email, phoneNumber, amount);
            System.out.println("Sending ok response");
            return new ResponseOk("Added successfully");
        } catch (ServiceException | ValidationError e) {
            System.out.println("Sending error response");
            return new ResponseError(e.getMessage());
        }
    }

    private Response processLogin(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean isAuthorized = this.teledonService.authenticateVolunteer(username, password);
        if (isAuthorized) {
            this.teledonService.addObserver(this);
            return new ResponseOk();
        } else {
            return new ResponseError("Invalid credentials");
        }
    }

    @Override
    public void update() {
        System.out.println("Sending update response");
        sendResponse(new UpdateCharityCasesResponse());
    }
}
