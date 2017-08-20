package com.accele.engine.io;

import com.accele.engine.Engine;
import com.accele.engine.util.GLUtils;

public class MouseInput extends Input {
	
	private boolean[] buttons;
	
	public MouseInput(Engine engine) {
		super(engine, "acl.input.mouse", "mouse");
		this.buttons = new boolean[GLUtils.getMouseButtonAmount()];
		for (int i = 0; i < buttons.length; i++)
			buttons[i] = false;
	}
	
	public boolean isButtonDown(int button) {
		return GLUtils.isMouseButtonDown(engine.getWindow(), button);
	}
	
	public boolean isButtonPressed(int button) {
		return isButtonDown(button) && !buttons[button];
	}
	
	public boolean isKeyReleased(int button) {
		return !isButtonDown(button) && buttons[button];
	}

	@Override
	public void onUpdate(float delta) {
		for (int i = 0; i < buttons.length; i++)
			buttons[i] = isButtonDown(i);
	}

}
