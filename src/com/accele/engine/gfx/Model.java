package com.accele.engine.gfx;

import com.accele.engine.Indexable;
import com.accele.engine.util.GLUtils;
import com.accele.engine.util.Pair;

public class Model implements Indexable {

	private String registryId;
	private String localizedId;
	private int drawCount;
	private int vertexId;
	private int indexId;
	private int textureId;
	private int dimensions;
	
	public Model(String registryId, String localizedId, float[] vertices, int[] indices, float[] textureCoords, int dimensions) {
		this.registryId = registryId;
		this.localizedId = localizedId;
		this.drawCount = indices.length;
		Pair<Pair<Integer, Integer>, Integer> ids = GLUtils.initModel(vertices, indices, textureCoords);
		this.vertexId = ids.getFirst().getFirst();
		this.indexId = ids.getFirst().getSecond();
		this.textureId = ids.getSecond();
		this.dimensions = dimensions;
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	@Override
	public String getLocalizedId() {
		return localizedId;
	}
	
	public int getDrawCount() {
		return drawCount;
	}
	
	public int getVertexId() {
		return vertexId;
	}
	
	public int getIndexId() {
		return indexId;
	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public int getDimensions() {
		return dimensions;
	}
	
}
