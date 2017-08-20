package com.accele.engine.impl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.accele.engine.Engine;
import com.accele.engine.entity.Entity;
import com.accele.engine.entity.Transformation;
import com.accele.engine.gfx.Animation;
import com.accele.engine.gfx.Graphics;
import com.accele.engine.gfx.Model;
import com.accele.engine.gfx.Shader;
import com.accele.engine.gfx.Texture;
import com.accele.engine.util.AxisAlignedBB;
import com.accele.engine.util.Resource;
import com.accele.engine.util.ResourceUtils;
import com.accele.engine.util.Utils;

public class Player extends Entity {

	private Model model;
	private Texture texture;
	private Shader shader;
	private Transformation transformation;
	private AxisAlignedBB bounds;
	
	private Animation animation;

	public Player(Engine engine, String registryId, String localizedId, Model model, Texture texture, Shader shader) {
		super(engine, registryId, localizedId);
		this.model = model;
		this.texture = texture;
		this.shader = shader;
		this.transformation = new Transformation(new Vector3f(), new Vector3f(16, 16, 1));
		this.bounds = new AxisAlignedBB(new Vector2f(transformation.pos.x, transformation.pos.y), new Vector2f(1, 1));
		
		//engine.getRegistry().register(new Texture("acl.texture.p0", "p0", new Resource("C:/Users/MrKnowname/Desktop/AOVTest/res/textures/player_down0.png", ResourceUtils.DEFAULT_TEXTURE_LOADER2)));
		engine.getRegistry().register(new Texture("acl.texture.p1", "p1", new Resource("C:/Users/MrKnowname/Desktop/AOVTest/res/textures/player_down1.png", ResourceUtils.DEFAULT_TEXTURE_LOADER)));
		engine.getRegistry().register(new Texture("acl.texture.p2", "p2", new Resource("C:/Users/MrKnowname/Desktop/AOVTest/res/textures/player_down2.png", ResourceUtils.DEFAULT_TEXTURE_LOADER)));
		this.animation = new Animation("acl.animation.player", "player", new Texture[] { /*engine.getRegistry().getTexture("p0"),*/ engine.getRegistry().getTexture("p1"), engine.getRegistry().getTexture("p2") }, 5);
	}
	
	private static final int SPEED_FACTOR = 10;

	@Override
	public void onUpdate(float delta) {
		if (engine.getDefaultKeyInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			transformation.pos.add(new Vector3f(0, SPEED_FACTOR * delta, 0));
		}
		
		if (engine.getDefaultKeyInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			transformation.pos.add(new Vector3f(-SPEED_FACTOR * delta, 0, 0));
		}
		
		if (engine.getDefaultKeyInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			transformation.pos.add(new Vector3f(0, -SPEED_FACTOR * delta, 0));
		}
		
		if (engine.getDefaultKeyInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			transformation.pos.add(new Vector3f(SPEED_FACTOR * delta, 0, 0));
		}
		
		bounds.getCenter().set(transformation.pos.x, transformation.pos.y);
		
		AxisAlignedBB[] radius = new AxisAlignedBB[25];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Vector2f tilePos = Utils.entityToTileCoordinates(Utils.xy(transformation.pos), i, j);
				radius[i + j * 5] = engine.getWorld().getTileAABB((int) tilePos.x, (int) tilePos.y);
			}
		}
		
		AxisAlignedBB box = null;
		for (int i = 0; i < radius.length; i++) {
			if (radius[i] != null) {
				if (box == null)
					box = radius[i];
				
				Vector2f len1 = box.getCenter().sub(transformation.pos.x, transformation.pos.y, new Vector2f());
				Vector2f len2 = radius[i].getCenter().sub(transformation.pos.x, transformation.pos.y, new Vector2f());
				
				if (len1.lengthSquared() > len2.lengthSquared())
					box = radius[i];
			}
		}
		
		if (box != null) {
			bounds.realign(box);
			transformation.pos.set(bounds.getCenter(), 0);
			
			for (int i = 0; i < radius.length; i++) {
				if (radius[i] != null) {
					if (box == null)
						box = radius[i];
					
					Vector2f len1 = box.getCenter().sub(transformation.pos.x, transformation.pos.y, new Vector2f());
					Vector2f len2 = radius[i].getCenter().sub(transformation.pos.x, transformation.pos.y, new Vector2f());
					
					if (len1.lengthSquared() > len2.lengthSquared())
						box = radius[i];
				}
			}
			
			if (box != null) {
				bounds.realign(box);
				transformation.pos.set(bounds.getCenter(), 0);
			}
		}
		
		engine.getCamera().getPosition().lerp(transformation.pos.mul(-engine.getWorld().getScale(), new Vector3f()), 0.05f);
		//engine.getCamera().setPosition(transformation.pos.mul(-engine.getWorld().getScale(), new Vector3f()));
	}

	@Override
	public void onRender(Graphics g) {
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", transformation.getProjection(engine.getCamera().getProjection()));
		animation.bind();
		g.drawModel(model);
		animation.unbind();
		shader.unbind();
	}

}
