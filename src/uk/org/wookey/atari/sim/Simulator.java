package uk.org.wookey.atari.sim;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import uk.org.wookey.atari.utils.Logger;

import com.loomcom.symon.Cpu;
import com.loomcom.symon.exceptions.MemoryAccessException;
import com.loomcom.symon.machines.Machine;
import com.loomcom.symon.ui.MemoryWindow;
import com.loomcom.symon.ui.StatusPanel;
import com.loomcom.symon.ui.TraceLog;

public class Simulator extends JPanel {
	private static final long serialVersionUID = 1L;

	private final static Logger _logger = new Logger(Simulator.class.getName());
	
    private static final String[] STEPS = {"1", "5", "10", "20", "50", "100"};

	private StatusPanel statusPane;
	
	private JPanel machinePane;
	
    private JButton runStopButton;
    private JButton stepButton;
    private JButton softResetButton;
    private JButton hardResetButton;
    private JComboBox<String> stepCountBox;
    
    private TraceLog traceLog;
    private MemoryWindow memoryWindow;

    private JFileChooser fileChooser;
    
    private int stepsPerClick;
    
    private Cpu cpu;
    
    private SimRunner simRunner;

	public Simulator(Machine machine) {
		super();
		
		stepsPerClick = 1;
		
		cpu = machine.getCpu();
		try {
			cpu.reset();
		} catch (MemoryAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//setTitle("6502 Simulator - " + machine.getName());
		setLayout(new BorderLayout());

        // UI components used for I/O.
        statusPane = new StatusPanel(machine);
        statusPane.updateState();
        
        // UI component for machine specific components
        machinePane = machine.getUI();
        
        
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
        add(consoleContainer, BorderLayout.LINE_START);
        
        // middle - machine panel
        JScrollPane scroller = new JScrollPane(machinePane);
        add(scroller, BorderLayout.CENTER);
        
        // Right side - status pane
        add(statusPane, BorderLayout.LINE_END);

        // Bottom - buttons.
        add(buttonContainer, BorderLayout.PAGE_END);

        runStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            	handleRun();
            }
        });

        stepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                handleStep(stepsPerClick);
            }
        });

        softResetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                handleReset(false);
            }
        });

        hardResetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                handleReset(true);
            }
        });

        // Prepare the log window
        traceLog = new TraceLog();

        // Prepare the memory window
        memoryWindow = new MemoryWindow(machine.getBus());
        add(memoryWindow, BorderLayout.LINE_START);

        setVisible(true);

        //console.requestFocus();
        
        _logger.logInfo("Simulator created and initialised");
		
        _logger.logInfo("Create simRunner thread");
        simRunner = new SimRunner();
        _logger.logInfo("Starting SimRunner thread");
        simRunner.start();

        // Now the simRunner thread is alive, set a timer to update the statusPane every
        // so often (only if simRunner is actually doing anything)
        Timer SimpleTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (simRunner.isRunning()) {
            		statusPane.updateState();
            	}
            }
        });
        SimpleTimer.start();
	}
	
	private void handleRun() {
		// Starting or stopping?
		_logger.logInfo("in handleRun()");
		
		if (!simRunner.isRunning()) {
			runStopButton.setText("Stop");
			stepButton.setEnabled(false);
			//statusPane.setGreyed(true);
			
			simRunner.setRunning(true);
		}
		else {
			_logger.logInfo("Stopping SimRunner");
			simRunner.setRunning(false);
			
			// Wait for simRunner to actually stop
			while (simRunner.isRunning()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			statusPane.updateState();
			
			runStopButton.setText("Run");
			stepButton.setEnabled(true);
			
			//statusPane.setGreyed(false);
		}
	}
	
	private void handleReset(boolean hard) {
		_logger.logInfo("Reset CPU");
		try {
			cpu.reset();
			statusPane.updateState();
		} catch (MemoryAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleStep(int numSteps) {
		_logger.logInfo("Step - " + numSteps);
		try {
			cpu.step(numSteps);
			statusPane.updateState();
		} catch (MemoryAccessException e) {
			_logger.logError("Processor threw an exception", e);
		}
	}

	
	
	
	private class SimRunner extends Thread {
		private boolean running = false;
		
		@Override
		public void run() {
			while (true) {
				while (isRunning()) {
					try {
						cpu.step();
					} catch (MemoryAccessException e) {
						_logger.logError("Processor threw an exception", e);
						setRunning(false);
					}	
				}

				// wait a bit before checking again
				try {
					sleep(100);
				} catch (InterruptedException e) {
					_logger.logError("simRunner caught an InterruptedException", e);
				}
			}
		}
		
		public synchronized boolean isRunning() {
			return running;
		}
		
		public synchronized void setRunning(boolean runningOrNot) {
			running = runningOrNot;
		}
	}
}
