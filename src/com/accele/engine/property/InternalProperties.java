package com.accele.engine.property;

import com.accele.engine.util.ACLEngineAccessProvider;

public final class InternalProperties {

	private final String version;
	private String title;
	private int screenWidth;
	private int screenHeight;
	private boolean running;
	private final int gameType;
	private boolean fullscreen;
	private int fps;
	
	public InternalProperties(Object key, String version, String title, int screenWidth, int screenHeight, int gameType, boolean fullscreen) {
		if (!ACLEngineAccessProvider.canAccess(key))
			ACLEngineAccessProvider.accessError(this.getClass().getSimpleName());
		
		this.version = version;
		this.title = title;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.gameType = gameType;
		this.fullscreen = fullscreen;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public int getGameType() {
		return gameType;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setScreenWidth(Object key, int screenWidth) {
		if (!ACLEngineAccessProvider.canAccess(key))
			ACLEngineAccessProvider.accessError(this.getClass().getSimpleName());
		
		this.screenWidth = screenWidth;
	}
	
	public void setScreenHeight(Object key, int screenHeight) {
		if (!ACLEngineAccessProvider.canAccess(key))
			ACLEngineAccessProvider.accessError(this.getClass().getSimpleName());
		
		this.screenHeight = screenHeight;
	}
	
	public void setRunning(Object key, boolean running) {
		if (!ACLEngineAccessProvider.canAccess(key))
			ACLEngineAccessProvider.accessError(this.getClass().getSimpleName());
		
		this.running = running;
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}
	
	public void setFullscreen(boolean fullscreen) {
		// TODO: Implement this
		this.fullscreen = fullscreen;
	}
	
	public int getFps() {
		return fps;
	}
	
	public void setFps(Object key, int fps) {
		if (!ACLEngineAccessProvider.canAccess(key))
			ACLEngineAccessProvider.accessError(this.getClass().getSimpleName());
		
		this.fps = fps;
	}

	public float getAspectRatio() {
		return (float) screenWidth / (float) screenHeight;
	}
	
}
