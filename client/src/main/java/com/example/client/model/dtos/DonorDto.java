package com.example.client.model.dtos;

import java.io.Serializable;

public class DonorDto implements Serializable {
    private final String name;
    private final String email;
    private final String phone;

    public DonorDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "DonorDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
