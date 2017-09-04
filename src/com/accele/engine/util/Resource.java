package com.accele.engine.util;

public class Resource {

	private String path;
	private Object value;
	
	public Resource(String path, ResourceLoader loader) {
		this.path = path;
		this.value = loader.load(path);
	}
	
	public Resource(Object value) {
		this.path = ":internal:";
		this.value = value;
	}
	
	public String getPath() {
		return path;
	}
	
	public Object get() {
		return value;
	}
	
}
