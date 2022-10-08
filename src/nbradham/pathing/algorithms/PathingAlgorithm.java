package nbradham.pathing.algorithms;

/**
 * Interface for path finding algorithms.
 * 
 * @author Nickolas Bradham
 *
 */
public sealed interface PathingAlgorithm permits AStarPather, DijkstraPather {

	void step();

	boolean isFinished();

	boolean hasPath();

	int[][] generateKeyframes();
}