package com.accele.engine.util;

public final class ACLEngineAccessProvider {

	private static final ACLEngineAccessProvider INSTANCE = new ACLEngineAccessProvider();
	
	public Object key;
	
	private ACLEngineAccessProvider() {
		this.key = new Object();
	}
	
	public static boolean canAccess(Object key) {
		return key != null && key.equals(INSTANCE.key) && key.hashCode() == INSTANCE.key.hashCode();
	}
	
	public static IllegalAccessError accessError(String className) {
		return new IllegalAccessError("Illegal access of instance of class \"" + className + "\".");
	}
	
}
