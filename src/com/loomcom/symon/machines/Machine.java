/*
 * Copyright (c) 2014 Seth J. Morabito <web@loomcom.com>
 *                    Maik Merten <maikmerten@googlemail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.loomcom.symon.machines;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import uk.org.wookey.atari.utils.Logger;

import com.loomcom.symon.Bus;
import com.loomcom.symon.Cpu;
import com.loomcom.symon.exceptions.MemoryRangeException;
import com.loomcom.symon.ui.MemoryWindow;
import com.loomcom.symon.ui.PreferencesDialog;
import com.loomcom.symon.ui.StatusPanel;
import com.loomcom.symon.ui.TraceLog;


public class Machine {
	private final static Logger _logger = new Logger("Machine");
	
	protected Bus bus;
	protected Cpu cpu;
	
	protected JPanel ui;
	
	private String name;
	public Machine(String name) {
		bus = new Bus(getBusWidth());
		cpu = new Cpu();
		
		setName(name);
		bus.addCpu(cpu);
		addDevices();
		
		ui = new JPanel();
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
    	bus.constructUI(ui);
    }
}