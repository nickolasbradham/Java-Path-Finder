package nbradham.pathing.algorithms;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;

import nbradham.pathing.Simulation;
import nbradham.pathing.objects.Bot;

/**
 * Handles Dijkstra's path finding.
 * 
 * @author Nickolas Bradham
 *
 */
public final class DijkstraPather extends PathingAlgorithm {

	private final Cell[][] grid = new Cell[Simulation.GRID_W][Simulation.GRID_H];
	private final PriorityQueue<Cell> q = new PriorityQueue<>();
	private Cell[] path;

	/**
	 * Constructs a new DijkstraPather instance.
	 * 
	 * @param setSim The Simulation to pull info from.
	 */
	public DijkstraPather(Simulation setSim) {
		super(setSim);
		for (byte x = 0; x < grid.length; x++)
			for (byte y = 0; y < grid[x].length; y++)
				grid[x][y] = new Cell(x, y);
	}

	@Override
	public void step() {
		if (hasPath())
			return;

		Cell u = q.poll();

		if (u.loc.equals(end)) {
			ArrayList<Cell> tpath = new ArrayList<>();
			while (u != null) {
				tpath.add(u);
				u = u.prev;
			}
			path = tpath.toArray(new Cell[0]);
			q.clear();
			return;
		}

		for (byte x = -1; x <= 1; x++)
			for (byte y = -1; y <= 1; y++)
				if (!(x == 0 && y == 0)) {
					int nx = u.loc.x + x, ny = u.loc.y + y;
					if (nx >= 0 && ny >= 0 && nx < grid.length && ny < grid[nx].length) {
						double alt = u.dist + u.loc.distance(grid[nx][ny].loc) * Bot.MOV_T;
						if (alt < grid[nx][ny].dist && sim.notPointInPersonalSpace((int) alt, nx * Simulation.CELL_S,
								ny * Simulation.CELL_S)) {
							grid[nx][ny].dist = alt;
							grid[nx][ny].prev = u;
							q.offer(grid[nx][ny]);
						}
					}
				}
	}

	@Override
	public boolean isFinished() {
		return q.isEmpty();
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
			kfs.add(new int[] { --k * Bot.MOV_T, c.loc.x * Simulation.CELL_S, c.loc.y * Simulation.CELL_S });
		return kfs.toArray(new int[0][]);
	}

	@Override
	public void setPoints(Point start, Point end) {
		Point sc = toCellCoords(start);
		super.setPoints(start, end);
		grid[sc.x][sc.y].dist = 0;
		q.offer(grid[sc.x][sc.y]);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		for (byte x = 0; x < grid.length; x++)
			for (byte y = 0; y < grid[x].length; y++) {
				if (grid[x][y].prev != null)
					g.drawLine(x * Simulation.CELL_S + Simulation.CELL_S / 2,
							y * Simulation.CELL_S + Simulation.CELL_S / 2,
							grid[x][y].prev.loc.x * Simulation.CELL_S + Simulation.CELL_S / 2,
							grid[x][y].prev.loc.y * Simulation.CELL_S + Simulation.CELL_S / 2);
				g.drawString(String.format("%.1f", grid[x][y].dist), x * Simulation.CELL_S, y * Simulation.CELL_S + 12);
			}
	}

	/**
	 * Holds cell information.
	 * 
	 * @author Nickolas Bradham
	 *
	 */
	private static final class Cell implements Comparable<Cell> {

		private final Point loc;
		private Cell prev;
		private double dist = Double.POSITIVE_INFINITY;

		/**
		 * Constructs a new Cell instance.
		 * 
		 * @param x the X coordinate of the Cell.
		 * @param y the Y coordinate of the Cell.
		 */
		private Cell(byte x, byte y) {
			loc = new Point(x, y);
		}

		@Override
		public int compareTo(Cell o) {
			return (int) Math.floor(dist - o.dist);
		}
	}
}