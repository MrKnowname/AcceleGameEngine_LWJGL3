package com.accele.engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.accele.engine.gfx.Camera;
import com.accele.engine.gfx.Graphics;
import com.accele.engine.gfx.Model;
import com.accele.engine.gfx.Shader;
import com.accele.engine.gfx.Texture;
import com.accele.engine.gfx.Tile;
import com.accele.engine.gfx.World;
import com.accele.engine.impl.Player;
import com.accele.engine.io.Input;
import com.accele.engine.io.KeyInput;
import com.accele.engine.io.MouseInput;
import com.accele.engine.util.GFXUtils;
import com.accele.engine.util.GLUtils;
import com.accele.engine.util.IndexedSet;
import com.accele.engine.util.Pair;
import com.accele.engine.util.Resource;
import com.accele.engine.util.ResourceUtils;
import com.accele.engine.util.Utils;

public final class Engine {

	// Temporary variable
	private World world;
	
	private Registry registry;
	private InternalProperties internalProperties;
	private Graphics graphics;
	private Camera camera;
	private KeyInput defaultKeyInput;
	private MouseInput defaultMouseInput;
	private long window;
	private boolean finalize;
	
	public Engine(String title, int screenWidth, int screenHeight, int gameType, boolean fullscreen) {
		this.registry = new Registry();
		this.internalProperties = new InternalProperties("AcceleGameEngine vALPHA-0.0.1", title, screenWidth, screenHeight, gameType, false);
		this.graphics = new Graphics(this);
		this.camera = new Camera(screenWidth, screenHeight);
		this.finalize = false;
		
		registry.register(this.defaultKeyInput = new KeyInput(this));
		registry.register(this.defaultMouseInput = new MouseInput(this));
		
		init();
	}
	
	public Engine(String title, int screenWidth, int screenHeight) {
		this(title, screenWidth, screenHeight, 0, false);
	}
	
	public Engine(String title, int gameType) {
		this(title, 640, 480, gameType, false);
	}
	
	public Engine(String title) {
		this(title, 640, 480, 0, false);
	}
	
	public Engine(int screenWidth, int screenHeight, int gameType) {
		this("", screenWidth, screenHeight, gameType, false);
	}
	
	public Engine(int screenWidth, int screenHeight) {
		this("", screenWidth, screenHeight, 0, false);
	}
	
	public Engine(int gameType) {
		this("", 640, 480, gameType, false);
	}
	
	public Engine() {
		this("", 640, 480, 0, false);
	}
	
	private void init() {
		GLUtils.setGLFWErrorCallbackTarget(System.err);
		
		this.window = GLUtils.initWindow(internalProperties.getScreenWidth(), internalProperties.getScreenHeight(), 
				internalProperties.getTitle(), 1, internalProperties.isFullscreen(), Utils.genericArray(new Pair<Integer, Integer>(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)));
		
		GLUtils.setWindowSizeCallback(window, (window, width, height) -> {
			this.internalProperties.setScreenWidth(width);
			this.internalProperties.setScreenHeight(height);
		});
	}
	
