package nbradham.pathing;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

	final void load(File file) throws FileNotFoundException {
		Scanner scan = new Scanner(file);
		bot.setStart(scan.nextShort(), scan.nextShort());
		bot.setTarget(scan.nextShort(), scan.nextShort());
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

		@Override
		public final void run() {
			for (byte i = 0; i < 50; i++) {
				step();
				try {
					sleep(100);
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