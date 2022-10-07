package nbradham.pathing;

import java.awt.Graphics;
import java.lang.Thread.State;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

final class VPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final SimThread thread = new SimThread();
	private byte size = 0;

	final void start() {
		thread.start();
	}

	final void pause() {
		thread.pause();
	}

	final void step() {
		size++;
		SwingUtilities.invokeLater(() -> repaint());
	}

	final boolean isStarted() {
		return thread.getState() != State.NEW;
	}
	
	final void reset() {
		size = 0;
	}

	@Override
	public final void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.fillRect(0, 0, size * 10, size * 10);
	}

	private final class SimThread extends Thread {

		private final Object lock = new Object();
		private boolean pause = false;

		private void pause() {
			if (!(pause = !pause))
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