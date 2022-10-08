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

	static enum BotState {
		PATHING, NO_PATH, READY
	};

	private static final short HITBOX_RADIUS = 25;
	private static final int HITBOX_DIAMETER = HITBOX_RADIUS * 2;

	private final Point start, end;
	private PathingAlgorithm alg;
	private BotState state = BotState.PATHING;

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
	BotState getState() {
		return state;
	}

	/**
	 * Steps the path finding algorithm.
	 */
	void stepPathing() {
		if (alg.isFinished())
			if (alg.hasPath()) {
				keyPoss = alg.generateKeyframes();
				state = BotState.READY;
			} else
				state = BotState.NO_PATH;
		else
			alg.step();
	}

	/**
	 * Resets the bot to the initial position and configuration.
	 */
	void reset() {
		keyPoss = new int[][] { { 0, start.x, start.y } };
		state = BotState.PATHING;
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