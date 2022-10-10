package nbradham.pathing.objects;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a human in the simulation. This human may have a determined path
 * or be stationary.
 * 
 * @author Nickolas Bradham
 *
 */
public final class Human extends KeyframedObject {

	private static final short PERSONAL_RADIUS = 75;
	private static final int PERSONAL_DIAMETER = PERSONAL_RADIUS * 2;

	/**
	 * Constructs a new Human instance.
	 * 
	 * @param setKeyPoss Sets the key frame data of this Human.
	 */
	public Human(int[][] setKeyPoss) {
		super(setKeyPoss);
	}

	@Override
	public final void paint(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, 32));
		g.fillOval(loc.x - PERSONAL_RADIUS, loc.y - PERSONAL_RADIUS, PERSONAL_DIAMETER, PERSONAL_DIAMETER);
		super.paint(g);
	}
}