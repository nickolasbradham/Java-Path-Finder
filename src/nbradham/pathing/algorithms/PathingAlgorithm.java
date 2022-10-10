package nbradham.pathing.algorithms;

import nbradham.pathing.Simulation;

/**
 * Interface for path finding algorithms.
 * 
 * @author Nickolas Bradham
 *
 */
public sealed abstract class PathingAlgorithm permits AStarPather, DijkstraPather {

	protected final Simulation sim;

	protected PathingAlgorithm(Simulation setSim) {
		sim = setSim;
	}

	/**
	 * Steps the algorithm forward one cycle.
	 */
	public abstract void step();

	/**
	 * Retrieves if the algorithm is finished.
	 * 
	 * @return True if the algorithm has found a path or concluded that no path
	 *         exists.
	 */
	public abstract boolean isFinished();

	/**
	 * Retrieves if the algorithm found a path.
	 * 
	 * @return True if the algorithm has found a valid path.
	 */
	public abstract boolean hasPath();

	/**
	 * Generates the animation key frames.
	 * 
	 * @return The key frames in an {@code int[][]}.
	 */
	public abstract int[][] generateKeyframes();
}