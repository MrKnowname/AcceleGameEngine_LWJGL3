package com.accele.engine.gfx;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.accele.engine.Indexable;
import com.accele.engine.util.GLUtils;
import com.accele.engine.util.Pair;
import com.accele.engine.util.Resource;
import com.accele.engine.util.Utils;

public class Texture implements Indexable {

	private String registryId;
	private String localizedId;
	private final int textureId;
	private int width;
	private int height;
	private ByteBuffer pixels;
	
	public Texture(String registryId, String localizedId, Resource image) {
		this.registryId = registryId;
		this.localizedId = localizedId;
		Object[] data = (Object[]) image.get();
		this.width = (int) data[0];
		this.height = (int) data[1];
		this.pixels = (ByteBuffer) data[2];
		this.textureId = GLUtils.initTexture(width, height, pixels,
				Utils.genericArray(new Pair<Integer, Integer>(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST), 
						new Pair<Integer, Integer>(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)));
	}
	
	public void bind(int sampler) {
		GLUtils.setActiveTexture(GL13.GL_TEXTURE0, sampler);
		GLUtils.bindTexture(textureId);
	}
	
	public void bind() {
		bind(0);
	}
	
	public void unbind() {
		GLUtils.setActiveTexture(GLUtils.NULL);
		GLUtils.unbindTexture();
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	@Override
	public String getLocalizedId() {
		return localizedId;
	}
	
	public final int getTextureId() {
		return textureId;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ByteBuffer getPixels() {
		return pixels;
	}
	
}
