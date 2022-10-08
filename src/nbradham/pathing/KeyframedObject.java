package nbradham.pathing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;

/**
 * Represents a human obstacle.
 * 
 * @author Nickolas Bradham
 *
 */
class KeyframedObject {

	private static final short HITBOX_RADIUS = 25, PERSONAL_RADIUS = 75;
	private static final int HITBOX_DIAMETER = HITBOX_RADIUS * 2, PERSONAL_DIAMETER = PERSONAL_RADIUS * 2;

	private final int[][] keyPoss;
	private Point loc;

	public KeyframedObject(int[][] setKeyPoss) {
		keyPoss = setKeyPoss;
		loc = new Point(keyPoss[0][1], keyPoss[0][2]);
	}

	void step(short n) {
		int ind = Arrays.binarySearch(keyPoss, new int[] { n }, (int[] a, int[] b) -> {
			return a[0] - b[0];
		});
		if (ind < 0) {
			int[] first = keyPoss[-(ind + 1) - 1];
			int si = -(ind + 1);
			if (si < keyPoss.length) {
				int[] second = keyPoss[si];
				int dt = second[0] - first[0], midT = n - first[0];
				loc.setLocation(first[1] + (second[1] - first[1]) * midT / dt,
						first[2] + (second[2] - first[2]) * midT / dt);
			} else
				loc.setLocation(first[1], first[2]);
		} else
			loc.setLocation(keyPoss[ind][1], keyPoss[ind][2]);
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