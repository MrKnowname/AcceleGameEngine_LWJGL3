package com.accele.engine.gfx;

import com.accele.engine.Engine;
import com.accele.engine.gfx.shader.ShaderProgram;
import com.accele.engine.util.ACLEngineAccessProvider;

public class Graphics_ {

	private Engine engine;
	private ShaderProgram currentProgram;
	
	public Graphics_(Engine engine, Object key) {
		if (!ACLEngineAccessProvider.canAccess(key))
			throw ACLEngineAccessProvider.accessError(this.getClass().getSimpleName());
		
		this.engine = engine;
	}
	
	public ShaderProgram getCurrentProgram() {
		return currentProgram;
	}
	
	public void setCurrentProgram(ShaderProgram program) {
		this.currentProgram = program;
	}
	
}
