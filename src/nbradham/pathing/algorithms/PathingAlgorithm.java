package nbradham.pathing.algorithms;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import nbradham.pathing.Simulation;

/**
 * Interface for path finding algorithms.
 * 
 * @author Nickolas Bradham
 *
 */
public abstract class PathingAlgorithm {

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
	 * Retrieves the length of the calculated path.
	 * 
	 * @return The path length.
	 */
	public abstract int getDist();

	/**
	 * Sets the start and end point of the algorithm.
	 * 
	 * @param start The start Point.
	 * @param end   The end Point.
	 */
	public void setPoints(Point start, Point setEnd) {
		end = toCellCoords(setEnd);
	}

	/**
	 * Paints algorithm data to {@code g}.
	 * 
	 * @param g The graphics to paint to.
	 */
	public void paint(Graphics2D g) {
		g.setStroke(new BasicStroke(4));
	};

	/**
	 * Converts Point {@code p} to cell coordinates.
	 * 
	 * @param p The Point to convert.
	 * @return A new Point instance with the coordinates converted to cell
	 *         coordinates.
	 */
	protected static final Point toCellCoords(Point p) {
		return new Point(p.x / Simulation.CELL_S, p.y / Simulation.CELL_S);
	}
}