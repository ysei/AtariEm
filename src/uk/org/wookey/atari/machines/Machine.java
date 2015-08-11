package uk.org.wookey.atari.machines;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import uk.org.wookey.atari.architecture.Bus;
import uk.org.wookey.atari.architecture.Cpu;
import uk.org.wookey.atari.labels.LabelTable;
import uk.org.wookey.atari.utils.Logger;

public class Machine {
	private final static Logger _logger = new Logger("Machine");
	
	protected Bus bus;
	protected Cpu cpu;
	protected LabelTable labels;
	
	protected JPanel ui;
	
	private String name;
	public Machine(String name) {
		labels = new LabelTable();
		
		bus = new Bus(getBusWidth(), labels);
		cpu = new Cpu();
		
		setName(name);
		bus.addCpu(cpu);
		addDevices();
		
		ui = new JPanel();
		ui.setLayout(new BorderLayout());
		ui.setBackground(new Color(160, 200, 120));
		createCoreUI();
		
		bus.dumpState();
	}
	
	public void addDevices() {
	}
	
	public int getBusWidth() {
		return 16;
	}
	
    public Bus getBus() {
    	return bus;
    }
    
    public Cpu getCpu() {
    	return cpu;
    }
    
    public LabelTable getLabelTable() {
    	return labels;
    }
    
    public JPanel getUI() {
    	return ui;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return name;
    }
    
    private final void createCoreUI() {
    	bus.constructUI();
    	
    	JPanel busPane = bus.getUI();
    	ui.add(busPane, BorderLayout.LINE_START);
    }
}