package uk.org.wookey.atari.sim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import uk.org.wookey.atari.utils.Logger;

import com.loomcom.symon.exceptions.SymonException;
import com.loomcom.symon.machines.Machine;
import com.loomcom.symon.ui.Console;
import com.loomcom.symon.ui.MemoryWindow;
import com.loomcom.symon.ui.PreferencesDialog;
import com.loomcom.symon.ui.StatusPanel;
import com.loomcom.symon.ui.TraceLog;

public class Simulator extends JPanel {
	private final static Logger _logger = new Logger(Simulator.class.getName());
	
    private static final int  DEFAULT_FONT_SIZE = 12;
    private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, DEFAULT_FONT_SIZE);
    private static final int  CONSOLE_BORDER_WIDTH = 5;
    
    private static final String[] STEPS = {"1", "5", "10", "20", "50", "100"};

	private Console console;
	private StatusPanel statusPane;
	
    private JButton runStopButton;
    private JButton stepButton;
    private JButton softResetButton;
    private JButton hardResetButton;
    private JComboBox<String> stepCountBox;
    
    private TraceLog traceLog;
    private MemoryWindow memoryWindow;

    private JFileChooser fileChooser;
    
    private int stepsPerClick;

	public Simulator(Machine machine) {
		super();

		//setTitle("6502 Simulator - " + machine.getName());
		setLayout(new BorderLayout());

        // UI components used for I/O.
        console = new Console(80, 25, DEFAULT_FONT);
        statusPane = new StatusPanel(machine);

        console.setBorderWidth(CONSOLE_BORDER_WIDTH);

        // File Chooser
        fileChooser = new JFileChooser(System.getProperty("user.dir"));

        // Panel for Console and Buttons
        JPanel consoleContainer = new JPanel();
        JPanel buttonContainer = new JPanel();

        consoleContainer.setLayout(new BorderLayout());
        consoleContainer.setBorder(new EmptyBorder(10, 10, 10, 0));
        buttonContainer.setLayout(new FlowLayout());

        runStopButton = new JButton("Run");
        stepButton = new JButton("Step");
        softResetButton = new JButton("Soft Reset");
        hardResetButton = new JButton("Hard Reset");

        stepCountBox = new JComboBox<String>(STEPS);
        stepCountBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    @SuppressWarnings("rawtypes")
					JComboBox cb = (JComboBox) actionEvent.getSource();
                    stepsPerClick = Integer.parseInt((String) cb.getSelectedItem());
                } catch (NumberFormatException ex) {
                    stepsPerClick = 1;
                    stepCountBox.setSelectedIndex(0);
                }
            }
        });

        buttonContainer.add(runStopButton);
        buttonContainer.add(stepButton);
        buttonContainer.add(stepCountBox);
        buttonContainer.add(softResetButton);
        buttonContainer.add(hardResetButton);

        // Left side - console
        consoleContainer.add(console, BorderLayout.CENTER);
        add(consoleContainer, BorderLayout.LINE_START);

        // Right side - status pane
        add(statusPane, BorderLayout.LINE_END);

        // Bottom - buttons.
        add(buttonContainer, BorderLayout.PAGE_END);

        runStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            }
        });

        stepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
//                handleStep(stepsPerClick);
            }
        });

        softResetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // If this was a CTRL-click, do a hard reset.
//                handleReset(false);
            }
        });

        hardResetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // If this was a CTRL-click, do a hard reset.
//                handleReset(true);
            }
        });

        // Prepare the log window
        traceLog = new TraceLog();

        // Prepare the memory window
        memoryWindow = new MemoryWindow(machine.getBus());

        setVisible(true);

        console.requestFocus();
	}
}
