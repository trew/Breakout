package se.samuelandersson.breakout.systems;

import se.samuelandersson.breakout.components.Position;
import se.samuelandersson.breakout.components.Sprite;
import se.samuelandersson.breakout.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;

public class MovementSystem extends EntityProcessingSystem {

	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Sprite> sm;

	public MovementSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class, Sprite.class));
	}

	@Override
	protected void process(Entity e) {
		float delta = world.getDelta();

		Position p = pm.get(e);
		Velocity v = vm.get(e);

		p.x += v.x * delta;
		p.y += v.y * delta;

		// FIXME: this is collision detection; should not be done here.
		Sprite s = sm.get(e);
		if (p.x + s.w > Gdx.graphics.getWidth()) v.x = -v.x;
		if (p.x < 0) v.x = -v.x;

		if (p.y + s.h > Gdx.graphics.getHeight()) v.y = -v.y;
		if (p.y < 0) v.y = -v.y;
	}

}
