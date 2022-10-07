package nbradham.pathing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

final class PathingApp extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String ACT_OPEN = "open", ACT_RUN = "run", ACT_PAUSE = "pause", ACT_STEP = "step",
			ACT_RESET = "reset";

	private final VPanel pane = new VPanel();
	private final JMenuItem runItem = createJMenuItem("Run", ACT_RUN, KeyEvent.VK_R),
			pauseItem = createJMenuItem("Pause", ACT_PAUSE, KeyEvent.VK_P),
			stepItem = createJMenuItem("Step", ACT_STEP, KeyEvent.VK_S),
			resetItem = createJMenuItem("Reset", ACT_RESET, KeyEvent.VK_E);

	private PathingApp() {
		super("Pathing App");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JMenuBar bar = new JMenuBar();
		JMenuItem openItem = createJMenuItem("Open", ACT_OPEN, KeyEvent.VK_O);
		openItem.setEnabled(true);
		bar.add(openItem);
		bar.add(runItem);
		bar.add(pauseItem);
		bar.add(stepItem);
		bar.add(resetItem);
		setJMenuBar(bar);

		pane.setPreferredSize(new Dimension(1300, 700));
		setContentPane(pane);
		pack();
	}

	private JMenuItem createJMenuItem(String txt, String act, int key) {
		JMenuItem jmi = new JMenuItem(txt);
		jmi.setActionCommand(act);
		jmi.addActionListener(this);
		jmi.setAccelerator(KeyStroke.getKeyStroke(key, KeyEvent.CTRL_DOWN_MASK));
		jmi.setEnabled(false);
		return jmi;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ACT_OPEN:
			// TODO: Finish implementation.
			runItem.setEnabled(true);
			stepItem.setEnabled(true);
			break;
		case ACT_RUN:
			runItem.setEnabled(false);
			pauseItem.setEnabled(true);
			stepItem.setEnabled(false);
			resetItem.setEnabled(false);
			if (pane.isStarted())
				pane.pause();
			else
				pane.start();
			break;
		case ACT_PAUSE:
			pane.pause();
			pauseItem.setEnabled(false);
			runItem.setEnabled(true);
			stepItem.setEnabled(true);
			resetItem.setEnabled(true);
			break;
		case ACT_STEP:
			pane.step();
			break;
		case ACT_RESET:
			pane.reset();
			pane.repaint();
		}
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> new PathingApp().setVisible(true));
	}
}