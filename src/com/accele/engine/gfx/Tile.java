package com.accele.engine.gfx;

import com.accele.engine.Engine;
import com.accele.engine.Indexable;

public class Tile implements Indexable {

	protected Engine engine;
	protected String registryId;
	protected String localizedId;
	protected Texture texture;
	protected boolean solid;
	
	public Tile(Engine engine, String registryId, String localizedId, Texture texture, boolean solid) {
		this.engine = engine;
		this.registryId = registryId;
		this.localizedId = localizedId;
		this.texture = texture;
		this.solid = solid;
	}

	@Override
	public String getRegistryId() {
		return registryId;
	}

	@Override
	public String getLocalizedId() {
		return localizedId;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
}
