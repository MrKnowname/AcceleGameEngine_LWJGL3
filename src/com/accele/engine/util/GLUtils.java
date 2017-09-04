package com.accele.engine.util;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL41.GL_PROGRAM_SEPARABLE;
import static org.lwjgl.opengl.GL41.glBindProgramPipeline;
import static org.lwjgl.opengl.GL41.glDeleteProgramPipelines;
import static org.lwjgl.opengl.GL41.glGenProgramPipelines;
import static org.lwjgl.opengl.GL41.glProgramParameteri;
import static org.lwjgl.opengl.GL41.glUseProgramStages;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public final class GLUtils {

	public static final int NULL = 0;
	
	private GLUtils() {}
	
	public static int initTexture(int width, int height, ByteBuffer pixels, Pair<Integer, Integer>[] params) {
		int textureId = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, textureId);
		for (Pair<Integer, Integer> param : params)
			glTexParameterf(GL_TEXTURE_2D, param.getFirst(), param.getSecond());
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		glBindTexture(GL_TEXTURE_2D, NULL);
		return textureId;
	}
	
	public static void bindTexture(int textureId) {
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	public static void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, NULL);
	}
	
	public static void deleteTexture(int textureId) {
		glDeleteTextures(textureId);
	}
	
	public static void setGLFWErrorCallbackTarget(PrintStream stream) {
		GLFWErrorCallback.createPrint(stream).set();
	}
	
	public static long initWindow(int width, int height, String title, int swapInterval, boolean fullscreen, Pair<Integer, Integer>[] windowHints) {
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW");
		
		for (Pair<Integer, Integer> hint : windowHints)
			GLFW.glfwWindowHint(hint.getFirst(), hint.getSecond());
		long window = GLFW.glfwCreateWindow(width, height, title, fullscreen ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL, MemoryUtil.NULL);
		
		if (window == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create GLFW window");
		
		if (!fullscreen) {
			GLFWVidMode mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window, (mode.width() - width) / 2, (mode.height() - height) / 2);			
		}
		
		GLFW.glfwMakeContextCurrent(window);
		//GLFW.glfwSwapInterval(swapInterval);
		
		return window;
	}
	
	public static void runWindow(long window) {
		GLFW.glfwShowWindow(window);
		//GL.createCapabilities();
	}
	
	public static void enableAlphaBlending() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void disableAlphaBlending() {
		glDisable(GL_BLEND);
	}
	
	public static void setClearColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}
	
	public static void clear(boolean clearDepth) {
		int target = GL_COLOR_BUFFER_BIT;
		if (clearDepth)
			target |= GL_DEPTH_BUFFER_BIT;
		glClear(target);
	}
	
	public static void pollEvents() {
		GLFW.glfwPollEvents();
	}
	
	public static void swapBuffers(long window) {
		GLFW.glfwSwapBuffers(window);
	}
	
	public static boolean windowShouldClose(long window) {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public static void finalize(long window) {
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
	
	public static Pair<Pair<Integer, Integer>, Integer> initModel(float[] vertices, int[] indices, float[] textureCoords) {
		FloatBuffer vBuffer = BufferUtils.createFloatBuffer(vertices.length);
		vBuffer.put(vertices);
		vBuffer.flip();
		
		int vertexId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexId);
		glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
		
		IntBuffer iBuffer = BufferUtils.createIntBuffer(indices.length);
		iBuffer.put(indices);
		iBuffer.flip();
		
		int indexId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
		
		int textureId = NULL;
		
		if (textureCoords != null) {
			FloatBuffer tBuffer = BufferUtils.createFloatBuffer(textureCoords.length);
			tBuffer.put(textureCoords);
			tBuffer.flip();
			
			textureId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, textureId);
			glBufferData(GL_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return new Pair<>(new Pair<>(vertexId, indexId), textureId);
	}
	
	public static void setClientState(int target, boolean value) {
		if (value)
			glEnableClientState(target);
		else
			glDisableClientState(target);
	}
	
	public static void bindArrayBuffer(int id) {
		glBindBuffer(GL_ARRAY_BUFFER, id);
	}
	
	public static void unbindArrayBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public static void enable(int target) {
		glEnable(target);
	}
	
	public static void disable(int target) {
		glDisable(target);
	}
	
	public static void bindElementArrayBuffer(int id) {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
	}
	
	public static void unbindElementArrayBuffer() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public static Pair<Integer, Pair<Integer, Integer>> createShader(String shaderRegistryId, Resource vertexShader, Resource fragmentShader, IndexedSet<String> attributes) {
		int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShaderId, vertexShader.get().toString());
		glCompileShader(vertexShaderId);
		if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) != 1)
			System.err.println("Error loading vertex shader " + vertexShader.getPath() + ", details:\n" + glGetShaderInfoLog(vertexShaderId));
		
		int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShaderId, fragmentShader.get().toString());
		glCompileShader(fragmentShaderId);
		if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) != 1)
			System.err.println("Error loading fragment shader " + fragmentShader.getPath() + ", details:\n" + glGetShaderInfoLog(fragmentShaderId));
		
		int programId = glCreateProgram();
		glAttachShader(programId, vertexShaderId);
		glAttachShader(programId, fragmentShaderId);
		
		if (attributes != null)
			for (int i = 0; i < attributes.size(); i++)
				glBindAttribLocation(programId, i, attributes.get(i));
		
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) != 1)
			System.err.println("Error linking shader " + shaderRegistryId + ", details:\n" + glGetProgramInfoLog(programId));
		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) != 1)
			System.err.println("Error validating shader " + shaderRegistryId + ", details:\n" + glGetProgramInfoLog(programId));
		
		return new Pair<>(programId, new Pair<>(vertexShaderId, fragmentShaderId));
	}
	
	public static int createShader(String shaderRegistryId, Resource source, int shaderType) {
		int id = glCreateShader(shaderType);
		glShaderSource(id, source.get().toString());
		glCompileShader(id);
		
		if (glGetShaderi(id, GL_COMPILE_STATUS) != 1) {
			System.err.println("Error loading shader " + shaderRegistryId + ", details:\n" + glGetShaderInfoLog(id));
			return NULL;
		}
		
		return id;
	}
	
	public static int createShaderProgram(int[] shaderIds, boolean preserveShaders, boolean separable) {
		int id = glCreateProgram();
		for (int shaderId : shaderIds)
			glAttachShader(id, shaderId);
		
		if (separable)
			glProgramParameteri(id, GL_PROGRAM_SEPARABLE, GL_TRUE);
		
		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) != 1) {
			System.err.println("Error linking shaders, details:\n" + glGetProgramInfoLog(id));
			return NULL;
		}
		
		glValidateProgram(id);
		if (glGetProgrami(id, GL_VALIDATE_STATUS) != 1) {
			System.err.println("Error validating shaders, details:\n" + glGetProgramInfoLog(id));
			return NULL;
		}
		
		if (!preserveShaders) {
			for (int shaderId : shaderIds) {
				glDetachShader(id, shaderId);
				glDeleteShader(shaderId);
			}
		}
		
		return id;
	}
	
	public static void deleteShaderProgram(int programId) {
		glDeleteProgram(programId);
	}
	
	public static void bindShader(int id) {
		glUseProgram(id);
	}
	
	public static void unbindShader() {
		glUseProgram(0);
	}
	
	public static void setUniform(int programId, String name, int value) {
		int location = glGetUniformLocation(programId, name);
		if (location != -1)
			glUniform1i(location, value);
	}
	
	public static void setUniform(int programId, String name, float value) {
		int location = glGetUniformLocation(programId, name);
		if (location != -1)
			glUniform1f(location, value);
	}
	
	public static void setUniform(int programId, String name, Vector2f value) {
		int location = glGetUniformLocation(programId, name);
		if (location != -1)
			glUniform2f(location, value.x, value.y);
	}
	
	public static void setUniform(int programId, String name, Vector3f value) {
		int location = glGetUniformLocation(programId, name);
		if (location != -1)
			glUniform3f(location, value.x, value.y, value.z);
	}
	
	public static void setUniform(int programId, String name, Vector4f value) {
		int location = glGetUniformLocation(programId, name);
		if (location != -1)
			glUniform4f(location, value.x, value.y, value.z, value.w);
	}
	
	public static void setUniform(int programId, String name, Matrix4f value) {
		int location = glGetUniformLocation(programId, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		value.get(buffer);
		if (location != -1)
			glUniformMatrix4fv(location, false, buffer);
	}

	public static void setActiveTexture(int target, int sampler) {
		if (sampler < 0 || sampler > 31)
			return;
		glActiveTexture(target + sampler);
	}
	
	public static void setActiveTexture(int target) {
		setActiveTexture(target, NULL);
	}
	
	public static long getCurrentTimeMS() {
		return (long) GLFW.glfwGetTime() * 1000L;
	}
	
	public static void setWindowSizeCallback(long window, GLFWWindowSizeCallbackI callback) {
		GLFW.glfwSetWindowSizeCallback(window, callback);
	}
	
	public static boolean isKeyDown(long window, int key) {
		return GLFW.glfwGetKey(window, key) == 1;
	}
	
	public static int getKeyAmount() {
		return GLFW.GLFW_KEY_LAST;
	}
	
	public static boolean isMouseButtonDown(long window, int button) {
		return GLFW.glfwGetMouseButton(window, button) == 1;
	}
	
	public static int getMouseButtonAmount() {
		return GLFW.GLFW_MOUSE_BUTTON_LAST;
	}

	public static void deleteModel(int vertexId, int indexId, int textureId) {
		glDeleteBuffers(vertexId);
		glDeleteBuffers(indexId);
		glDeleteBuffers(textureId);
	}

	public static void deleteShader(int programId, int vertexShaderId, int fragmentShaderId) {
		glDetachShader(programId, vertexShaderId);
		glDetachShader(programId, fragmentShaderId);
		glDeleteShader(vertexShaderId);
		glDeleteShader(fragmentShaderId);
		glDeleteProgram(programId);
	}

	public static int getUniformLocation(int programId, String name) {
		return glGetUniformLocation(programId, name);
	}
	
	public static void setUniformInt(int programId, int uniformLoc, int val) {
		glUniform1i(uniformLoc, val);
	}
	
	public static void setUniformFloat(int programId, int uniformLoc, float val) {
		glUniform1f(uniformLoc, val);
	}
	
	public static void setUniformVector2f(int programId, int uniformLoc, Vector2f val) {
		glUniform2f(uniformLoc, val.x, val.y);
	}
	
	public static void setUniformVector3f(int programId, int uniformLoc, Vector3f val) {
		glUniform3f(uniformLoc, val.x, val.y, val.z);
	}

	public static void setUniformVector4f(int programId, int uniformLoc, Vector4f val) {
		glUniform4f(uniformLoc, val.x, val.y, val.z, val.w);
	}

	public static void setUniformMatrix4f(int programId, int uniformLoc, Matrix4f val) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		val.get(buffer);
		glUniformMatrix4fv(uniformLoc, false, buffer);
	}

	public static int createShaderProgramPipeline(List<Pair<Integer, Integer>> shaderProgramData) {
		int id = glGenProgramPipelines();
		
		for (Pair<Integer, Integer> data : shaderProgramData)
			glUseProgramStages(id, data.getSecond(), data.getFirst());
		
		return id;
	}
	
	public static void bindShaderProgramPipeline(int id) {
		glBindProgramPipeline(id);
	}
	
	public static void unbindShaderProgramPipeline() {
		glBindProgramPipeline(NULL);
	}
	
	public static void bindShaderProgramToPipeline(int shaderProgramPipelineId, int shaderProgramId, int shaderTypes) {
		glUseProgramStages(shaderProgramPipelineId, shaderTypes, shaderProgramId);
	}

	public static void removeShaderProgramPipeline(int id) {
		glDeleteProgramPipelines(id);
	}

	public static void initOpenGL() {
		GL.createCapabilities();
	}
	
}
