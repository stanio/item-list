package net.example.spring.web;

/**
 * Error response representation.
 */
public class ErrorEntity {

    private String error;

    private String message;

    public ErrorEntity() {
        // default, empty
    }

    public ErrorEntity(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
