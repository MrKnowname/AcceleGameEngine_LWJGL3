package com.accele.engine.io;

import com.accele.engine.Engine;
import com.accele.engine.util.GLUtils;

public class KeyInput extends Input {

	private boolean[] keys;
	
	public KeyInput(Engine engine) {
		super(engine, "acl.input.key", "key");
		this.keys = new boolean[GLUtils.getKeyAmount()];
		for (int i = 0; i < keys.length; i++)
			keys[i] = false;
	}
	
	public boolean isKeyDown(int key) {
		return GLUtils.isKeyDown(engine.getWindow(), key);
	}
	
	public boolean isKeyPressed(int key) {
		return isKeyDown(key) && !keys[key];
	}
	
	public boolean isKeyReleased(int key) {
		return !isKeyDown(key) && keys[key];
	}

	@Override
	public void onUpdate(float delta) {
		for (int i = 32; i < keys.length; i++)
			keys[i] = isKeyDown(i);
	}

}
