package nbradham.pathing.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Represents a object with key framed movements.
 * 
 * @author Nickolas Bradham
 *
 */
public abstract class KeyframedObject {

	public static final Comparator<int[]> KEYFRAME_SORTER = (int[] a, int[] b) -> {
		return a[0] - b[0];
	};

	private static final short HITBOX_RADIUS = 25;
	private static final int HITBOX_DIAMETER = HITBOX_RADIUS * 2;

	protected int[][] keyPoss;
	protected Point loc;

	/**
	 * Constructs a new KeyframedObject and sets the frames.
	 * 
	 * @param setKeyPoss A array of 3 element int arrays. The order of the elements
	 *                   is {@code frameNum, x, y}.
	 */
	public KeyframedObject(int[][] setKeyPoss) {
		keyPoss = setKeyPoss;
		loc = new Point(keyPoss[0][1], keyPoss[0][2]);
	}

	/**
	 * Updates the position of this object to frame {@code n} as determined by the
	 * key frames stored in this object. It will calculate it's position between two
	 * frames if key frame {@code n} does not exist.
	 * 
	 * @param n The target frame.
	 */
	public void step(short n) {
		int ind = Arrays.binarySearch(keyPoss, new int[] { n }, KEYFRAME_SORTER);
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

	/**
	 * Paints the object to Graphics {@code g}.
	 * 
	 * @param g The graphics to paint to.
	 */
	public void paint(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(4));
		for (byte i = 1; i < keyPoss.length; i++) {
			int last = i - 1;
			g.drawLine(keyPoss[last][1], keyPoss[last][2], keyPoss[i][1], keyPoss[i][2]);
		}

		g.setColor(Color.BLACK);
		g.fillOval(loc.x - HITBOX_RADIUS, loc.y - HITBOX_RADIUS, HITBOX_DIAMETER, HITBOX_DIAMETER);
	}
}