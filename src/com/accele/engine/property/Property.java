package com.accele.engine.property;

import com.accele.engine.Engine;
import com.accele.engine.Indexable;

public class Property implements Indexable {

	protected Engine engine;
	protected String registryId;
	protected String localizedId;
	protected Object value;
	
	public Property(Engine engine, String registryId, String localizedId, Object value) {
		this.engine = engine;
		this.registryId = registryId;
		this.localizedId = localizedId;
		this.value = value;
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}

	@Override
	public String getLocalizedId() {
		return localizedId;
	}

	public Object get() {
		return value;
	}
	
	public void set(Object value) {
		this.value = value;
	}
	
}
