package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.accele.engine.Engine;
import com.accele.engine.Indexable;
import com.accele.engine.SubRegistry;
import com.accele.engine.gfx.Graphics;
import com.accele.engine.gfx.Model;
import com.accele.engine.gfx.Texture;
import com.accele.engine.gfx.shader.FragmentShader;
import com.accele.engine.gfx.shader.ShaderProgram;
import com.accele.engine.gfx.shader.VertexShader;
import com.accele.engine.state.State;
import com.accele.engine.util.GLUtils;
import com.accele.engine.util.IndexedSet;
import com.accele.engine.util.Resource;
import com.accele.engine.util.ResourceUtils;
import com.accele.engine.util.Transformation;
import com.accele.engine.util.Utils;

public class Test {

	public static class GameState extends State {

		//private ShaderProgramAggregate spa;
		private ShaderProgram program;
		private Transformation trans;
		private Model tile;
		private Texture texture;
		
		public GameState(Engine engine, String registryId, String localizedId) {
			super(engine, registryId, localizedId);
			
			VertexShader vs = new VertexShader("acl.test.shader.vertex", "vertex", new Resource("C:/Users/MrKnowname/Desktop/testVS3.txt", ResourceUtils.DEFAULT_SHADER_LOADER), new IndexedSet<>(Arrays.asList("position", "textureCoordinates")));
			FragmentShader fs = new FragmentShader("acl.test.shader.fragment", "fragment", new Resource("C:/Users/MrKnowname/Desktop/testFS3.txt", ResourceUtils.DEFAULT_SHADER_LOADER));
			
			this.program = new ShaderProgram("acl.test.sp.sp1", "sp1", new com.accele.engine.gfx.shader.Shader[] { vs, fs });
			this.trans = new Transformation();//.rotate(0, 0, (float) Math.toRadians(180));
			this.tile = new Model("acl.model.tile_default", "tile_default", 
					new float[] { -1, 1, 0, 1, 1, 0, 1, -1, 0, -1, -1, 0 }, 
					new int[] { 0, 1, 2, 2, 3, 0 }, 
					new float[] { 0, 0, 1, 0, 1, 1, 0, 1 }, 3);
			this.texture = new Texture("acl.test.texture.test", "test", new Resource("C:/Users/MrKnowname/Desktop/test.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2));
			//ShaderProgram sp1 = new ShaderProgram("acl.test.sp.sp1", "sp1", new com.accele.engine.gfx.shader.Shader[] { fs }, false, true);
			
			//Map<ShaderProgram, Class<? extends com.accele.engine.gfx.shader.Shader>[]> programs = new HashMap<>();
			//programs.put(sp0, Utils.genericArray(VertexShader.class));
			//programs.put(sp1, Utils.genericArray(FragmentShader.class));
			
			//ShaderProgramAggregate spa = new ShaderProgramAggregate(programs);
			
			//this.spa = spa;
		}

		@Override
		public void onUpdate(float delta) {
			if (engine.getDefaultKeyInput().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
				engine.exit(true);
			}
		}

		@Override
		public void onRender(Graphics g) {
			program.bind();
			
			float rx = ((float) (Utils.getCurrentTime() / 100000000f * Math.toRadians(180)));
			float ry = ((float) (Utils.getCurrentTime() / 100000000f * Math.toRadians(180)));
			float rz = ((float) (Utils.getCurrentTime() / 100000000f * Math.toRadians(180)));
			
			program.setUniformMatrix4f("modelView", trans.rotate(rx, ry, rz).getTransformationMatrix());
			
			g.drawModel(tile, texture);
			
			program.unbind();
		}

		@Override
		public void onInit() {
			
		}

		@Override
		public void onExit() {
			program.cleanUp();
			GLUtils.deleteModel(tile.getVertexId(), tile.getIndexId(), tile.getTextureId());
			GLUtils.deleteTexture(texture.getTextureId());
		}
		
	}
	
	public static class Tool implements Indexable {

		private String registryId;
		private String localizedId;
		
		public Tool(String registryId, String localizedId) {
			this.registryId = registryId;
			this.localizedId = localizedId;
		}
		
		@Override
		public String getRegistryId() {
			return registryId;
		}

		@Override
		public String getLocalizedId() {
			return localizedId;
		}
		
	}
	
	public static class GameRegistry extends SubRegistry {
		
		private Map<String, Tool> tools;
		
		public GameRegistry() {
			super("acl.test.subregistry.game", "game");
			this.tools = new HashMap<>();
		}
		
		@Override
		public <T extends Indexable> void register(T entry) {
			if (!canRegister(entry)) {
				System.err.println("Could not register entry \"" + entry.getRegistryId() + "\".");
				return;
			}
			
			if (entry instanceof Tool)
				tools.put(entry.getLocalizedId(), (Tool) entry);
		}
		
		public Tool getTool(String localizedId) {
			return tools.get(localizedId);
		}
		
		public Tool[] getTools() {
			return tools.values().toArray(new Tool[tools.size()]);
		}
		
	}
	
	public static void main(String[] args) {
		Engine engine = new Engine();
		
		System.out.println(engine.getInternalProperties().getAspectRatio());
		
		engine.getRegistry().register(new GameRegistry());
		engine.getRegistry().getSubRegistry("game").register(new Tool("acl.test.tool.t1", "t1"));
		System.out.println(engine.getRegistry().getSubRegistry("game", GameRegistry.class).getTool("t1").getRegistryId());
		engine.getInternalProperties().setTitle("Test Application with ACLEngine v" + engine.getInternalProperties().getVersion());
		
		Transformation trans = new Transformation().rotate(0, 0, (float) Math.toRadians(180));
		Vector4f result = new Vector4f(1, 0, 0, 1).mul(trans.getTransformationMatrix());
		System.out.printf("%f, %f, %f\n", result.x, result.y, result.z);
		
		State state = new GameState(engine, "acl.test.state.game", "game");
		engine.getRegistry().register(state);
		engine.getStateHandler().setCurrentState(false, true, state);
		engine.run();
	}
	
}
