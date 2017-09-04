package com.accele.engine.gfx;

import static com.accele.engine.util.GLUtils.bindArrayBuffer;
import static com.accele.engine.util.GLUtils.bindElementArrayBuffer;
import static com.accele.engine.util.GLUtils.disable;
import static com.accele.engine.util.GLUtils.enable;
import static com.accele.engine.util.GLUtils.unbindArrayBuffer;
import static com.accele.engine.util.GLUtils.unbindElementArrayBuffer;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import com.accele.engine.Engine;
import com.accele.engine.util.ACLEngineAccessProvider;
import com.accele.engine.util.Utils;

public class Graphics {
	
	private Engine engine;
	private Model defaultTileModel;
	
	public Graphics(Engine engine, Object key) {
		this.engine = engine;
		
		if (!ACLEngineAccessProvider.canAccess(key))
			throw ACLEngineAccessProvider.accessError(this.getClass().getSimpleName());
	}
	
	public void init() {
		engine.getRegistry().register(this.defaultTileModel = new Model("acl.model.tile_default", "tile_default", 
				new float[] { -1, 1, 0, 1, 1, 0, 1, -1, 0, -1, -1, 0 }, 
				new int[] { 0, 1, 2, 2, 3, 0 }, 
				new float[] { 0, 0, 1, 0, 1, 1, 0, 1 }, 3));
	}
	
	public void drawModel(Model model) {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		bindArrayBuffer(model.getVertexId());
		glVertexAttribPointer(0, model.getDimensions(), GL_FLOAT, false, 0, 0);
		
		bindArrayBuffer(model.getTextureId());
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		bindElementArrayBuffer(model.getIndexId());
		glDrawElements(GL_TRIANGLES, model.getDrawCount(), GL_UNSIGNED_INT, 0);
		
		unbindElementArrayBuffer();
		unbindArrayBuffer();
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	
	public void drawModel(Model model, Texture texture) {
		texture.bind(0);
		
		drawModel(model);
		
		texture.unbind();
	}
	
	public void drawTile(Tile tile, Matrix4f transformation, Shader shader, Matrix4f world) {
		shader.bind();
		//tile.getTexture().bind(0);
		
		Matrix4f target = new Matrix4f();
		
		engine.getCamera().getProjection().mul(world, target);
		target.mul(transformation);
		
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", target);
		
		drawModel(defaultTileModel, tile.getTexture());
		
		//tile.getTexture().unbind();
		shader.unbind();
	}
	
	public void drawWorld(World world, Shader shader) {
		//for (int i = 0; i < world.getHeight(); i++)
			//for (int j = 0; j < world.getWidth(); j++)
				//drawTile(world.getTiles()[j + i * world.getWidth()], Utils.transformationMatrix(Utils.toTileCoordinates(new Vector3f(j, -i, 0)), new Vector3f(0), new Vector3f(1)), shader, world.getProjection());
		
		int x = ((int) engine.getCamera().getPosition().x + (engine.getInternalProperties().getScreenWidth() / 2)) / (world.getScale() * 2);
		int y = ((int) engine.getCamera().getPosition().y - (engine.getInternalProperties().getScreenHeight() / 2)) / (world.getScale() * 2);
		
		for (int i = 0; i < world.getView(); i++) {
			for (int j = 0; j < world.getView(); j++) {
				Tile t = world.getTile(i - x, j + y);
				if (t != null)
					drawTile(t, Utils.transformationMatrix(Utils.toTileCoordinates(new Vector3f(i - x, -j - y, 0)), new Vector3f(0), new Vector3f(1)), shader, world.getProjection());
			}
		}
	}
	
	public void enableTextureMode() {
		enable(GL11.GL_TEXTURE_2D);
	}
	
	public void disableTextureMode() {
		disable(GL11.GL_TEXTURE_2D);
	}
	
}
