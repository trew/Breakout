package se.samuelandersson.breakout;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;

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
		float blockWidth = 70;
		float blockHeight = 40;
		float cols = 7;
		float rows = 5;
		float spacing = 20;
		float totalWidth = cols * (blockWidth + spacing);
		float topMargin = 40;
		float leftMargin = (Gdx.graphics.getWidth() - totalWidth) / 2;
		for (int i = 0; i < cols * rows; i++) {
			float x = leftMargin + (i % cols) * (blockWidth + spacing);
			float y = Gdx.graphics.getHeight() - blockHeight - topMargin - ((int)(i / cols) * (blockHeight + spacing));
			Entity block = EntityFactory.createBlock(x, y, blockWidth, blockHeight);
			block.addToWorld();
		}
	}

	@Override
	protected void initialize() {
	}

}
