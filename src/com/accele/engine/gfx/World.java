package com.accele.engine.gfx;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.accele.engine.Renderable;
import com.accele.engine.util.AxisAlignedBB;
import com.accele.engine.util.GFXUtils;

public class World implements Renderable {

	private Tile[] tiles;
	private int width;
	private int height;
	private int scale;
	private int view;
	private Matrix4f projection;
	private Shader shader;
	private AxisAlignedBB[] tileBounds;
	
	public World(Tile[] tiles, int width, int height, int scale, int view, Shader shader) {
		this.tiles = tiles;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.view = view;
		this.shader = shader;
		this.projection = new Matrix4f().setTranslation(new Vector3f(0)).scale(scale);
		this.tileBounds = new AxisAlignedBB[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (tiles[x + y * width].isSolid())
					tileBounds[x + y * width] = new AxisAlignedBB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
			}
		}
	}
	
	@Override
	public void onRender(Graphics g) {
		g.drawWorld(this, shader);
	}
	
	public Tile setTile(int x, int y, Tile newTile) {
		if (newTile.isSolid())
			tileBounds[x + y * width] = new AxisAlignedBB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
		else
			tileBounds[x + y * width] = null;
		
		return GFXUtils.setTile(tiles, x, y, width, height, newTile);
	}
	
	public Tile getTile(int x, int y) {
		try {
			return tiles[x + y * width];
		} catch (Exception e) {
			return null;
		}
	}
	
	public AxisAlignedBB getTileAABB(int x, int y) {
		try {
			return tileBounds[x + y * width];
		} catch (Exception e) {
			return null;
		}
	}
	
	public Tile[] getTiles() {
		return tiles;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getScale() {
		return scale;
	}
	
	public int getView() {
		return view;
	}
	
	public Matrix4f getProjection() {
		return projection;
	}
	
}
