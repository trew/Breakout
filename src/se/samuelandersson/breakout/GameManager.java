package se.samuelandersson.breakout;

import se.samuelandersson.gdxcommon.actions.Actions;
import se.samuelandersson.gdxcommon.actions.ColorAction;
import se.samuelandersson.gdxcommon.actions.ParallelAction;
import se.samuelandersson.gdxcommon.actions.SequenceAction;
import se.samuelandersson.gdxcommon.components.ActionContainer;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class GameManager extends Manager {

	public GameManager() {
	}

	public void addBall() {
		EntityFactory.createBall().addToWorld();
	}

	public void reset() {
		// remove everything
		ImmutableBag<Entity> blocks = world.getManager(GroupManager.class).getEntities(Group.BLOCK);
		ImmutableBag<Entity> balls = world.getManager(GroupManager.class).getEntities(Group.BALL);
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).deleteFromWorld();
		}
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).deleteFromWorld();
		}
		world.getManager(TagManager.class).unregister("PLAYER");

		// recreate the world
		EntityFactory.createPlayer().addToWorld();
		createBlocks();
	}

	public void createBlocks() {
		float blockWidth = Constants.BLOCK_WIDTH;
		float blockHeight = Constants.BLOCK_HEIGHT;
		float cols = 7;
		float rows = 5;
		float spacing = 20;
		float totalWidth = cols * (blockWidth + spacing);
		float topMargin = 40;
		float leftMargin = (Gdx.graphics.getWidth() - totalWidth) / 2;
		for (int i = 0; i < cols * rows; i++) {
			float x = leftMargin + (i % cols) * (blockWidth + spacing);
			float y = Gdx.graphics.getHeight() - blockHeight - topMargin - ((int) (i / cols) * (blockHeight + spacing));
			Entity block = EntityFactory.createBlock(x, Gdx.graphics.getHeight() + blockHeight, blockWidth, blockHeight);

			ActionContainer actions = block.getComponent(ActionContainer.class);
			if (Settings.JUICY) {
				SequenceAction sequence = Actions.sequence();
				sequence.addAction(Actions.delay(MathUtils.random(1f)));
				sequence.addAction(Actions.moveTo(x, y, 3, Interpolation.elasticOut));

				ColorAction color = Actions.color(
				      new Color(MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), 1), 3);

				ParallelAction para = Actions.parallel();
				para.addAction(sequence);
				para.addAction(color);

				actions.add(para);
			} else {
				actions.add(Actions.moveTo(x, y));
			}
			block.addToWorld();
		}
	}

	@Override
	protected void initialize() {
	}

}
