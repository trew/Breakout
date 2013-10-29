package se.samuelandersson.breakout.systems;

import se.samuelandersson.breakout.components.Position;
import se.samuelandersson.breakout.components.Sprite;
import se.samuelandersson.breakout.components.Velocity;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CollisionSystem extends VoidEntitySystem {

	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Sprite> sm;

	private interface CollisionHandler {
		void handle(Entity a, Entity b);
	}

	private class CollisionGroup {
		ImmutableBag<Entity> groupA;
		ImmutableBag<Entity> groupB;
		CollisionHandler handler;

		CollisionGroup(ImmutableBag<Entity> groupA, ImmutableBag<Entity> groupB, CollisionHandler handler) {
			this.groupA = groupA;
			this.groupB = groupB;
			this.handler = handler;
		}

		CollisionGroup(String groupA, String groupB, CollisionHandler handler) {
			this.groupA = world.getManager(GroupManager.class).getEntities(groupA);
			this.groupB = world.getManager(GroupManager.class).getEntities(groupB);
			this.handler = handler;
		}

		public void process() {
			for (int i = 0; i < groupA.size(); i++) {
				Entity a = groupA.get(i);
				for (int j = 0; j < groupB.size(); j++) {
					Entity b = groupB.get(j);

					if (collides(a, b)) handler.handle(a, b);
				}
			}
		}

		private boolean collides(Entity a, Entity b) {
			Position pa = pm.get(a);
			Sprite sa = sm.get(a);
			
			Position pb = pm.get(b);
			Sprite sb = sm.get(b);

			Rectangle ra = new Rectangle();
			ra.set(pa.x, pa.y, sa.w, sa.h);
			
			Rectangle rb = new Rectangle();
			rb.set(pb.x, pb.y, sb.w, sb.h);
			
			return Intersector.intersectRectangles(ra, rb);
		}
	}

	private Array<CollisionGroup> collisionGroups;

	public CollisionSystem() {
		collisionGroups = new Array<CollisionGroup>();
	}

	@Override
	protected void initialize() {
		collisionGroups.add(new CollisionGroup("BALL", "BOX", new CollisionHandler() {
			@Override
			public void handle(Entity a, Entity b) {
				// change Y velocity on ball
				Velocity ballVel = vm.get(a);
				ballVel.y = -ballVel.y;

				// ... and delete the box
				b.deleteFromWorld();
			}
		}));

		collisionGroups.add(new CollisionGroup("BALL", "PLAYER", new CollisionHandler() {
			@Override
			public void handle(Entity a, Entity b) {
				// change Y velocity on ball
				Velocity ballVel = vm.get(a);
				ballVel.y = -ballVel.y;
			}
		}));
	}

	@Override
	protected void processSystem() {
		for (CollisionGroup group : collisionGroups)
			group.process();

	}

}
