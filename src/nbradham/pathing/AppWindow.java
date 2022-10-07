package nbradham.pathing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Handles GUI elements.
 * 
 * @author Nickolas Bradham
 *
 */
final class AppWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String ACT_OPEN = "open", ACT_RUN = "run", ACT_PAUSE = "pause", ACT_STEP = "step",
			ACT_RESET = "reset";

	private final Simulation sim = new Simulation();
	private final JMenuItem runItem = createJMenuItem("Run", ACT_RUN, KeyEvent.VK_R),
			pauseItem = createJMenuItem("Pause", ACT_PAUSE, KeyEvent.VK_P),
			stepItem = createJMenuItem("Step", ACT_STEP, KeyEvent.VK_S),
			resetItem = createJMenuItem("Reset", ACT_RESET, KeyEvent.VK_E);

	/**
	 * Constructs a new AppWindow instance.
	 */
	private AppWindow() {
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

		sim.setPreferredSize(new Dimension(1300, 700));
		setContentPane(sim);
		pack();
	}

	/**
	 * Creates a new JMenuItem instance and sets it up.
	 * 
	 * @param txt The text displayed by the item.
	 * @param act The action command of the item.
	 * @param key The {@link KeyEvent} VK code of the accelerator key.
	 * @return The new JMenuItem instance.
	 */
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
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Open Sim File");
			jfc.setFileFilter(new FileNameExtensionFilter("Simulation Data File", "sim"));
			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					sim.load(jfc.getSelectedFile());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				runItem.setEnabled(true);
				stepItem.setEnabled(true);
			}
			break;
		case ACT_RUN:
			runItem.setEnabled(false);
			pauseItem.setEnabled(true);
			stepItem.setEnabled(false);
			resetItem.setEnabled(false);
			sim.run();
			break;
		case ACT_PAUSE:
			sim.pause();
			pauseItem.setEnabled(false);
			runItem.setEnabled(true);
			stepItem.setEnabled(true);
			resetItem.setEnabled(true);
			break;
		case ACT_STEP:
			sim.step();
			break;
		case ACT_RESET:
			sim.reset();
			sim.repaint();
		}
	}

	/**
	 * Constructs a new AppWindow on the AWT thread and sets it to visible.
	 * 
	 * @param args Ignored.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new AppWindow().setVisible(true));
	}
}