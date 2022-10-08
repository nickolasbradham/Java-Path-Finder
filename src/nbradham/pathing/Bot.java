package nbradham.pathing;

import nbradham.pathing.algorithms.PathingAlgorithm;

/**
 * Handles all robot interactions.
 * 
 * @author Nickolas Bradham
 *
 */
final class Bot {

	private PathingAlgorithm alg;
	private short startX, startY, targetX, targetY;

	/**
	 * Sets the start location of the robot.
	 * 
	 * @param x Start x.
	 * @param y Start y.
	 */
	void setStart(short x, short y) {
		startX = x;
		startY = y;
	}

	/**
	 * Sets the target of the robot.
	 * 
	 * @param x Target x.
	 * @param y Target y.
	 */
	void setTarget(short x, short y) {
		targetX = x;
		targetY = y;
	}

	/**
	 * Sets the algorithm that the robot uses.
	 * 
	 * @param algorithm The desired algorithm.
	 */
	void setAlgorithm(PathingAlgorithm algorithm) {
		alg = algorithm;
	}
}