package com.accele.engine.gfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.accele.engine.Engine;
import com.accele.engine.Renderable;
import com.accele.engine.Tickable;
import com.accele.engine.util.AxisAlignedBB;
import com.accele.engine.util.GFXUtils;
import com.accele.engine.util.Pair;
import com.accele.engine.util.Utils;

public class World implements Tickable, Renderable {

	//                                                  SIDE0     SIDE1     SIDE2     SIDE3     GROUND    WATER
	private static final int[] TILE_TYPES = new int[] { 0xFF0000, 0xFF00FF, 0xFFFF00, 0x00FFFF, 0x00FF00, 0x0000FF };
	
	private Engine engine;
	private Tile[] tiles;
	private int width;
	private int height;
	private int scale;
	private int view;
	private Matrix4f projection;
	private Shader shader;
	private AxisAlignedBB[] tileBounds;
	
	public World(Engine engine, Tile[] tiles, int width, int height, int scale, int view, Shader shader) {
		this.engine = engine;
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
		
		// ========== //
		entities = new HashMap<>();
	}
	
	public static class Entity {
		
		private final int pos;
		private int side;
		private int strength;
		private final int originalStrength;
		private boolean sick;
		
		public Entity(int pos, int side, int strength, boolean sick) {
			this.pos = pos;
			this.side = side;
			this.strength = strength;
			this.originalStrength = strength;
			this.sick = sick;
		}
		
		public void onUpdate() {
			if (!isDead()) {
				decreaseStrength();
				if (Math.random() < 0.1)
					sick = mutateSick();
			}
		}
		
		private void decreaseStrength() {
			if (sick)
				strength = Math.max(0, strength - 2);
			else
				strength = Math.max(0, strength - 1);
		}
		
		public Entity getClonedChild(int pos) {
			decreaseStrength();
			return new Entity(pos, side, strength, sick);
		}
		
		public Entity getMutatedChild(int pos) {
			decreaseStrength();
			return new Entity(pos, mutateSide(), mutateStrength(), mutateSick());
		}
		
		private int mutateSide() {
			if (Math.random() < 0.1)
				return Utils.randomIntExcluding(0, TILE_TYPES.length, Utils.getIndexForArrayElement(Utils.intToIntObjectArray(TILE_TYPES), side), ThreadLocalRandom.current());
			else
				return side;
		}
		
		private int mutateStrength() {
			return strength + (int) (10 * Math.random());
		}
		
		private boolean mutateSick() {
			if (Math.random() < 0.1)
				return !sick;
			else
				return sick;
		}
		
		public int getPos() {
			return pos;
		}
		
		public int getSide() {
			return side;
		}
		
		public int getStrength() {
			return strength;
		}
		
		public int getOriginalStrength() {
			return originalStrength;
		}
		
		public boolean isDead() {
			return strength == 0;
		}
		
	}
	
	private static final int T_SIDE0 = 0;
	private static final int T_SIDE1 = 1;
	private static final int T_SIDE2 = 2;
	private static final int T_SIDE3 = 3;
	private Map<Integer, Entity> entities;
	
	public int getRandomSpawnPosSurrounding(int surroundingPos) {
		Tile top = Utils.getAtIndexOrElse(tiles, surroundingPos - width, null);
		Tile left = Utils.getAtIndexOrElse(tiles, surroundingPos - 1, null);
		Tile right = Utils.getAtIndexOrElse(tiles, surroundingPos + 1, null);
		Tile bottom = Utils.getAtIndexOrElse(tiles, surroundingPos + width, null);
		
		List<Integer> spaces = new ArrayList<>(4);
		if (top != null)
			spaces.add(surroundingPos - width);
		if (left != null)
			spaces.add(surroundingPos - 1);
		if (right != null)
			spaces.add(surroundingPos + 1);
		if (bottom != null)
			spaces.add(surroundingPos + width);
		
		if (spaces.isEmpty())
			return -1;
		
		return spaces.get(engine.getRand().nextInt(spaces.size()));
	}
	
	public boolean canSpawnChild(int surroundingPos) {
		Tile[] surrounding = new Tile[4];
		surrounding[0] = Utils.getAtIndexOrElse(tiles, surroundingPos - width, null);
		surrounding[1] = Utils.getAtIndexOrElse(tiles, surroundingPos - 1, null);
		surrounding[2] = Utils.getAtIndexOrElse(tiles, surroundingPos + 1, null);
		surrounding[3] = Utils.getAtIndexOrElse(tiles, surroundingPos + width, null);
		
		return (surrounding[0] != null && surrounding[0].getLocalizedId().equals("tg")) || (surrounding[1] != null && surrounding[1].getLocalizedId().equals("tg")) 
				|| (surrounding[2] != null && surrounding[2].getLocalizedId().equals("tg")) || (surrounding[3] != null && surrounding[3].getLocalizedId().equals("tg"));
	}
	
	private int i;
	
	@Override
	public void onUpdate(float delta) {
		//displayMap();
		
		//System.out.println(i++);
		
		if (i >= 25) {
			System.out.println(i);
			i = 0;
			Map<Integer, Entity> immutableMap = Utils.immutableMap(entities);
			
			for (Map.Entry<Integer, Entity> entry : immutableMap.entrySet()) {
				Entity ent = entry.getValue();
				ent.onUpdate();
				if (ent.isDead()) {
					entities.remove(entry.getKey());
					Pair<Integer, Integer> xyPos = toXYCoords(ent.getPos());
					setTile(xyPos.getFirst(), xyPos.getSecond(), (Tile) engine.getRegistry().get("acl.tile.tg"));
					System.out.println(xyPos.getFirst() + ":" + xyPos.getSecond());
				} else if (canSpawnChild(ent.getPos()) && ThreadLocalRandom.current().nextInt(5) == 0) {
					int childPos = getRandomSpawnPosSurrounding(ent.getPos());
					Entity child = ent.getMutatedChild(childPos);
					entities.put(childPos, child);
					Pair<Integer, Integer> xyPos = toXYCoords(childPos);
					System.out.println(xyPos.getFirst() + "<>" + xyPos.getSecond());
					if (child.getSide() == T_SIDE0)
						setTile(xyPos.getFirst(), xyPos.getSecond(), (Tile) engine.getRegistry().get("acl.tile.tc"));
					else if (child.getSide() == T_SIDE1)
						setTile(xyPos.getFirst(), xyPos.getSecond(), (Tile) engine.getRegistry().get("acl.tile.tr"));
					else if (child.getSide() == T_SIDE2)
						setTile(xyPos.getFirst(), xyPos.getSecond(), (Tile) engine.getRegistry().get("acl.tile.tp"));
					else if (child.getSide() == T_SIDE3)
						setTile(xyPos.getFirst(), xyPos.getSecond(), (Tile) engine.getRegistry().get("acl.tile.ty"));
				}
			}
		}
	}
	
	public Pair<Integer, Integer> toXYCoords(int coord) {
		int x = 0;
		int y = 0;
		while (coord >= 0) {
			coord -= width;
			y++;
		}
		
		x = coord + width;
		y--;
		
		return new Pair<>(x, y);
	}
	
	private void displayMap() {
		System.out.println("======================== MAP =========================");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				System.out.print(getTile(x, y).getLocalizedId() + " ");
			}
			System.out.print("\n");
		}
		System.out.println("======================================================");
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
