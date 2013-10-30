package se.samuelandersson.breakout;

import se.samuelandersson.breakout.components.ActionComponent;
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

		e.addComponent(new Position(Gdx.graphics.getWidth() / 2 - Constants.PADDLE_WIDTH / 2, 10));
		e.addComponent(new Velocity(0, 0));
		e.addComponent(new Sprite(Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Color.WHITE));
		e.addComponent(new ActionComponent());

		world.getManager(TagManager.class).register("PLAYER", e);
		world.getManager(GroupManager.class).add(e, Group.BLOCK);
		return e;
	}

	public static Entity createBall() {
		Entity e = world.createEntity();

		e.addComponent(new Position(Gdx.graphics.getWidth() / 2 - 5, Gdx.graphics.getHeight() / 2 - 5));
		e.addComponent(new Velocity(50, -100));
		e.addComponent(new Sprite(Constants.BALL_WIDTH, Constants.BALL_HEIGHT));
		e.addComponent(new ActionComponent());

		world.getManager(GroupManager.class).add(e, Group.BALL);

		return e;
	}

	public static Entity createBlock(float x, float y, float w, float h) {
		return createBlock(x, y, w, h, Color.WHITE);
	}

	public static Entity createBlock(float x, float y, float w, float h, Color color) {
		Entity e = world.createEntity();

		e.addComponent(new Position(x, y));
		e.addComponent(new Velocity(0, 0));
		e.addComponent(new Sprite(w, h, color));
		e.addComponent(new ActionComponent());

		world.getManager(GroupManager.class).add(e, Group.BLOCK);

		return e;
	}
}
