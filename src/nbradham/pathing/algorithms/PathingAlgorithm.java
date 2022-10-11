package nbradham.pathing.algorithms;

import java.awt.Graphics;
import java.awt.Point;

import nbradham.pathing.Simulation;

/**
 * Interface for path finding algorithms.
 * 
 * @author Nickolas Bradham
 *
 */
public sealed abstract class PathingAlgorithm permits AStarPather, DijkstraPather {

	protected final Simulation sim;
	protected Point end;

	/**
	 * Constructs a new PathingAlgorithm instance.
	 * 
	 * @param setSim The Simulation to pull info from.
	 */
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

	/**
	 * Sets the start and end point of the algorithm.
	 * 
	 * @param start The start Point.
	 * @param end   The end Point.
	 */
	public abstract void setPoints(Point start, Point end);

	/**
	 * Paints algorithm data to {@code g}.
	 * 
	 * @param g The graphics to paint to.
	 */
	public abstract void paint(Graphics g);

	/**
	 * Converts Point {@code p} to cell coordinates.
	 * 
	 * @param p The Point to convert.
	 * @return A new Point instance with the coordinates converted to cell
	 *         coordinates.
	 */
	protected static final Point toCellCords(Point p) {
		return new Point(p.x / Simulation.CELL_S, p.y / Simulation.CELL_S);
	}
}