package se.samuelandersson.breakout.components;

import se.samuelandersson.breakout.actions.Actions;
import se.samuelandersson.breakout.actions.BaseAction;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Allows the owner of this component to have actions applied to them. see {@link Actions}
 * 
 * @author Samuel Andersson
 */
public class ActionComponent extends Component {
	public Array<BaseAction> actions = new Array<BaseAction>();
}
