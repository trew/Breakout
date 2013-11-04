package se.samuelandersson.breakout.systems;

import se.samuelandersson.breakout.Group;
import se.samuelandersson.breakout.Settings;
import se.samuelandersson.gdxcommon.actions.Actions;
import se.samuelandersson.gdxcommon.actions.ColorAction;
import se.samuelandersson.gdxcommon.actions.ParallelAction;
import se.samuelandersson.gdxcommon.actions.SequenceAction;
import se.samuelandersson.gdxcommon.components.ActionContainer;
import se.samuelandersson.gdxcommon.components.SpriteComponent;
import se.samuelandersson.gdxcommon.components.VelocityComponent;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CollisionSystem extends VoidEntitySystem {

	@Mapper ComponentMapper<VelocityComponent> vm;
	@Mapper ComponentMapper<SpriteComponent> sm;
	@Mapper ComponentMapper<ActionContainer> am;

	private interface CollisionHandler {
		void handle(Entity a, Entity b, Rectangle intersection);
	}

	private class CollisionGroup {
		ImmutableBag<Entity> groupA;
		ImmutableBag<Entity> groupB;
		CollisionHandler handler;

		private final Rectangle intersection = new Rectangle();

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

		private boolean collides(Entity a, Entity b) {
			SpriteComponent sa = sm.get(a);
			SpriteComponent sb = sm.get(b);

			if (overlaps(sa, sb)) {
				// calculate intersection
				float minX = Math.max(sa.x, sb.x);
				float minY = Math.max(sa.y, sb.y);
				float maxX = Math.min(sa.x + sa.w, sb.x + sb.w);
				float maxY = Math.min(sa.y + sa.h, sb.y + sb.h);
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

	public boolean overlaps(SpriteComponent sa, SpriteComponent sb) {
		return !(sa.x > sb.x + sb.w || sa.x + sa.w < sb.x || sa.y > sb.y + sb.h || sa.y + sa.h < sb.y);
	}

	@Override
	protected void initialize() {
		collisionGroups.add(new CollisionGroup(Group.BALL, Group.BLOCK, new CollisionHandler() {
			@Override
			public void handle(Entity a, Entity b, Rectangle intersection) {
				VelocityComponent ballVelocity = vm.get(a);

				SpriteComponent ballSprite = sm.get(a);
				SpriteComponent blockSprite = sm.get(b);

				// if we hit the top or bottom
				if (intersection.width >= intersection.height) {
					if (ballSprite.y > blockSprite.y) { // top
						ballSprite.y = blockSprite.y + blockSprite.h;
						ballVelocity.y = Math.abs(ballVelocity.y);
					} else { // bottom
						ballSprite.y = blockSprite.y - ballSprite.h;
						ballVelocity.y = -Math.abs(ballVelocity.y);
					}
				}

				// if we hit the left or right side
				if (intersection.height >= intersection.width) {
					if (ballSprite.x > blockSprite.x) { // right side
						ballSprite.x = blockSprite.x + blockSprite.w;
						ballVelocity.x = Math.abs(ballVelocity.x);
					} else { // left side
						ballSprite.x = blockSprite.x - ballSprite.w;
						ballVelocity.x = -Math.abs(ballVelocity.x);
					}
				}

				if (Settings.JUICY) {
					ActionContainer bc = am.get(a);
					ParallelAction par = Actions.parallel();

					SequenceAction colorSeq = Actions.sequence();
					ColorAction to = Actions.color(
					      new Color(MathUtils.random(0.1f, 1f), MathUtils.random(0.1f, 1f), MathUtils.random(0.1f, 1f), 1), 0.15f);
					ColorAction back = Actions.color(Color.WHITE, 0.15f);
					colorSeq.addAction(to).addAction(back);

					SequenceAction seq = Actions.sequence();
					seq.addAction(Actions.scaleTo(1.5f, 1.5f, 0.1f));
					seq.addAction(Actions.scaleTo(0.75f, 0.75f, 0.1f));
					seq.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));

					par.addAction(colorSeq).addAction(seq);
					bc.add(par);

				}
				// ... and delete the block
				if (world.getManager(TagManager.class).getEntity("PLAYER").getId() != b.getId()) {
					world.getManager(GroupManager.class).remove(b, Group.BLOCK);

					if (Settings.JUICY) {
						ActionContainer ac = am.get(b);
						SequenceAction seq = Actions.sequence();
						float startX = MathUtils.random(1.1f, 1.3f);
						float startY = MathUtils.random(1.1f, 1.3f);
						seq.addAction(Actions.scaleTo(startX, startY, 0.1f));
						seq.addAction(Actions.scaleTo(0f, 0f, 0.2f));
						seq.addAction(Actions.remove());
						ac.add(seq);
					} else {
						b.deleteFromWorld();
					}
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
