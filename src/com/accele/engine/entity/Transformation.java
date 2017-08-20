package com.accele.engine.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

	public Vector3f pos;
	public Vector3f scale;
	
	public Transformation() {
		this(new Vector3f(), new Vector3f(1));
	}
	
	public Transformation(Vector3f pos, Vector3f scale) {
		this.pos = pos;
		this.scale = scale;
	}
	
	public Matrix4f getProjection(Matrix4f target) {
		target.scale(scale);
		target.translate(pos);
		return target;
	}
	
}
