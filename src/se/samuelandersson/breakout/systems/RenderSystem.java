package se.samuelandersson.breakout.systems;

import se.samuelandersson.breakout.Assets;
import se.samuelandersson.breakout.components.Position;
import se.samuelandersson.breakout.components.Sprite;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderSystem extends EntityProcessingSystem {

	@Mapper ComponentMapper<Sprite> sm;
	@Mapper ComponentMapper<Position> pm;

	OrthographicCamera camera;
	SpriteBatch batch;

	public RenderSystem() {
		super(Aspect.getAspectForAll(Sprite.class, Position.class));
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		batch = new SpriteBatch();
	}

	@Override
	protected void begin() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override
	protected void process(Entity e) {
		Position p = pm.get(e);
		Sprite sprite = sm.get(e);
		
		TextureRegion region = Assets.getPixelRegion();
		
		batch.setColor(sprite.r, sprite.g, sprite.b, sprite.a);
		batch.draw(region, p.x, p.y, 0, 0, sprite.w, sprite.h, sprite.scaleX, sprite.scaleY, sprite.angle);
	}

	@Override
	protected void end() {
		batch.end();
	}

}
