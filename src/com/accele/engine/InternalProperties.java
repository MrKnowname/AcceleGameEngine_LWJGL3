package com.accele.engine;

public final class InternalProperties {

	private final String version;
	private String title;
	private int screenWidth;
	private int screenHeight;
	private boolean running;
	private final int gameType;
	private boolean fullscreen;
	private int fps;
	
	protected InternalProperties(String version, String title, int screenWidth, int screenHeight, int gameType, boolean fullscreen) {
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
	
	protected void setTitle(String title) {
		this.title = title;
	}
	
	protected void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	protected void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	
	protected void setRunning(boolean running) {
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
	
	protected void setFps(int fps) {
		this.fps = fps;
	}
	
}
