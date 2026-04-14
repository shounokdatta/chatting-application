package com.nit.nitchattingapp;

public class MessageModel {
    String id, name, message, time;

    public MessageModel() {}

    public MessageModel(String id, String name, String message, String time) {
        //  New code
        // Nit
        this.id = id;
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
