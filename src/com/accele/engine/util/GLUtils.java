package com.accele.engine.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
		glBindTexture(GL_TEXTURE_2D, 0);
		return textureId;
	}
	
	public static void bindTexture(int textureId) {
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	public static void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
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
		GL.createCapabilities();
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
	
}
