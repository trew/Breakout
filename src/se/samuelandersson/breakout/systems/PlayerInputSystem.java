package se.samuelandersson.breakout.systems;

import se.samuelandersson.breakout.Constants;
import se.samuelandersson.breakout.GameManager;
import se.samuelandersson.breakout.components.Position;
import se.samuelandersson.breakout.components.Sprite;
import se.samuelandersson.breakout.components.Velocity;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;

public class PlayerInputSystem extends VoidEntitySystem implements InputProcessor {

	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Sprite> sm;

	float state = 0;

	@Override
	protected void processSystem() {
		if (Constants.USE_MOUSE) return;
		Entity e = world.getManager(TagManager.class).getEntity("PLAYER");
		if (e == null) return;

		if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
			state = Gdx.input.getAccelerometerX() / 10f;
			Gdx.app.log("BREAKOUT", "accelerometer state: " + state);
		} else {
			state = Gdx.input.isKeyPressed(Keys.LEFT) ? -1 : Gdx.input.isKeyPressed(Keys.RIGHT) ? 1 : 0;
		}

		Velocity v = vm.get(e);
		v.x = state * 200;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.SPACE) {
			world.getManager(GameManager.class).reset();
		}
		if (keycode == Keys.B) {
			world.getManager(GameManager.class).addBall();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (Constants.USE_MOUSE) {
			Entity e = world.getManager(TagManager.class).getEntity("PLAYER");
			if (e != null) {
				Position p = pm.get(e);
				Sprite s = sm.get(e);
				p.x = screenX - s.w / 2;
			}
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
