package org.example.models;

public class Volunteer extends Entity<Long> {
    private String name;
    private String username;
    private String password;

    public Volunteer(String name, String username, String password) {
        super(null);
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public Volunteer(Long aLong, String name, String username, String password) {
        super(aLong);
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
