package se.samuelandersson.breakout.actions;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;

public class SequenceAction extends BaseAction {

	protected Array<BaseAction> actions;

	SequenceAction() {
		actions = new Array<BaseAction>();
	}

	@Override
	public void setEntity(Entity e) {
		super.setEntity(e);
		for (BaseAction a : actions) {
			a.setEntity(e);
		}
	}

	public SequenceAction addAction(BaseAction a) {
		actions.add(a);
		return this;	
	}

	@Override
	public boolean update(float delta) {
		BaseAction action = actions.first();
		if (action == null) return true;
		if (action.update(delta)) {
			actions.removeValue(action, true);
		}
		return actions.size == 0;
	}
}
