package com.accele.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
import com.accele.engine.property.InternalProperties;
import com.accele.engine.state.StateHandler;
import com.accele.engine.util.ACLEngineAccessProvider;
import com.accele.engine.util.GFXUtils;
import com.accele.engine.util.GLUtils;
import com.accele.engine.util.IndexedSet;
import com.accele.engine.util.Pair;
import com.accele.engine.util.Resource;
import com.accele.engine.util.ResourceUtils;
import com.accele.engine.util.Utils;

public final class Engine {

	// TODO: Redefine how shaders work: one shader object should be linked to only one shader type (i.e. vertex, fragment, tessellation, etc.)
	// TODO: Shaders should be linked into a singular shader program via the Graphics class (i.e. a method such as "linkShaders()")
	
	// Temporary variable
	private World world;
	
	private Registry registry;
	private InternalProperties internalProperties;
	private Graphics graphics;
	private Camera camera;
	private KeyInput defaultKeyInput;
	private MouseInput defaultMouseInput;
	private StateHandler stateHandler;
	private Random rand;
	private long window;
	private boolean finalize;
	private Object key;
	
	public Engine(String title, int screenWidth, int screenHeight, int gameType, boolean fullscreen) {
		try {
			Field providerInstance = ACLEngineAccessProvider.class.getDeclaredField("INSTANCE");
			providerInstance.setAccessible(true);
			this.key = ((ACLEngineAccessProvider) providerInstance.get(null)).key;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.registry = new Registry();
		this.internalProperties = new InternalProperties(key, "AcceleGameEngine vALPHA-0.0.1", title, screenWidth, screenHeight, gameType, false);
		this.graphics = new Graphics(this, key);
		this.camera = new Camera(screenWidth, screenHeight);
		this.stateHandler = new StateHandler(key);
		this.rand = ThreadLocalRandom.current();
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
			this.internalProperties.setScreenWidth(key, width);
			this.internalProperties.setScreenHeight(key, height);
		});
		
