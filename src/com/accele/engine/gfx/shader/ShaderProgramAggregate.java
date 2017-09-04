package com.accele.engine.gfx.shader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL41;
import org.lwjgl.opengl.GL43;

import com.accele.engine.util.GLUtils;
import com.accele.engine.util.Pair;

public class ShaderProgramAggregate {

	private int id;
	private Map<ShaderProgram, Class<? extends Shader>[]> programs;
	private Map<String, Integer> uniforms;
	
	public ShaderProgramAggregate(Map<ShaderProgram, Class<? extends Shader>[]> programs) {
		List<Pair<Integer, Integer>> shaderProgramData = new ArrayList<>();
		for (ShaderProgram program : programs.keySet()) {
			if (!program.isSeparable())
				throw new IllegalArgumentException("ShaderProgram \"" + program.getRegistryId() + "\" must be separable for use in a ShaderProgramAggregate.");
			int id = program.getId();
			int shaderType = 0;
			for (Class<? extends Shader> shaderClass : programs.get(program))
				shaderType |= shaderClassToInteger(shaderClass);
			shaderProgramData.add(new Pair<>(id, shaderType));
		}
		
		this.programs = programs;
		this.id = GLUtils.createShaderProgramPipeline(shaderProgramData);
		this.uniforms = new HashMap<>();
	}
	
	public void addShaderProgram(ShaderProgram program, Class<? extends Shader>[] shaderTypes) {
		if (!program.isSeparable())
			return;
		int shaderTypesAsInt = 0;
		for (Class<? extends Shader> shaderType : shaderTypes)
			shaderTypesAsInt |= shaderClassToInteger(shaderType);
		GLUtils.bindShaderProgramToPipeline(id, program.getId(), shaderTypesAsInt);
		programs.put(program, shaderTypes);
	}
	
	public void bind() {
		GLUtils.bindShaderProgramPipeline(id);
	}
	
	public void unbind() {
		GLUtils.unbindShaderProgramPipeline();
	}
	
	public void cleanUp() {
		GLUtils.removeShaderProgramPipeline(id);
		for (ShaderProgram program : programs.keySet())
			program.cleanUp();
	}
	
	public int getId() {
		return id;
	}
	
	public Map<ShaderProgram, Class<? extends Shader>[]> getPrograms() {
		return programs;
	}
	
	public void setUniformInt(String name, int val, ShaderProgram program) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(program.getId(), name));
		GLUtils.setUniformInt(id, uniforms.get(name), val);
	}
	
	public void setUniformFloat(String name, float val, ShaderProgram program) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(program.getId(), name));
		GLUtils.setUniformFloat(id, uniforms.get(name), val);
	}
	
	public void setUniformVector2f(String name, Vector2f val, ShaderProgram program) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(program.getId(), name));
		GLUtils.setUniformVector2f(id, uniforms.get(name), val);
	}
	
	public void setUniformVector3f(String name, Vector3f val, ShaderProgram program) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(program.getId(), name));
		GLUtils.setUniformVector3f(id, uniforms.get(name), val);
	}
	
	public void setUniformVector4f(String name, Vector4f val, ShaderProgram program) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(program.getId(), name));
		GLUtils.setUniformVector4f(id, uniforms.get(name), val);
	}
	
	public void setUniformMatrix4f(String name, Matrix4f val, ShaderProgram program) {
		if (!uniforms.containsKey(name))
			uniforms.put(name, GLUtils.getUniformLocation(program.getId(), name));
		GLUtils.setUniformMatrix4f(id, uniforms.get(name), val);
	}
	
	private static int shaderClassToInteger(Class<? extends Shader> shaderClass) {
		if (shaderClass.getCanonicalName().equals(VertexShader.class.getCanonicalName()))
			return GL41.GL_VERTEX_SHADER_BIT;
		else if (shaderClass.getCanonicalName().equals(TessellationControlShader.class.getCanonicalName()))
			return GL41.GL_TESS_CONTROL_SHADER_BIT;
		else if (shaderClass.getCanonicalName().equals(TessellationEvaluationShader.class.getCanonicalName()))
			return GL41.GL_TESS_EVALUATION_SHADER_BIT;
		else if (shaderClass.getCanonicalName().equals(GeometryShader.class.getCanonicalName()))
			return GL41.GL_GEOMETRY_SHADER_BIT;
		else if (shaderClass.getCanonicalName().equals(FragmentShader.class.getCanonicalName()))
			return GL41.GL_FRAGMENT_SHADER_BIT;
		else if (shaderClass.getCanonicalName().equals(ComputeShader.class.getCanonicalName()))
			return GL43.GL_COMPUTE_SHADER_BIT;
		else
			return -1;
	}
	
}
