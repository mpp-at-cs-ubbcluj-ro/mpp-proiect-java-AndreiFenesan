package com.example.client.model;

import java.io.Serializable;

public class Entity<ID> {
    ID id;

    public Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
