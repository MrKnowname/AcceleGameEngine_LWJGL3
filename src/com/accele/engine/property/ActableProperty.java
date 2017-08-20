package com.accele.engine.property;

import com.accele.engine.Engine;
import com.accele.engine.util.TriFunction;

public class ActableProperty extends Property {

	private TriFunction<Engine, Property, Object, Object> action;
	private ActablePropertyLocation location;
	
	public ActableProperty(Engine engine, String registryId, String localizedId, Object value, TriFunction<Engine, Property, Object, Object> action, ActablePropertyLocation location) {
		super(engine, registryId, localizedId, value);
		this.action = action;
		this.location = location;
		
		if (location == ActablePropertyLocation.INIT || location == ActablePropertyLocation.ALL)
			this.value = action.apply(engine, this, value);
	}
	
	@Override
	public Object get() {
		if (location == ActablePropertyLocation.RETRIEVAL || location == ActablePropertyLocation.ALL)
			this.value = action.apply(engine, this, value);
		return value;
	}
	
	@Override
	public void set(Object value) {
		if (location == ActablePropertyLocation.MODIFICATION || location == ActablePropertyLocation.ALL)
			this.value = action.apply(engine, this, value);
		else
			this.value = value;
	}

}
