package se.samuelandersson.breakout;

import se.samuelandersson.breakout.screens.GameScreen;
import se.samuelandersson.gdxcommon.Game;

public class Breakout extends Game {

	@Override
	public void create() {
		Assets.load();
		setScreen(new GameScreen());
	}

}
