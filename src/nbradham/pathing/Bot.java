package nbradham.pathing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import nbradham.pathing.algorithms.PathingAlgorithm;

/**
 * Handles all robot interactions.
 * 
 * @author Nickolas Bradham
 *
 */
final class Bot extends KeyframedObject {

	private static final short HITBOX_RADIUS = 25;
	private static final int HITBOX_DIAMETER = HITBOX_RADIUS * 2;

	private final Point start, end;
	private PathingAlgorithm alg;
	private Point loc;

	/**
	 * Constructs a new Bot.
	 * 
	 * @param startX The starting X of the bot.
	 * @param startY The starting Y of the bot.
	 * @param endX   The target X of the bot.
	 * @param endY   The target Y of the bot.
	 */
	Bot(int startX, int startY, int endX, int endY) {
		super(new int[][] { { 0, startX, startY } });
		start = new Point(startX, startY);
		loc = new Point(start);
		end = new Point(endX, endY);
	}

	/**
	 * Sets the algorithm that the robot uses.
	 * 
	 * @param algorithm The desired algorithm.
	 */
	void setAlgorithm(PathingAlgorithm algorithm) {
		alg = algorithm;
	}

	/**
	 * Retrieves if the bot has a path ready to follow.
	 * 
	 * @return True if the bot has found a path.
	 */
	boolean hasPath() {
		// TODO: Figure this out.
		return true;
	}

	/**
	 * Steps the pathing algorithm.
	 */
	void stepPathing() {
		// TODO Figure this out.
	}

	/**
	 * Resets the bot to the initial position and configuration.
	 */
	void reset() {
		loc.setLocation(start);
	}

	/**
	 * Draws the bot to {@code g}.
	 * 
	 * @param g The Graphics to draw the bot to.
	 */
	void paint(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillOval(loc.x - HITBOX_RADIUS, loc.y - HITBOX_RADIUS, HITBOX_DIAMETER, HITBOX_DIAMETER);

		g.setColor(Color.MAGENTA);
		g.fillRect(end.x - 10, end.y - 10, 20, 20);
	}
}