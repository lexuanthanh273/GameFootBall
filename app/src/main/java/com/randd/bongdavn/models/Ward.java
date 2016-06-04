package com.randd.bongdavn.models;

/**
 * Created by Thanh Le on 30/05/16.
 */
public class Ward {
    private int id;
    private String name;
    private int count;

    public Ward(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return name + "\t" + count;
    }
}
