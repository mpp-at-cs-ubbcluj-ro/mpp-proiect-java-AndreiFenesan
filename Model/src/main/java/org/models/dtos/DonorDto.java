package org.models.dtos;

import java.io.Serializable;

public class DonorDto implements Serializable {
    private final Long id;
    private final String name;
    private final String email;
    private final String phone;

    public DonorDto(Long id, String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.id = id;
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

    public Long getId() {
        return id;
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
