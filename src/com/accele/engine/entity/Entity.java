package com.accele.engine.entity;

import com.accele.engine.Engine;
import com.accele.engine.Indexable;
import com.accele.engine.Renderable;
import com.accele.engine.Tickable;

public abstract class Entity implements Indexable, Tickable, Renderable {

	protected Engine engine;
	protected String registryId;
	protected String localizedId;
	
	public Entity(Engine engine, String registryId, String localizedId) {
		this.engine = engine;
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
	
}
