package com.accele.engine.gfx.shader;

import org.lwjgl.opengl.GL40;

import com.accele.engine.util.GLUtils;
import com.accele.engine.util.Resource;

public class TessellationControlShader extends Shader {

	public TessellationControlShader(String registryId, String localizedId, Resource source) {
		super(registryId, localizedId, GLUtils.NULL);
		
		this.id = GLUtils.createShader(registryId, source, GL40.GL_TESS_CONTROL_SHADER);
	}

}
