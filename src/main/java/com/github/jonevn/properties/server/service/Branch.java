package com.github.jonevn.properties.server.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Branch {

    private final String name;

    private final String displayName;

    private final String urlEncodedName;

    public Branch(String name) {
	this.name = name;
	this.displayName = name.substring(name.lastIndexOf("/") + 1, name.length());
	try {
	    this.urlEncodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
	} catch (UnsupportedEncodingException e) {
	    throw new PropertiesServerException("Failed to urlEncode branch name " + name + ": " + e.getMessage());
	}
    }

    public String getName() {
	return name;
    }

    public String getDisplayName() {
	return displayName;
    }

    public String getUrlEncodedName() {
	return urlEncodedName;
    }

    @Override
    public int hashCode() {
	return Objects.hash(displayName);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Branch other = (Branch) obj;
	return Objects.equals(this.displayName, other.displayName);
    }
}