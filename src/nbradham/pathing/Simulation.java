package nbradham.pathing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nbradham.pathing.algorithms.AStarPather;
import nbradham.pathing.algorithms.DijkstraPather;

/**
 * Handles simulation execution and drawing.
 * 
 * @author Nickolas Bradham
 *
 */
final class Simulation extends JPanel {
	private static final long serialVersionUID = 1L;

	static final byte CELL_S = 50;

	private final SimThread thread = new SimThread();
	private final JLabel stepLabel = new JLabel("Waiting...");

	private KeyframedObject[] humans = new KeyframedObject[0];
	private Bot bot = new Bot(-1, -1, -1, -1);
	private String labelText = "";
	private short step = 0;
	private boolean usingAStar = false;

	/**
	 * Constructs a new Simulation.
	 */
	Simulation() {
		super();
		setPreferredSize(new Dimension(1300, 700));
	}

	/**
	 * Loads a simulation file.
	 * 
	 * @param file The file to load.
	 * @throws FileNotFoundException Thrown by {@link Scanner#Scanner(File)}.
	 */
	final void load(File file) throws FileNotFoundException {
		Scanner scan = new Scanner(file);
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
			humansT.add(new KeyframedObject(keyPoss.toArray(new int[0][])));
		}

		scan.close();
		humans = humansT.toArray(new KeyframedObject[0]);
		repaint();
	}

	/**
	 * Updates the {@link PathingAlgorithm} of the robot.
	 */
	private void updateBotPather() {
		bot.setAlgorithm(usingAStar ? new AStarPather() : new DijkstraPather());
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
		case READY:
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
			labelText = "No path.";
			pause();
			break;
		case END_REACHED:
			labelText = "End reached";
			pause();
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