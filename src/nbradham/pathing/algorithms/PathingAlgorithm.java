package nbradham.pathing.algorithms;

/**
 * Interface for path finding algorithms.
 * 
 * @author Nickolas Bradham
 *
 */
public sealed interface PathingAlgorithm permits AStarPather, DijkstraPather {

	/**
	 * Steps the algorithm forward one cycle.
	 */
	void step();

	/**
	 * Retrieves if the algorithm is finished.
	 * 
	 * @return True if the algorithm has found a path or concluded that no path
	 *         exists.
	 */
	boolean isFinished();

	/**
	 * Retrieves if the algorithm found a path.
	 * 
	 * @return True if the algorithm has found a valid path.
	 */
	boolean hasPath();

	/**
	 * Generates the animation key frames.
	 * 
	 * @return The key frames in an {@code int[][]}.
	 */
	int[][] generateKeyframes();
}