package com.accele.engine.gfx.shader;

import org.lwjgl.opengl.GL20;

import com.accele.engine.util.GLUtils;
import com.accele.engine.util.Resource;

public class FragmentShader extends Shader {

	public FragmentShader(String registryId, String localizedId, Resource source) {
		super(registryId, localizedId, GLUtils.NULL);
		
		this.id = GLUtils.createShader(registryId, source, GL20.GL_FRAGMENT_SHADER);
	}

}
