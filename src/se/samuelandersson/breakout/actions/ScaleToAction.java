package se.samuelandersson.breakout.actions;

import com.artemis.ComponentType;
import com.badlogic.gdx.math.Interpolation;

import se.samuelandersson.breakout.components.Sprite;

public class ScaleToAction extends BaseAction {

	protected float startX;
	protected float startY;
	protected float endX;
	protected float endY;

	protected float duration;
	protected float time;
	protected boolean completed = false;
	protected Interpolation interpolation;

	protected ComponentType spatialType;
	protected Sprite sprite;

	ScaleToAction(float scaleX, float scaleY, float duration, Interpolation interpolation) {
		spatialType = ComponentType.getTypeFor(Sprite.class);
		endX = scaleX;
		endY = scaleY;
		this.duration = duration;
		this.interpolation = interpolation;
	}

	private void setup(Sprite s) {
		startX = s.scaleX;
		startY = s.scaleY;
	}

	@Override
	public boolean update(float delta) {
		Sprite s = (Sprite) entity.getComponent(spatialType);
		if (s == null) return true;
		if (time == 0) setup(s);

		time += delta;
		float percent;
		completed = time >= duration;
		if (completed) {
			percent = 1;
		} else {
			percent = time / duration;
			if (interpolation != null) percent = interpolation.apply(percent);
		}
		s.scaleX = startX + (endX - startX) * percent;
		s.scaleY = startY + (endY - startY) * percent;
		return completed;
	}

}
