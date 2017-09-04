package com.accele.engine.gfx.shader;

import com.accele.engine.Indexable;

public abstract class Shader implements Indexable {

	protected String registryId;
	protected String localizedId;
	protected int id;
	
	protected Shader(String registryId, String localizedId, int id) {
		this.registryId = registryId;
		this.localizedId = localizedId;
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	@Override
	public String getLocalizedId() {
		return localizedId;
	}
	
	public int getId() {
		return id;
	}
	
}
