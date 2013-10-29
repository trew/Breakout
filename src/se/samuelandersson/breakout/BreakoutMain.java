package se.samuelandersson.breakout;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public final class BreakoutMain {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 800;
		cfg.height = 480;
		cfg.title = "Breakout";
		new LwjglApplication(new Breakout(), cfg);
	}

	private BreakoutMain() {
	}

}
