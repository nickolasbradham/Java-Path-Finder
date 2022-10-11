package nbradham.pathing.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;

import nbradham.pathing.algorithms.PathingAlgorithm;

/**
 * Handles all robot interactions.
 * 
 * @author Nickolas Bradham
 *
 */
public final class Bot extends KeyframedObject {

	public static final byte MOV_T = 4;

	/**
	 * Represents the current state of a {@link Bot} instance.
	 * 
	 * @author Nickolas Bradham
	 *
	 */
	public static enum BotState {
		PATHING, NO_PATH, MOVING, END_REACHED
	};

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
	public Bot(int startX, int startY, int endX, int endY) {
		super(new int[][] { { 0, startX, startY } }, Color.GRAY);
		start = new Point(startX, startY);
		end = new Point(endX, endY);
	}

	/**
	 * Sets the algorithm that the robot uses.
	 * 
	 * @param algorithm The desired algorithm.
	 */
	public void setAlgorithm(PathingAlgorithm algorithm) {
		alg = algorithm;
		alg.setPoints(start, end);
	}

	/**
	 * Retrieves if the bot has a path ready to follow.
	 * 
	 * @return True if the bot has found a path.
	 */
	public BotState getState() {
		return state;
	}

	/**
	 * Steps the path finding algorithm.
	 */
	public void stepPathing() {
		if (alg.isFinished())
			if (alg.hasPath()) {
				keyPoss = alg.generateKeyframes();
				Arrays.sort(keyPoss, KEYFRAME_SORTER);
				state = BotState.MOVING;
			} else
				state = BotState.NO_PATH;
		else
			alg.step();
	}

	/**
	 * Resets the bot to the initial position and configuration.
	 */
	public void reset() {
		keyPoss = new int[][] { { 0, start.x, start.y } };
		loc.setLocation(start);
		state = BotState.PATHING;
	}

	/**
	 * Draws the bot to {@code g}.
	 * 
	 * @param g The Graphics to draw the bot to.
	 */
	public void paint(Graphics g) {
		alg.paint(g);
		g.setColor(Color.MAGENTA);
		g.fillRect(end.x - 10, end.y - 10, 20, 20);
		super.paint((Graphics2D) g);
	}

	@Override
	public void step(int frame) {
		if (frame <= keyPoss[keyPoss.length - 1][0])
			super.step(frame);
		else
			state = BotState.END_REACHED;
	}
}