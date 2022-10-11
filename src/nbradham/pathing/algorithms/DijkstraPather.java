package nbradham.pathing.algorithms;

import java.awt.Graphics;
import java.awt.Point;

import nbradham.pathing.Simulation;

/**
 * Handles Dijkstra's path finding.
 * 
 * @author Nickolas Bradham
 *
 */
public final class DijkstraPather extends PathingAlgorithm {

	/**
	 * Constructs a new DijkstraPather instance.
	 * 
	 * @param setSim The Simulation to pull info from.
	 */
	public DijkstraPather(Simulation setSim) {
		super(setSim);
	}

	@Override
	public void step() {
		// TODO Implement Dijkstra's.
	}

	@Override
	public boolean isFinished() {
		// TODO Implement Dijkstra's.
		return false;
	}

	@Override
	public boolean hasPath() {
		// TODO Implement Dijkstra's.
		return false;
	}

	@Override
	public int[][] generateKeyframes() {
		// TODO Implement Dijkstra's.
		return null;
	}

	@Override
	public void setPoints(Point start, Point end) {
		// TODO Auto-generated method stub
	}

	@Override
	public void paint(Graphics g) {

	}
}