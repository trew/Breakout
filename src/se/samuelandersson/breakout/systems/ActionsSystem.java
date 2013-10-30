package se.samuelandersson.breakout.systems;

import se.samuelandersson.breakout.actions.BaseAction;
import se.samuelandersson.breakout.components.ActionComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.utils.Array;

public class ActionsSystem extends EntityProcessingSystem {

	@Mapper
	ComponentMapper<ActionComponent> am;

	public ActionsSystem() {
		super(Aspect.getAspectForAll(ActionComponent.class));
	}

	@Override
	protected void process(Entity e) {
		ActionComponent actionComponent = am.get(e);
		float delta = world.getDelta();
		Array<BaseAction> entityActions = actionComponent.actions;
		if (entityActions.size == 0) return;

		for (int i = 0; i < entityActions.size; i++) {
			BaseAction action = entityActions.get(i);
			action.setEntity(e);
			boolean completed = action.update(delta);
			if (completed) entityActions.removeIndex(i--);
		}
	}
}
