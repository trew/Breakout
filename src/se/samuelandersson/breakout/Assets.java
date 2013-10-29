package se.samuelandersson.breakout;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static final AssetManager manager = new AssetManager();

	private static TextureRegion pixelRegion;

	public static void load() {
		manager.load("com/badlogic/gdx/utils/arial-15.fnt", BitmapFont.class);
		manager.finishLoading();

		// create a 1x1 texture region containing a white pixel
		Pixmap p = new Pixmap(1, 1, Format.RGBA8888);
		p.drawPixel(0, 0, 0xFFFFFFFF);
		pixelRegion = new TextureRegion(new Texture(p));
	}

	public static TextureRegion getPixelRegion() {
		return pixelRegion;
	}

	public static BitmapFont getFont() {
		return manager.get("com/badlogic/gdx/utils/arial-15.fnt", BitmapFont.class);
	}

}
