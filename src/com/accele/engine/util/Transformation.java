package com.accele.engine.util;

import org.joml.Matrix4f;

public final class Transformation {

	private Matrix4f transformation;
	
	public Transformation(float x, float y, float z, float rx, float ry, float rz, float sx, float sy, float sz) {
		this.transformation = new Matrix4f();
		transformation.translate(x, y, z);
		transformation.rotate(rx, 1, 0, 0);
		transformation.rotate(ry, 0, 1, 0);
		transformation.rotate(rz, 0, 0, 1);
		transformation.scale(sx, sy, sz);
	}
	
	public Transformation() {
		this(0, 0, 0, 0, 0, 0, 1, 1, 1);
	}
	
	public Transformation translate(float x, float y, float z) {
		transformation.translate(x, y, z);
		return this;
	}
	
	public Transformation rotate(float rx, float ry, float rz) {
		transformation.rotate(rx, 1, 0, 0);
		transformation.rotate(ry, 0, 1, 0);
		transformation.rotate(rz, 0, 0, 1);
		return this;
	}
	
	public Transformation scale(float sx, float sy, float sz) {
		transformation.scale(sx, sy, sz);
		return this;
	}
	
	public Matrix4f getTransformationMatrix() {
		return transformation;
	}
	
}
