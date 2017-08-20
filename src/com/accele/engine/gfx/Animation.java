package com.accele.engine.gfx;

import com.accele.engine.Indexable;
import com.accele.engine.util.Utils;

public class Animation implements Indexable {

	private String registryId;
	private String localizedId;
	private Texture[] frames;
	private int pointer;
	private double elapsedTime;
	private double currentTime;
	private double lastTime;
	private double fps;
	
	public Animation(String registryId, String localizedId, Texture[] frames, int fps) {
		this.frames = frames;
		this.pointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Utils.getCurrentTime();
		this.fps = 1.0 / (double) fps;
	}
	
	public void bind(int sampler) {
		this.currentTime = Utils.getCurrentTime();
		this.elapsedTime += currentTime - lastTime;
		
		if (elapsedTime >= fps) {
			elapsedTime -= fps;
			pointer++;
		}
		
		if (pointer >= frames.length)
			pointer = 0;
		
		this.lastTime = currentTime;
		
		frames[pointer].bind(sampler);
	}
	
	public void bind() {
		bind(0);
	}
	
	public void unbind() {
		frames[pointer].unbind();
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	@Override
	public String getLocalizedId() {
		return localizedId;
	}

	public Texture[] getFrames() {
		return frames;
	}

	public int getPointer() {
		return pointer;
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public double getLastTime() {
		return lastTime;
	}

	public double getFps() {
		return fps;
	}
	
}
