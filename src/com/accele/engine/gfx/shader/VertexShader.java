package com.accele.engine.gfx.shader;

import org.lwjgl.opengl.GL20;

import com.accele.engine.util.GLUtils;
import com.accele.engine.util.IndexedSet;
import com.accele.engine.util.Resource;
import com.accele.engine.util.Utils;

public class VertexShader extends Shader {

	private IndexedSet<String> attributes;
	
	public VertexShader(String registryId, String localizedId, Resource source, IndexedSet<String> attributes) {
		super(registryId, localizedId, GLUtils.NULL);
		
		this.id = GLUtils.createShader(registryId, source, GL20.GL_VERTEX_SHADER);
		this.attributes = attributes;
	}
	
	public int getIndexForAttribute(String attributeName) {
		return Utils.getIndexForEntry(attributes, attributeName);
	}
	
	public String getAttributeAtIndex(int index) {
		return attributes.get(index);
	}
	
	public IndexedSet<String> getAttributes() {
		return attributes;
	}

}
