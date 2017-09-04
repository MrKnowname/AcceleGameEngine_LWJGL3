package com.accele.engine.gfx.shader;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.accele.engine.Indexable;
import com.accele.engine.util.GLUtils;

public class ShaderProgram implements Indexable {

	private String registryId;
	private String localizedId;
	private int id;
	private Shader[] shaders;
	private Map<String, Integer> uniforms;
	private boolean separable;
	
	public ShaderProgram(String registryId, String localizedId, Shader[] shaders) {
		this(registryId, localizedId, shaders, false, false);
	}
	
	public ShaderProgram(String registryId, String localizedId, Shader[] shaders, boolean preserveShaders, boolean separable) {
		this.registryId = registryId;
		this.localizedId = localizedId;
		this.shaders = shaders;
		
		int[] shaderIds = new int[shaders.length];
		
		for (int i = 0; i < shaderIds.length; i++)
			shaderIds[i] = shaders[i].getId();
		
		this.id = GLUtils.createShaderProgram(shaderIds, preserveShaders, separable);
		this.uniforms = new HashMap<>();
		this.separable = separable;
	}
	
	public void bind() {
		GLUtils.bindShader(id);
	}
	
	public void unbind() {
		GLUtils.unbindShader();
	}
	
	public void cleanUp() {
		GLUtils.deleteShaderProgram(id);
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	@Override
	public String getLocalizedId() {
		return localizedId;
	}
	
	public int getId() {
		return id;
	}
	
	public Shader[] getShaders() {
		return shaders;
	}
	
	public boolean isSeparable() {
		return separable;
	}
	
	public void setUniformInt(String name, int val) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(id, name));
		GLUtils.setUniformInt(id, uniforms.get(name), val);
	}
	
	public void setUniformFloat(String name, float val) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(id, name));
		GLUtils.setUniformFloat(id, uniforms.get(name), val);
	}
	
	public void setUniformVector2f(String name, Vector2f val) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(id, name));
		GLUtils.setUniformVector2f(id, uniforms.get(name), val);
	}
	
	public void setUniformVector3f(String name, Vector3f val) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(id, name));
		GLUtils.setUniformVector3f(id, uniforms.get(name), val);
	}
	
	public void setUniformVector4f(String name, Vector4f val) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(id, name));
		GLUtils.setUniformVector4f(id, uniforms.get(name), val);
	}
	
	public void setUniformMatrix4f(String name, Matrix4f val) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(id, name));
		GLUtils.setUniformMatrix4f(id, uniforms.get(name), val);
	}
	
}
