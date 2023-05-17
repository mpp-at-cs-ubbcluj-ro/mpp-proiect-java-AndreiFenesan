package org.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@MappedSuperclass
public class Entity<ID extends Serializable> {
    ID id;

    public Entity(ID id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
