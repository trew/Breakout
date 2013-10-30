package se.samuelandersson.breakout.screens;

import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import se.samuelandersson.breakout.EntityFactory;
import se.samuelandersson.breakout.GameManager;
import se.samuelandersson.breakout.systems.ActionsSystem;
import se.samuelandersson.breakout.systems.CollisionSystem;
import se.samuelandersson.breakout.systems.MovementSystem;
import se.samuelandersson.breakout.systems.PlayerInputSystem;
import se.samuelandersson.breakout.systems.RenderSystem;
import se.samuelandersson.gdxcommon.Screen;

public class GameScreen implements Screen {

	private World world;
	
	private Array<EntitySystem> updateSystems;
	private Array<EntitySystem> renderSystems;

	public GameScreen() {
		updateSystems = new Array<EntitySystem>();
		renderSystems = new Array<EntitySystem>();
		
		reset();
	}
	
	public void reset() {
		updateSystems.clear();
		renderSystems.clear();
		
		world = new World();
		
		EntityFactory.setWorld(world);
		
		world.setManager(new GroupManager());
		world.setManager(new TagManager());
		world.setManager(new GameManager());

		PlayerInputSystem input = new PlayerInputSystem();
		Gdx.input.setInputProcessor(input);
		
		updateSystems.add(world.setSystem(new ActionsSystem(), false));
		updateSystems.add(world.setSystem(new MovementSystem(), false));
		updateSystems.add(world.setSystem(input, false));
		updateSystems.add(world.setSystem(new CollisionSystem(), false));
		renderSystems.add(world.setSystem(new RenderSystem(), false));
		
		world.initialize();
	}
	

	@Override
	public void update(float delta) {
		world.setDelta(delta);
		world.process();
		for (EntitySystem system : updateSystems)
			system.process();
	}

	@Override
	public void render() {
		for (EntitySystem system : renderSystems)
			system.process();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
