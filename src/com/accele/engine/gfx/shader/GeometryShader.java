package com.accele.engine.gfx.shader;

import org.lwjgl.opengl.GL32;

import com.accele.engine.util.GLUtils;
import com.accele.engine.util.Resource;

public class GeometryShader extends Shader {

	public GeometryShader(String registryId, String localizedId, Resource source) {
		super(registryId, localizedId, GLUtils.NULL);
		
		this.id = GLUtils.createShader(registryId, source, GL32.GL_GEOMETRY_SHADER);
	}

}
