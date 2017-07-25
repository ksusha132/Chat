package com.company;


import java.io.Serializable;

public class Messages implements Serializable {
    private String name;
    private String message;

    Messages(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Messages : " +
               name + ": "+ message;
    }
}
