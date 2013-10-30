package se.samuelandersson.breakout.systems;

import se.samuelandersson.breakout.Group;
import se.samuelandersson.breakout.components.Position;
import se.samuelandersson.breakout.components.Sprite;
import se.samuelandersson.breakout.components.Velocity;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CollisionSystem extends VoidEntitySystem {

	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Sprite> sm;

	private interface CollisionHandler {
		void handle(Entity a, Entity b, Rectangle intersection);
	}

	private class CollisionGroup {
		ImmutableBag<Entity> groupA;
		ImmutableBag<Entity> groupB;
		CollisionHandler handler;

		private final Rectangle intersection = new Rectangle();

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

					if (collides(a, b)) {
						handler.handle(a, b, new Rectangle(intersection));
					}
				}
			}
		}

		private boolean overlaps(Position pa, Sprite sa, Position pb, Sprite sb) {
			return !(pa.x > pb.x + sb.w || pa.x + sa.w < pb.x || pa.y > pb.y + sb.h || pa.y + sa.h < pb.y);
		}

		private boolean collides(Entity a, Entity b) {
			Position pa = pm.get(a);
			Sprite sa = sm.get(a);

			Position pb = pm.get(b);
			Sprite sb = sm.get(b);

			if (overlaps(pa, sa, pb, sb)) {
				// calculate intersection
				float minX = Math.max(pa.x, pb.x);
				float minY = Math.max(pa.y, pb.y);
				float maxX = Math.min(pa.x + sa.w, pb.x + sb.w);
				float maxY = Math.min(pa.y + sa.h, pb.y + sb.h);
				intersection.set(minX, minY, maxX - minX, maxY - minY);
				return true;
			}
			return false;
		}
	}

	private Array<CollisionGroup> collisionGroups;

	public CollisionSystem() {
		collisionGroups = new Array<CollisionGroup>();
	}

	@Override
	protected void initialize() {
		collisionGroups.add(new CollisionGroup(Group.BALL, Group.BOX, new CollisionHandler() {
			@Override
			public void handle(Entity a, Entity b, Rectangle intersection) {
				Velocity ballVelocity = vm.get(a);

				// if we hit the top or bottom
				if (intersection.width >= intersection.height) {
					// change Y velocity on ball
					ballVelocity.y = -ballVelocity.y;
				}

				// if we hit the left or right side
				if (intersection.height >= intersection.width) {
					// change X velocity on ball
					ballVelocity.x = -ballVelocity.x;
				}

				// ... and delete the box
				// TODO: delete it in a fancy and juicy way
				b.deleteFromWorld();
			}
		}));

		collisionGroups.add(new CollisionGroup(Group.BALL, Group.PLAYER, new CollisionHandler() {
			@Override
			public void handle(Entity a, Entity b, Rectangle intersection) {
				Velocity ballVelocity = vm.get(a);

				// if we hit the top or bottom
				if (intersection.width >= intersection.height) {
					// change Y velocity on ball
					ballVelocity.y = -ballVelocity.y;
				}

				// if we hit the left or right side
				if (intersection.height >= intersection.width) {
					// change X velocity on ball
					ballVelocity.x = -ballVelocity.x;
				}
			}
		}));
	}

	@Override
	protected void processSystem() {
		for (CollisionGroup group : collisionGroups)
			group.process();
	}
}
