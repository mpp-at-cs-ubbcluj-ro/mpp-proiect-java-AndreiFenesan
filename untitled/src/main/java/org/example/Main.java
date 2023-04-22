package org.example;

import org.example.objectProtocol.requests.LoginRequest;
import org.example.objectProtocol.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Maaai");
        Socket socket = new Socket("127.0.0.1", 34999);
        System.out.println("Connected");
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream.flush();
        System.out.println("Sending request");
        outputStream.writeObject(new LoginRequest("asd", "asd"));
        outputStream.flush();
        System.out.println("Waiting for response");
        Response response = (Response) inputStream.readObject();
        System.out.println("Response got");

        System.out.println(response.getClass());
        while (true) {

        }
    }
}