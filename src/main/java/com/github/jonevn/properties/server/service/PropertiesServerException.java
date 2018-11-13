package com.github.jonevn.properties.server.service;

public class PropertiesServerException extends RuntimeException {

    public PropertiesServerException(String message) {
	super(message);
    }

    public PropertiesServerException(String message, Throwable t) {
	super(message, t);
    }
}