package se.samuelandersson.breakout.systems;

import se.samuelandersson.breakout.Assets;
import se.samuelandersson.gdxcommon.components.SpriteComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderSystem extends EntityProcessingSystem {

	@Mapper ComponentMapper<SpriteComponent> sm;

	OrthographicCamera camera;
	SpriteBatch batch;

	public RenderSystem() {
		super(Aspect.getAspectForAll(SpriteComponent.class));
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
		SpriteComponent sprite = sm.get(e);
		
		TextureRegion region = Assets.getPixelRegion();
		
		batch.setColor(sprite.r, sprite.g, sprite.b, sprite.a);
		batch.draw(region, sprite.x, sprite.y, sprite.w / 2, sprite.h / 2, sprite.w, sprite.h, sprite.scaleX, sprite.scaleY, sprite.angle);
	}

	@Override
	protected void end() {
		batch.end();
	}

}
