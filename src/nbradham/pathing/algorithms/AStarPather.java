package nbradham.pathing.algorithms;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;

import nbradham.pathing.Simulation;

/**
 * Handles A* path finding.
 * 
 * @author Nickolas Bradham
 *
 */
public final class AStarPather extends PathingAlgorithm {

	private final Cell[][] grid = new Cell[Simulation.GRID_W][Simulation.GRID_H];
	private final PriorityQueue<Cell> openSet = new PriorityQueue<>();
	private Point end;
	private Cell[] path;

	/**
	 * Constructs a new AStarPather instance.
	 * 
	 * @param setSim The Simulation to pull info from.
	 */
	public AStarPather(Simulation setSim) {
		super(setSim);

		for (byte x = 0; x < grid.length; x++)
			for (byte y = 0; y < grid[x].length; y++)
				grid[x][y] = new Cell(x, y);
	}

	@Override
	public void step() {
		if (hasPath())
			return;

		Cell cur = openSet.poll();
		if (cur.loc.equals(end)) {
			ArrayList<Cell> pt = new ArrayList<>();
			while (cur != null) {
				pt.add(cur);
				cur = cur.cameFrom;
			}
			path = pt.toArray(new Cell[0]);
			openSet.clear();
			return;
		}

		for (byte x = -1; x <= 1; x++)
			for (byte y = -1; y <= 1; y++)
				if (!(x == 0 && y == 0)) {
					int nx = cur.loc.x + x, ny = cur.loc.y + y;
					if (nx >= 0 && ny >= 0 && nx < grid.length && ny < grid[nx].length
							&& sim.isPointInPersonalSpace(nx * Simulation.CELL_S, ny * Simulation.CELL_S)) {
						double tgScore = cur.gScore + cur.loc.distance(grid[nx][ny].loc);
						if (tgScore < grid[nx][ny].gScore) {
							grid[nx][ny].cameFrom = cur;
							grid[nx][ny].gScore = tgScore;
							grid[nx][ny].fScore = tgScore + h(grid[nx][ny].loc);
							if (!openSet.contains(grid[nx][ny]))
								openSet.offer(grid[nx][ny]);
						}
					}
				}
	}

	@Override
	public boolean isFinished() {
		return openSet.isEmpty();
	}

	@Override
	public boolean hasPath() {
		return path != null;
	}

	@Override
	public int[][] generateKeyframes() {
		ArrayList<int[]> kfs = new ArrayList<>();
		int k = path.length;
		for (Cell c : path)
			kfs.add(new int[] { --k * 2, c.loc.x * Simulation.CELL_S, c.loc.y * Simulation.CELL_S });
		return kfs.toArray(new int[0][]);
	}

	@Override
	public void setPoints(Point start, Point endPoint) {
		Point sc = toCellCords(start);
		end = toCellCords(endPoint);
		openSet.offer(grid[sc.x][sc.y]);
		grid[sc.x][sc.y].gScore = 0;
		grid[sc.x][sc.y].fScore = h(sc);
	}

	@Override
	public void paint(Graphics g) {
		for (byte x = 0; x < grid.length; x++)
			for (byte y = 0; y < grid[x].length; y++) {
				g.drawString(String.format("%.1f", grid[x][y].fScore), x * Simulation.CELL_S,
						y * Simulation.CELL_S + 12);
				g.drawString(String.format("%.1f", grid[x][y].gScore), x * Simulation.CELL_S,
						y * Simulation.CELL_S + 24);
			}
	}

	private final double h(Point start) {
		return start.distance(end);
	}

	private static final class Cell implements Comparable<Cell> {

		private final Point loc;
		private Cell cameFrom;
		private double fScore = Double.POSITIVE_INFINITY, gScore = Double.POSITIVE_INFINITY;

		private Cell(byte x, byte y) {
			loc = new Point(x, y);
		}

		@Override
		public int compareTo(Cell o) {
			return (int) Math.floor(fScore - o.fScore);
		}

		@Override
		public final String toString() {
			return String.format("Cell:{loc:(%d,%d),par:%s,fScore:%f,gScore:%f}", loc.x, loc.y, cameFrom, fScore,
					gScore);
		}
	}
}