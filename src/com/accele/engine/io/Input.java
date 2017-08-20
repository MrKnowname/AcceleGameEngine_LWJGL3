package com.accele.engine.io;

import com.accele.engine.Engine;
import com.accele.engine.Indexable;
import com.accele.engine.Tickable;

public abstract class Input implements Indexable, Tickable {

	protected Engine engine;
	protected String registryId;
	protected String localizedId;
	
	public Input(Engine engine, String registryId, String localizedId) {
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
