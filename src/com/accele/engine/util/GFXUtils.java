package com.accele.engine.util;

import java.lang.reflect.Field;
import java.util.Objects;

import org.joml.Vector3f;

import com.accele.engine.Engine;
import com.accele.engine.gfx.Camera;
import com.accele.engine.gfx.Tile;
import com.accele.engine.gfx.World;

public final class GFXUtils {

	private GFXUtils() {}
	
	private static Tile copyTile(Tile tile) {
		Objects.requireNonNull(tile);
		try {
			Field engine = Tile.class.getDeclaredField("engine");
			engine.setAccessible(true);
			return new Tile((Engine) engine.get(tile), tile.getRegistryId(), tile.getLocalizedId(), tile.getTexture(), tile.isSolid());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Tile setTile(Tile[] tiles, int x, int y, int mapWidth, int mapHeight, Tile newTile) {
		Tile result = copyTile(tiles[x + y * mapWidth]);
		tiles[x + y * mapWidth] = newTile;
		return result;
	}
	
	public static void lockCameraToWorld(Camera camera, World world, int screenWidth, int screenHeight) {
		Vector3f pos = camera.getPosition();
		
		int width = -world.getWidth() * world.getScale() * 2;
		int height = world.getHeight() * world.getScale() * 2;
		
		if (pos.x > -(screenWidth / 2) + world.getScale())
			pos.x = -(screenWidth / 2) + world.getScale();
		
		if (pos.x < width + screenWidth / 2 + world.getScale())
			pos.x = width + screenWidth / 2 + world.getScale();
		
		if (pos.y < screenHeight / 2 - world.getScale())
			pos.y = screenHeight / 2 - world.getScale();
		
		if (pos.y > height - screenHeight / 2 - world.getScale())
			pos.y = height - screenHeight / 2 - world.getScale();
	}
	
	public static Tile[] uniformTileMap(Tile tile, int width, int height) {
		Tile[] result = new Tile[width * height];
		for (int i = 0; i < result.length; i++)
			result[i] = tile;
		return result;
	}
	
}
