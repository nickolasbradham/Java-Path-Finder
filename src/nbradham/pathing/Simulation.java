package nbradham.pathing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nbradham.pathing.algorithms.AStarPather;
import nbradham.pathing.algorithms.DijkstraPather;
import nbradham.pathing.objects.Bot;
import nbradham.pathing.objects.Human;
import nbradham.pathing.objects.KeyframedObject;

/**
 * Handles simulation execution and drawing.
 * 
 * @author Nickolas Bradham
 *
 */
public final class Simulation extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final byte CELL_S = 50, GRID_W = 26, GRID_H = 14;

	private final SimThread thread = new SimThread();
	private final JLabel stepLabel = new JLabel("Waiting...");
	private final AppWindow appWin;

	private Human[] humans = new Human[0];
	private Bot bot = new Bot(-1, -1, -1, -1);
	private String labelText = "Waiting...";
	private short step = 0;
	private boolean usingAStar = false;

	/**
	 * Constructs a new Simulation.
	 * 
	 * @param aw The AppWindow to call
	 *           {@link AppWindow#actionPerformed(ActionEvent)} on when simulation
	 *           ends.
	 */
	Simulation(AppWindow aw) {
		super();
		setPreferredSize(new Dimension(GRID_W * CELL_S, GRID_H * CELL_S));
		appWin = aw;
	}

	/**
	 * Loads a simulation file.
	 * 
	 * @param file The file to load.
	 * @return True if the file was loaded successfully.
	 */
	final boolean load(File file) {
		Scanner scan;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "File was not found.", "Sim Load Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		bot = new Bot(scan.nextShort(), scan.nextShort(), scan.nextShort(), scan.nextShort());
		updateBotPather();

		scan.nextLine();

		ArrayList<KeyframedObject> humansT = new ArrayList<>();
		while (scan.hasNextLine()) {
			String[] split = scan.nextLine().split(" ");
			ArrayList<int[]> keyPoss = new ArrayList<>();
			byte i = 0;
			while (i < split.length)
				keyPoss.add(new int[] { Short.parseShort(split[i++]), Short.parseShort(split[i++]),
						Short.parseShort(split[i++]) });
			int[][] sort = keyPoss.toArray(new int[0][]);
			Arrays.sort(sort, KeyframedObject.KEYFRAME_SORTER);
			if (sort[0][0] != 0) {
				JOptionPane.showMessageDialog(this, "At least one object is missing an initial (0) keyframe.",
						"Sim Data Error", JOptionPane.ERROR_MESSAGE);
				scan.close();
				return false;
			}
			humansT.add(new Human(sort));
		}
		scan.close();

		humans = humansT.toArray(new Human[0]);
		repaint();
		return true;
	}

	/**
	 * Updates the {@link PathingAlgorithm} of the robot.
	 */
	private void updateBotPather() {
		bot.setAlgorithm(usingAStar ? new AStarPather(this) : new DijkstraPather(this));
		repaint();
	}

	/**
	 * Starts (or resumes) simulation thread.
	 */
	final void run() {
		if (thread.getState() == State.NEW)
			thread.start();
		else
			thread.unpause();
	}

	/**
	 * Pauses the simulation thread.
	 */
	final void pause() {
		thread.pause();
	}

	/**
	 * Continues the simulation by one step.
	 */
	final void step() {
		switch (bot.getState()) {
		case MOVING:
			step++;
			labelText = "Step: " + step;
			for (KeyframedObject h : humans)
				h.step(step);
			bot.step(step);
			break;
		case PATHING:
			labelText = "Pathing...";
			bot.stepPathing();
			break;
		case NO_PATH:
			labelText = "No path";
			appWin.pause();
			break;
		case END_REACHED:
			labelText = "End reached";
			appWin.pause();
		}

		SwingUtilities.invokeLater(() -> repaint());
	}

	/**
	 * Resets the simulation to the initial state.
	 */
	final void reset() {
		step = 0;
		for (KeyframedObject h : humans)
			h.step(step);
		bot.reset();
		updateBotPather();
		labelText = "Reset.";
		repaint();
	}

	/**
	 * Sets the algorithm for the bot.
	 * 
	 * @param useAStar Set to true to use A*, false for Dijkstra's.
	 */
	final void setUsingAStar(boolean useAStar) {
		usingAStar = useAStar;
		updateBotPather();
	}

	/**
	 * Sets the wait time of the {@link SimulationThread}.
	 * 
	 * @param wait The new wait time.
	 */
	final void setWait(short wait) {
		thread.setWait(wait);
	}

	/**
	 * Retrieves the JLabel that will show simulation information.
	 * 
	 * @return The JLabel that is controlled by this Simulation.
	 */
	JLabel getStepLabel() {
		return stepLabel;
	}

	/**
	 * Checks if point {@code (nx, ny)} is in the "personal space" of any
	 * {@link Human} instance.
	 * 
	 * @param t  Time step to check.
	 * @param nx X coordinate of point.
	 * @param ny Y coordinate of point.
	 * @return True if the point is not too close to any human.
	 */
	public boolean notPointInPersonalSpace(int t, int nx, int ny) {
		Point p = new Point(nx, ny);
		for (Human h : humans)
			if (h.isPointInPS(t, p))
				return false;
		return true;
	}

	@Override
	public final void paint(Graphics g) {
		stepLabel.setText(labelText);

		g.clearRect(0, 0, getWidth(), getHeight());
		for (short x = 0; x < getWidth(); x += CELL_S)
			g.drawLine(x, 0, x, getHeight());
		for (short y = 0; y < getHeight(); y += CELL_S)
			g.drawLine(0, y, getWidth(), y);

		for (KeyframedObject h : humans)
			h.paint((Graphics2D) g);

		bot.paint(g);
	}

	/**
	 * Handles simulation auto stepping.
	 * 
	 * @author Nickolas Bradham
	 *
	 */
	private final class SimThread extends Thread {

		private final Object lock = new Object();
		private short wait;
		private boolean pause = false;

		/**
		 * Pauses the simulation thread.
		 */
		private void pause() {
			pause = true;
		}

		/**
		 * Resumes the simulation thread.
		 */
		private void unpause() {
			pause = false;
			synchronized (lock) {
				lock.notify();
			}
		}

		/**
		 * Sets the wait time of this thread.
		 * 
		 * @param waitTime The new wait time in milliseconds.
		 */
		private void setWait(short waitTime) {
			wait = waitTime;
		}

		@Override
		public final void run() {
			while (true) {
				step();
				try {
					sleep(wait);
					if (pause)
						synchronized (lock) {
							lock.wait();
						}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}