package se.samuelandersson.breakout.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class Sprite extends Component {

	public Sprite(float w, float h) {
		this(w, h, Color.WHITE);
	}

	public Sprite(float w, float h, Color c) {
		this.w = w;
		this.h = h;
		r = c.r;
		g = c.g;
		b = c.b;
		a = c.a;
	}

	public float w;
	public float h;
	public float scaleX = 1;
	public float scaleY = 1;
	public float angle;
	public float r = 1;
	public float g = 1;
	public float b = 1;
	public float a = 1;

}
