package com.accele.engine.util;

import org.joml.Vector2f;

public final class AxisAlignedBB {

	private Vector2f center;
	private Vector2f halfExtent;
	
	public AxisAlignedBB(Vector2f center, Vector2f halfExtent) {
		this.center = center;
		this.halfExtent = halfExtent;
	}
	
	public Pair<Vector2f, Boolean> getCollision(AxisAlignedBB other) {
		Vector2f distance = other.center.sub(center, new Vector2f());
		distance.x = (float) Math.abs(distance.x);
		distance.y = (float) Math.abs(distance.y);
		
		distance.sub(halfExtent.add(other.halfExtent, new Vector2f()));
		
		return new Pair<>(distance, distance.x < 0 && distance.y < 0);
	}
	
	public boolean intersects(AxisAlignedBB other) {
		return getCollision(other).getSecond();
	}
	
	public void realign(AxisAlignedBB other) {
		Pair<Vector2f, Boolean> data = getCollision(other);
		if (!data.getSecond())
			return;
		
		Vector2f correction = other.center.sub(center, new Vector2f());
		if (data.getFirst().x > data.getFirst().y) {
			if (correction.x > 0)
				center.add(data.getFirst().x, 0);
			else
				center.add(-data.getFirst().x, 0);
		} else {
			if (correction.y > 0)
				center.add(0, data.getFirst().y);
			else
				center.add(0, -data.getFirst().y);
		}
	}

	public Vector2f getCenter() {
		return center;
	}

	public Vector2f getHalfExtent() {
		return halfExtent;
	}
	
}
