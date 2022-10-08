package nbradham.pathing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Represents a human obstacle.
 * 
 * @author Nickolas Bradham
 *
 */
final class Human {

	private static final short HITBOX_RADIUS = Simulation.toPixels(.5), PERSONAL_RADIUS = Simulation.toPixels(1.5);
	private static final int HITBOX_DIAMETER = HITBOX_RADIUS * 2, PERSONAL_DIAMETER = PERSONAL_RADIUS * 2;

	private final short[][] keyPoss;
	private Point loc;

	public Human(short[][] setKeyPoss) {
		keyPoss = setKeyPoss;
		loc = new Point(keyPoss[0][1], keyPoss[0][2]);
	}

	void step() {
		// TODO Figure this out.
	}

	public void paint(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, 32));
		g.fillOval(loc.x - PERSONAL_RADIUS, loc.y - PERSONAL_RADIUS, PERSONAL_DIAMETER, PERSONAL_DIAMETER);

		g.setColor(Color.BLACK);
		g.fillOval(loc.x - HITBOX_RADIUS, loc.y - HITBOX_RADIUS, HITBOX_DIAMETER, HITBOX_DIAMETER);

		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(4));
		for (byte i = 1; i < keyPoss.length; i++) {
			int last = i - 1;
			g.drawLine(keyPoss[last][1], keyPoss[last][2], keyPoss[i][1], keyPoss[i][2]);
		}
	}
}