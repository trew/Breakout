package se.samuelandersson.breakout.actions;

import com.artemis.Entity;

public abstract class BaseAction {
	protected Entity entity;

	public abstract boolean update(float delta);

	public void setEntity(Entity e) {
		entity = e;
	}
}