		GLUtils.initOpenGL();
	}
	
	/*public void run() {
		internalProperties.setRunning(key, true);
		
		GLUtils.runWindow(window);
		GLUtils.enableAlphaBlending();
		GLUtils.setClearColor(0, 0, 0, 0);
		
		graphics.init();
		
		//Texture texture = new Texture("acl.texture.test", "test", new Resource("C:/Users/MrKnowname/Desktop/grass.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Texture texture2 = new Texture("acl.texture.test2", "test2", new Resource("C:/Users/MrKnowname/Desktop/test.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Shader shader = new Shader("acl.shader.test", "test", new Resource("C:/Users/MrKnowname/Desktop/testVS.txt", ResourceUtils.DEFAULT_SHADER_LOADER), 
				new Resource("C:/Users/MrKnowname/Desktop/testFS.txt", ResourceUtils.DEFAULT_SHADER_LOADER), new IndexedSet<>(Arrays.asList("vertices", "attrib_textureCoords")));
		
		//Tile tile = new Tile(this, "acl.tile.t1", "t1", texture, false);
		//Tile tile2 = new Tile(this, "acl.tile.t2", "t2", texture2, true);
		
		// ========== //
		Texture red = new Texture("acl.texture.red", "red", new Resource("C:/Users/MrKnowname/Desktop/tiles/redTile.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Texture green = new Texture("acl.texture.green", "green", new Resource("C:/Users/MrKnowname/Desktop/tiles/greenTile.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Texture blue = new Texture("acl.texture.blue", "blue", new Resource("C:/Users/MrKnowname/Desktop/tiles/blueTile.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Texture cyan = new Texture("acl.texture.cyan", "cyan", new Resource("C:/Users/MrKnowname/Desktop/tiles/cyanTile.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Texture pink = new Texture("acl.texture.pink", "pink", new Resource("C:/Users/MrKnowname/Desktop/tiles/pinkTile.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		Texture yellow = new Texture("acl.texture.yellow", "yellow", new Resource("C:/Users/MrKnowname/Desktop/tiles/yellowTile.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
		
		Tile tr = new Tile(this, "acl.tile.tr", "tr", red, false); // SIDE1
		Tile tg = new Tile(this, "acl.tile.tg", "tg", green, false); // GROUND
		Tile tb = new Tile(this, "acl.tile.tb", "tb", blue, false); // WATER
		Tile tc = new Tile(this, "acl.tile.tc", "tc", cyan, false); // SIDE0
		Tile tp = new Tile(this, "acl.tile.tp", "tp", pink, false); // SIDE2
		Tile ty = new Tile(this, "acl.tile.ty", "ty", yellow, false); // SIDE3
		
		Object[] civEvolMapRaw = (Object[]) new Resource("C:/Users/MrKnowname/Desktop/testMap_CivEvol.png", ResourceUtils.DEFAULT_MAP_LOADER).get();
		Tile[] civEvolMap = Utils.interpolateMapFromPixels((int[]) civEvolMapRaw[2], (int) civEvolMapRaw[0], (int) civEvolMapRaw[1], Utils.hashMap(Utils.genericArray(new Pair<Integer, Tile>(Utils.rgbaToHex(255, 0, 0, 255), tr), 
				new Pair<Integer, Tile>(Utils.rgbaToHex(0, 255, 0, 255), tg), new Pair<Integer, Tile>(Utils.rgbaToHex(0, 0, 255, 255), tb), new Pair<Integer, Tile>(Utils.rgbaToHex(0, 255, 255, 255), tc), 
				new Pair<Integer, Tile>(Utils.rgbaToHex(255, 0, 255, 255), tp), new Pair<Integer, Tile>(Utils.rgbaToHex(255, 255, 0, 255), ty))), tb);
		this.world = new World(this, civEvolMap, 16, 16, 20, 16, shader);
		
		try {
			Method registerIndexable = Registry.class.getDeclaredMethod("register", Indexable.class);
			registerIndexable.setAccessible(true);
			registerIndexable.invoke(registry, tr);
			registerIndexable.invoke(registry, tb);
			registerIndexable.invoke(registry, tg);
			registerIndexable.invoke(registry, tc);
			registerIndexable.invoke(registry, tp);
			registerIndexable.invoke(registry, ty);
			
			Field entityMap = World.class.getDeclaredField("entities");
			entityMap.setAccessible(true);
			((Map<Integer, World.Entity>) entityMap.get(world)).put(34, new World.Entity(34, 0, 10 + rand.nextInt(10), rand.nextBoolean()));
			((Map<Integer, World.Entity>) entityMap.get(world)).put(45, new World.Entity(45, 1, 10 + rand.nextInt(10), rand.nextBoolean()));
			((Map<Integer, World.Entity>) entityMap.get(world)).put(210, new World.Entity(210, 2, 10 + rand.nextInt(10), rand.nextBoolean()));
			((Map<Integer, World.Entity>) entityMap.get(world)).put(221, new World.Entity(221, 3, 10 + rand.nextInt(10), rand.nextBoolean()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ========== //
		
		//Tile[] tiles = GFXUtils.uniformTileMap(tile, 64, 64);
		
		//Object[] mapRaw = (Object[]) new Resource("C:/Users/MrKnowname/Desktop/testMap.png", ResourceUtils.DEFAULT_MAP_LOADER).get();
		//Map<Integer, Tile> reference = new HashMap<>();
		//reference.put(Utils.rgbaToHex(255, 0, 0, 255), tile2);
		//reference.put(Utils.rgbaToHex(0, 255, 0, 255), tile);
		//Tile[] map = Utils.interpolateMapFromPixels((int[]) mapRaw[2], (int) mapRaw[0], (int) mapRaw[1], reference, tile);
		
		//this.world = new World(map, 64, 64, 16, 24, shader);
		
		//world.setTile(5, 0, tile2);
		//world.setTile(6, 0, tile2);
		//world.setTile(7, 0, tile2);
		//world.setTile(7, 1, tile2);
		//world.setTile(7, 2, tile2);
		//world.setTile(8, 2, tile2);
		
		Player player = new Player(this, "acl.entity.player", "player", registry.getModel("tile_default"), texture2, shader);
		
		graphics.enableTextureMode();
		
		double frameCap = 1.0 / 60.0;
		double time = Utils.getCurrentTime();
		double unprocessed = 0;
		double frameTime = 0;
		int frames = 0;
		
		// ========== //
		VertexShader vs = new VertexShader("acl.test.shader.vertex", "vertex", new Resource("C:/Users/MrKnowname/Desktop/testVS3.txt", ResourceUtils.DEFAULT_SHADER_LOADER), new IndexedSet<>(Arrays.asList("offset", "color")));
		TessellationControlShader tcs = new TessellationControlShader("acl.test.shader.tc", "tc", new Resource("C:/Users/MrKnowname/Desktop/testTCS.txt", ResourceUtils.DEFAULT_SHADER_LOADER));
		TessellationEvaluationShader tes = new TessellationEvaluationShader("acl.test.shader.te", "te", new Resource("C:/Users/MrKnowname/Desktop/testTES.txt", ResourceUtils.DEFAULT_SHADER_LOADER));
		GeometryShader gs = new GeometryShader("acl.test.shader.geometry", "geometry", new Resource("C:/Users/MrKnowname/Desktop/testGS.txt", ResourceUtils.DEFAULT_SHADER_LOADER));
		FragmentShader fs = new FragmentShader("acl.test.shader.fragment", "fragment", new Resource("C:/Users/MrKnowname/Desktop/testFS3.txt", ResourceUtils.DEFAULT_SHADER_LOADER));
		ShaderProgram program = new ShaderProgram(new com.accele.engine.gfx.shader.Shader[] { vs, tcs, tes, gs, fs });
		
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		float[] vertexPositions = new float[] {
				-0.25f, 0.25f, -0.25f,
				-0.25f, -0.25f, -0.25f,
				0.25f, -0.25f, -0.25f,
				0.25f, -0.25f, -0.25f,
				0.25f, 0.25f, -0.25f,
				-0.25f, 0.25f, -0.25f,
				-0.25f, 0.25f, -0.25f,
				0.25f, 0.25f, -0.25f,
				0.25f, 0.25f, 0.25f,
				0.25f, 0.25f, 0.25f,
				-0.25f, 0.25f, 0.25f,
				-0.25f, 0.25f, -0.25f
		};
		
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		//GL15.glBufferData(GL15.GL_ARRAY_BUFFER, new float[] { 0.25f, -0.25f, 0.5f, 1, -0.25f, -0.25f, 0.5f, 1, 0.25f, 0.25f, 0.5f, 1 }, GL15.GL_STATIC_DRAW);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexPositions, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLUtils.NULL);
		
		Matrix4f projection = new Matrix4f().perspective(50f, internalProperties.getAspectRatio(), 0.1f, 1000f);
		// ========== //
		
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
					internalProperties.setFps(key, frames);
					frames = 0;
				}
				
				// Update
				if (defaultKeyInput.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
					exit(true);
				}
				
				player.onUpdate((float) frameCap);
				world.onUpdate((float) frameCap);
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
				
				FloatBuffer fb = BufferUtils.createFloatBuffer(4);
				fb.put(new float[] { (float) Math.sin(Utils.getCurrentTime()) * 0.5f + 0.5f, (float) Math.cos(Utils.getCurrentTime()) * 0.5f + 0.5f, 0, 1 });
				fb.flip();
				
				FloatBuffer fbDepth = BufferUtils.createFloatBuffer(1);
				fbDepth.put(1);
				fbDepth.flip();
				
				GL30.glClearBufferfv(GL11.GL_COLOR, 0, fb);
				GL30.glClearBufferfv(GL11.GL_DEPTH, 0, fbDepth);
				
				GLUtils.bindShader(program.getId());
				
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				//GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, GLUtils.NULL);
				GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, GLUtils.NULL);
				GL20.glEnableVertexAttribArray(0);
				
				float ct = (float) Utils.getCurrentTime() / 100f;
				float f = ct * (float) Math.PI * 0.1f;
				Matrix4f modelView = new Matrix4f().translate(0, 0, -4).mul(new Matrix4f().translate((float) Math.sin(2.1f * f) * 0.5f, 
						(float) Math.cos(1.7f * f) * 0.5f, (float) Math.sin(1.3f * f) * (float) Math.cos(1.5f * f) * 2))
						.mul(new Matrix4f().rotate(ct * 45, 0, 1, 0)).mul(new Matrix4f().rotate(ct * 81, 1, 0, 0));

				program.setUniformMatrix4f("modelView", modelView);
				program.setUniformMatrix4f("projection", projection);
				
				//FloatBuffer offset = BufferUtils.createFloatBuffer(4);
				//offset.put(new float[] { (float) Math.sin(Utils.getCurrentTime()) * 0.5f, (float) Math.cos(Utils.getCurrentTime()) * 0.6f, 0, 0 });
				//offset.flip();
				
				//GL20.glVertexAttrib4fv(0, offset);
				//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				//GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, 3);
				//GL11.glPointSize(5);
				//GL11.glDrawArrays(GL40.GL_PATCHES, 0, 3);
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 36);
				
				GL20.glDisableVertexAttribArray(0);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLUtils.NULL);
				GLUtils.unbindShader();
				
				GLUtils.swapBuffers(window);
			}
		}
		
		// ========== //
		GL30.glDeleteVertexArrays(vao);
		GLUtils.deleteShaderProgram(program.getId());
		// ========== //
		
		cleanUp();
	}*/
	
	public void run() {
		internalProperties.setRunning(key, true);
		
		GLUtils.runWindow(window);
		GLUtils.enableAlphaBlending();
		GLUtils.setClearColor(0, 0, 0, 0);
		
		graphics.init();
		
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
					internalProperties.setFps(key, frames);
					frames = 0;
				}
				
				// Update
				stateHandler.onUpdate((float) frameCap);
				
				for (Input input : registry.getInputs())
					input.onUpdate((float) frameCap);
				
				GLUtils.pollEvents();
			}
			
			if (canRender) {
				frames++;

				// Render
				GLUtils.clear(internalProperties.getGameType() == 2 ? false : true);
				
				stateHandler.onRender(graphics);
				
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
		internalProperties.setRunning(key, false);
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
	
	public StateHandler getStateHandler() {
		return stateHandler;
	}
	
	public KeyInput getDefaultKeyInput() {
		return defaultKeyInput;
	}
	
	public MouseInput getDefaultMouseInput() {
		return defaultMouseInput;
	}
	
	public Random getRand() {
		return rand;
	}
	
	public long getWindow() {
		return window;
	}
	
	/*public static void main(String[] args) {
		Engine engine = new Engine("Test"); 
		engine.run();
	}*/
	
	// Temporary method
	public World getWorld() {
		return world;
	}
	
}
