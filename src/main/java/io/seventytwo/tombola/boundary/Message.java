package io.seventytwo.tombola.boundary;

public class Message {

    private final boolean error;
    private final String message;

    public Message(String message) {
        this.error = false;
        this.message = message;
    }

    public Message(String message, boolean error) {
        this.error = error;
        this.message = message;
    }
    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