	public void run() {
		internalProperties.setRunning(true);
		
		GLUtils.runWindow(window);
		GLUtils.enableAlphaBlending();
		GLUtils.setClearColor(0, 0, 0, 0);
		
		graphics.init();
		
		Texture texture = new Texture("acl.texture.test", "test", new Resource("C:/Users/MrKnowname/Desktop/grass.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Texture texture2 = new Texture("acl.texture.test2", "test2", new Resource("C:/Users/MrKnowname/Desktop/test.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Shader shader = new Shader("acl.shader.test", "test", new Resource("C:/Users/MrKnowname/Desktop/testVS.txt", ResourceUtils.DEFAULT_SHADER_LOADER), 
				new Resource("C:/Users/MrKnowname/Desktop/testFS.txt", ResourceUtils.DEFAULT_SHADER_LOADER), new IndexedSet<>(Arrays.asList("vertices", "attrib_textureCoords")));
		
		Tile tile = new Tile(this, "acl.tile.t1", "t1", texture, false);
		Tile tile2 = new Tile(this, "acl.tile.t2", "t2", texture2, true);
		
		//Tile[] tiles = GFXUtils.uniformTileMap(tile, 64, 64);
		
		Object[] mapRaw = (Object[]) new Resource("C:/Users/MrKnowname/Desktop/testMap.png", ResourceUtils.DEFAULT_MAP_LOADER).get();
		Map<Integer, Tile> reference = new HashMap<>();
		reference.put(Utils.rgbaToHex(255, 0, 0, 255), tile2);
		reference.put(Utils.rgbaToHex(0, 255, 0, 255), tile);
		Tile[] map = Utils.interpolateMapFromPixels((int[]) mapRaw[2], (int) mapRaw[0], (int) mapRaw[1], reference, tile);
		
		this.world = new World(map, 64, 64, 16, 24, shader);
		
		world.setTile(5, 0, tile2);
		world.setTile(6, 0, tile2);
		world.setTile(7, 0, tile2);
		world.setTile(7, 1, tile2);
		world.setTile(7, 2, tile2);
		world.setTile(8, 2, tile2);
		
		Player player = new Player(this, "acl.entity.player", "player", registry.getModel("tile_default"), texture2, shader);
		
		graphics.enableTextureMode();
		
		double frameCap = 1.0 / 60.0;
		double time = Utils.getCurrentTime();
		double unprocessed = 0;
		double frameTime = 0;
		int frames = 0;
		
		while (internalProperties.isRunning() && !GLUtils.windowShouldClose(window)) {
			boolean canRender = false;
			double currentTime = Utils.getCurrentTime();
			double delta = currentTime - time;
			unprocessed += delta;
			frameTime += delta;
			
			time = currentTime;
			
			while (unprocessed >= frameCap) {
				unprocessed -= frameCap;
				canRender = true;
				
				if (frameTime > 1.0) {
					frameTime = 0;
					internalProperties.setFps(frames);
					frames = 0;
				}
				
				// Update
				if (defaultKeyInput.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
					exit(true);
				}
				
				player.onUpdate((float) frameCap);
				GFXUtils.lockCameraToWorld(camera, world, internalProperties.getScreenWidth(), internalProperties.getScreenHeight());
				
				for (Input input : registry.getInputs())
					input.onUpdate((float) frameCap);
				GLUtils.pollEvents();
			}
			
			if (canRender) {
				frames++;
				
				// Render
				GLUtils.clear(internalProperties.getGameType() == 2 ? false : true);
				
				world.onRender(graphics);
				player.onRender(graphics);
				
				GLUtils.swapBuffers(window);
			}
		}
		
		cleanUp();
	}
	
	private void cleanUp() {
		if (finalize) {
			// TODO: Finalizers go here
		}
		
		for (Texture texture : registry.getTextures())
			GLUtils.deleteTexture(texture.getTextureId());
		
		for (Model model : registry.getModels())
			GLUtils.deleteModel(model.getVertexId(), model.getIndexId(), model.getTextureId());
		
		for (Shader shader : registry.getShaders())
			GLUtils.deleteShader(shader.getProgramId(), shader.getVertexShaderId(), shader.getFragmentShaderId());
		
		GLUtils.finalize(window);
	}
	
	public void exit(boolean finalize) {
		this.finalize = finalize;
		internalProperties.setRunning(false);
	}
	
	public void forceExit() {
		for (Texture texture : registry.getTextures())
			GLUtils.deleteTexture(texture.getTextureId());
		
		for (Model model : registry.getModels())
			GLUtils.deleteModel(model.getVertexId(), model.getIndexId(), model.getTextureId());
		
		for (Shader shader : registry.getShaders())
			GLUtils.deleteShader(shader.getProgramId(), shader.getVertexShaderId(), shader.getFragmentShaderId());
		
		GLUtils.finalize(window);
		System.exit(1);
	}
	
	public Registry getRegistry() {
		return registry;
	}
	
	public InternalProperties getInternalProperties() {
		return internalProperties;
	}
	
	public Graphics getGraphics() {
		return graphics;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public KeyInput getDefaultKeyInput() {
		return defaultKeyInput;
	}
	
	public MouseInput getDefaultMouseInput() {
		return defaultMouseInput;
	}
	
	public long getWindow() {
		return window;
	}
	
	public static void main(String[] args) {
		Engine engine = new Engine("Test");
		engine.run();
	}
	
	// Temporary method
	public World getWorld() {
		return world;
	}
	
}
