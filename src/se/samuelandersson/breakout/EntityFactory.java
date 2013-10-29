package se.samuelandersson.breakout;

import se.samuelandersson.breakout.components.Position;
import se.samuelandersson.breakout.components.Sprite;
import se.samuelandersson.breakout.components.Velocity;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class EntityFactory {

	private static World world;

	public static void setWorld(World world) {
		EntityFactory.world = world;
	}

	public static Entity createPlayer() {
		Entity e = world.createEntity();

		e.addComponent(new Position(Gdx.graphics.getWidth() / 2 - 50, 10));
		e.addComponent(new Velocity(0, 0));
		e.addComponent(new Sprite(100, 10, Color.YELLOW));

		world.getManager(TagManager.class).register("PLAYER", e);
		world.getManager(GroupManager.class).add(e, "PLAYER");
		return e;
	}

	public static Entity createBall() {
		Entity e = world.createEntity();

		e.addComponent(new Position(Gdx.graphics.getWidth() / 2 - 5, 50));
		e.addComponent(new Velocity(50, -50));
		e.addComponent(new Sprite(10, 10));

		world.getManager(TagManager.class).register("BALL", e);
		world.getManager(GroupManager.class).add(e, "BALL");

		return e;
	}

	public static Entity createBox(float x, float y, float w, float h) {
		return createBox(x, y, w, h, Color.WHITE);
	}

	public static Entity createBox(float x, float y, float w, float h, Color color) {
		Entity e = world.createEntity();

		e.addComponent(new Position(x, y));
		e.addComponent(new Velocity(0, 0));
		e.addComponent(new Sprite(w, h, color));

		world.getManager(GroupManager.class).add(e, "BOX");

		return e;
	}

}
