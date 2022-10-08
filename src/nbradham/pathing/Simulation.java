package nbradham.pathing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Scanner;

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

	private final SimThread thread = new SimThread();

	private final ArrayList<Human> humans = new ArrayList<>();
	private final Bot bot = new Bot();

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
		bot.setStart(scan.nextShort(), scan.nextShort());
		bot.setTarget(scan.nextShort(), scan.nextShort());
		scan.nextLine();
		while (scan.hasNextLine()) {
			String[] split = scan.nextLine().split(" ");
			Human th = new Human();
			byte i = 0;
			while (i < split.length)
				th.addKeyPos(Short.parseShort(split[i++]), Short.parseShort(split[i++]), Short.parseShort(split[i++]));
		}
		scan.close();
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
		SwingUtilities.invokeLater(() -> repaint());
	}

	/**
	 * Resets the simulation to the initial state.
	 */
	final void reset() {
		// TODO: Add function.
	}

	/**
	 * Sets the algorithm for the bot.
	 * 
	 * @param useAStar Set to true to use A*, false for Dijkstra's.
	 */
	final void setUsingAStar(boolean useAStar) {
		bot.setAlgorithm(useAStar ? new AStarPather() : new DijkstraPather());
	}

	/**
	 * Sets the wait time of the {@link SimulationThread}.
	 * 
	 * @param wait The new wait time.
	 */
	final void setWait(short wait) {
		thread.setWait(wait);
	}

	@Override
	public final void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		for (short x = 0; x < getWidth(); x += 50)
			g.drawLine(x, 0, x, getHeight());
		for (short y = 0; y < getHeight(); y += 50)
			g.drawLine(0, y, getWidth(), y);
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
			System.out.printf("Wait set: %d%n", wait);
		}

		@Override
		public final void run() {
			for (byte i = 0; i < 50; i++) {
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