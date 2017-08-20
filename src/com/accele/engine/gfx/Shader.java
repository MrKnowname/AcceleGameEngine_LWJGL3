package com.accele.engine.gfx;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.accele.engine.Indexable;
import com.accele.engine.util.GLUtils;
import com.accele.engine.util.IndexedSet;
import com.accele.engine.util.Pair;
import com.accele.engine.util.Resource;
import com.accele.engine.util.Utils;

public class Shader implements Indexable {

	private String registryId;
	private String localizedId;
	private int programId;
	private int vertexShaderId;
	private int fragmentShaderId;
	private IndexedSet<String> attributes;
	
	public Shader(String registryId, String localizedId, Resource vertexShader, Resource fragmentShader, IndexedSet<String> attributes) {
		this.registryId = registryId;
		this.localizedId = localizedId;
		this.attributes = Utils.unmodifiableIndexedSet(attributes);
		
		Pair<Integer, Pair<Integer, Integer>> ids = GLUtils.createShader(registryId, vertexShader, fragmentShader, attributes);
		this.programId = ids.getFirst();
		this.vertexShaderId = ids.getSecond().getFirst();
		this.fragmentShaderId = ids.getSecond().getSecond();
	}
	
	public void bind() {
		GLUtils.bindShader(programId);
	}
	
	public void unbind() {
		GLUtils.unbindShader();
	}
	
	public void setUniform(String name, int value) {
		GLUtils.setUniform(programId, name, value);
	}
	
	public void setUniform(String name, float value) {
		GLUtils.setUniform(programId, name, value);
	}
	
	public void setUniform(String name, Vector2f value) {
		GLUtils.setUniform(programId, name, value);
	}
	
	public void setUniform(String name, Vector3f value) {
		GLUtils.setUniform(programId, name, value);
	}
	
	public void setUniform(String name, Vector4f value) {
		GLUtils.setUniform(programId, name, value);
	}
	
	public void setUniform(String name, Matrix4f value) {
		GLUtils.setUniform(programId, name, value);
	}

	public String getRegistryId() {
		return registryId;
	}

	public String getLocalizedId() {
		return localizedId;
	}

	public int getProgramId() {
		return programId;
	}

	public int getVertexShaderId() {
		return vertexShaderId;
	}

	public int getFragmentShaderId() {
		return fragmentShaderId;
	}
	
	public IndexedSet<String> getAttributes() {
		return attributes;
	}
	
	public String getAttribute(int index) {
		return attributes.get(index);
	}
	
}
