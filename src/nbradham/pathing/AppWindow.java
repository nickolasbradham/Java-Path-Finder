package nbradham.pathing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
			ACT_RESET = "reset", ACT_ALGORITHM = "algorithm", ACT_WAIT = "wait", ALGORITHM_A_STAR = "A*",
			ALGORITHM_DIJKSTRAS = "Dijkstra's";

	private final Simulation sim = new Simulation();
	private final JButton runItem = createJButton("Run", ACT_RUN, KeyEvent.VK_R),
			pauseItem = createJButton("Pause", ACT_PAUSE, KeyEvent.VK_P),
			stepItem = createJButton("Step", ACT_STEP, KeyEvent.VK_S),
			resetItem = createJButton("Reset", ACT_RESET, KeyEvent.VK_E);
	private final JComboBox<String> algorithmBox = new JComboBox<>(
			new String[] { ALGORITHM_A_STAR, ALGORITHM_DIJKSTRAS }),
			waitBox = new JComboBox<>(new String[] { "500", "250", "100", "50", "17" });

	/**
	 * Constructs a new AppWindow instance.
	 */
	private AppWindow() {
		super("Pathing App");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel controls = new JPanel();
		JButton openItem = createJButton("Open", ACT_OPEN, KeyEvent.VK_O);
		openItem.setEnabled(true);
		controls.add(openItem);
		controls.add(runItem);
		controls.add(pauseItem);
		controls.add(stepItem);
		controls.add(resetItem);

		controls.add(new JLabel("Algorithm:"));
		algorithmBox.setActionCommand(ACT_ALGORITHM);
		algorithmBox.addActionListener(this);
		updateAlgorithm();
		controls.add(algorithmBox);

		controls.add(new JLabel("Step Wait (ms):"));
		waitBox.setActionCommand(ACT_WAIT);
		waitBox.addActionListener(this);
		waitBox.setEditable(true);
		updateWait();
		controls.add(waitBox);
		
		controls.add(sim.getStepLabel());

		add(controls, BorderLayout.NORTH);

		JPanel wrap = new JPanel();
		wrap.add(sim);
		add(wrap, BorderLayout.CENTER);
		pack();
	}

	/**
	 * Creates a new JMenuItem instance and sets it up.
	 * 
	 * @param txt The text displayed by the item.
	 * @param act The action command of the item.
	 * @param key The {@link KeyEvent} VK code of the mnemonic key.
	 * @return The new JMenuItem instance.
	 */
	private JButton createJButton(String txt, String act, int key) {
		JButton jb = new JButton(txt);
		jb.setActionCommand(act);
		jb.addActionListener(this);
		jb.setMnemonic(key);
		jb.setEnabled(false);
		return jb;
	}

	/**
	 * Updates the simulation algorithm.
	 */
	private void updateAlgorithm() {
		sim.setUsingAStar(algorithmBox.getSelectedItem().equals(ALGORITHM_A_STAR));
	}

	/**
	 * Updates the simulation speed.
	 */
	private void updateWait() {
		try {
			sim.setWait(Short.parseShort((String) waitBox.getSelectedItem()));
			waitBox.getEditor().getEditorComponent().setForeground(getForeground());
		} catch (NumberFormatException e) {
			waitBox.getEditor().getEditorComponent().setForeground(Color.RED);
			e.printStackTrace();
		}
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
			resetItem.setEnabled(true);
			sim.step();
			break;
		case ACT_RESET:
			sim.reset();
			sim.repaint();
			break;
		case ACT_ALGORITHM:
			updateAlgorithm();
			break;
		case ACT_WAIT:
			updateWait();
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