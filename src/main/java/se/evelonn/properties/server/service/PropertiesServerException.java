package se.evelonn.properties.server.service;

public class PropertiesServerException extends RuntimeException {

    public PropertiesServerException(String message) {
	super(message);
    }

    public PropertiesServerException(String message, Throwable t) {
	super(message, t);
    }
}