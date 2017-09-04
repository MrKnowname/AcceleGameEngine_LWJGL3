package com.accele.engine.state;

import com.accele.engine.Renderable;
import com.accele.engine.Tickable;
import com.accele.engine.gfx.Graphics;
import com.accele.engine.util.ACLEngineAccessProvider;

public final class StateHandler implements Tickable, Renderable {

	private State currentState;
	
	public StateHandler(Object key) {
		if (!ACLEngineAccessProvider.canAccess(key))
			throw ACLEngineAccessProvider.accessError(this.getClass().getSimpleName());
	}

	@Override
	public void onRender(Graphics g) {
		currentState.onRender(g);
	}

	@Override
	public void onUpdate(float delta) {
		currentState.onUpdate(delta);
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void setCurrentState(boolean exitOld, boolean initNew, State newState) {
		if (exitOld && this.currentState != null)
			this.currentState.onExit();
		if (initNew && newState != null)
			newState.onInit();
		this.currentState = newState;
	}
	
}
