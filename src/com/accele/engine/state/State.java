package com.accele.engine.state;

import com.accele.engine.Engine;
import com.accele.engine.Indexable;
import com.accele.engine.Renderable;
import com.accele.engine.Tickable;

public abstract class State implements Indexable, Tickable, Renderable {

	protected Engine engine;
	protected String registryId;
	protected String localizedId;
	
	public State(Engine engine, String registryId, String localizedId) {
		this.engine = engine;
		this.registryId = registryId;
		this.localizedId = localizedId;
	}
	
	public abstract void onInit();
	
	public abstract void onExit();
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	@Override
	public String getLocalizedId() {
		return localizedId;
	}
	
}
