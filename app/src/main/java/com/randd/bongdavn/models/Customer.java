package com.randd.bongdavn.models;

/**
 * Created by Thanh Le on 30/05/16.
 */
public class Customer {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String wardName;

    public Customer(int id, String name, String phone, String email, String wardName) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.wardName = wardName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    @Override
    public String toString() {
        return name + "\t" + phone + "\t" + email + "\t" + wardName;
    }
}
