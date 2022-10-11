package nbradham.pathing.algorithms;

import java.awt.Graphics;
import java.awt.Point;
import java.util.PriorityQueue;

import nbradham.pathing.Simulation;

/**
 * Handles Dijkstra's path finding.
 * 
 * @author Nickolas Bradham
 *
 */
public final class DijkstraPather extends PathingAlgorithm {

	private final Cell[][] grid = new Cell[Simulation.GRID_W][Simulation.GRID_H];
	private final PriorityQueue<Cell> q = new PriorityQueue<>();

	/**
	 * Constructs a new DijkstraPather instance.
	 * 
	 * @param setSim The Simulation to pull info from.
	 */
	public DijkstraPather(Simulation setSim) {
		super(setSim);
		for (byte x = 0; x < grid.length; x++)
			for (byte y = 0; y < grid[x].length; y++) {
				q.offer(grid[x][y] = new Cell());
			}
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

	private static final class Cell implements Comparable<Cell> {
		private Cell prev;
		private short dist = Short.MAX_VALUE;

		@Override
		public int compareTo(Cell o) {
			return (int) Math.floor(dist - o.dist);
		}
	}
}