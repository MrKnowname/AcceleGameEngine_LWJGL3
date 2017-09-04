package com.accele.engine.gfx.shader;

import org.lwjgl.opengl.GL43;

import com.accele.engine.util.GLUtils;
import com.accele.engine.util.Resource;

public class ComputeShader extends Shader {

	public ComputeShader(String registryId, String localizedId, Resource source) {
		super(registryId, localizedId, GLUtils.NULL);
		
		this.id = GLUtils.createShader(registryId, source, GL43.GL_COMPUTE_SHADER);
	}

}
