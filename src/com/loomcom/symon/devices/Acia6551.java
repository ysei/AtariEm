/*
 * Copyright (c) 2014 Seth J. Morabito <web@loomcom.com>
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

package com.loomcom.symon.devices;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import uk.org.wookey.atari.sim.Simulator;
import uk.org.wookey.atari.ui.RegisterPanel;
import uk.org.wookey.atari.utils.Logger;

import com.grahamedgecombe.jterminal.JTerminal;
import com.loomcom.symon.exceptions.MemoryAccessException;
import com.loomcom.symon.exceptions.MemoryRangeException;


/**
 * This is a simulation of the MOS 6551 ACIA, with limited
 * functionality.  Interrupts are not supported.
 * <p/>
 * Unlike a 16550 UART, the 6551 ACIA has only one-byte transmit and
 * receive buffers. It is the programmer's responsibility to check the
 * status (full or empty) for transmit and receive buffers before
 * writing / reading.
 */
public class Acia6551 extends Acia implements KeyListener {
	private final static Logger _logger = new Logger(Acia.class.getName());

	public static final int ACIA_SIZE = 4;

    static final int DATA_REG = 0;
    static final int STAT_REG = 1;
    static final int CMND_REG = 2;
    static final int CTRL_REG = 3;

    /**
     * Registers. These are ignored in the current implementation.
     */
    private int commandRegister;
    private int controlRegister;
    
    private RegisterPanel txReg;
    private RegisterPanel rxReg;
    private RegisterPanel statusReg;
    private RegisterPanel commandReg;
    private RegisterPanel controlReg;
    
    private JTextArea terminal;

    public Acia6551(int address) throws MemoryRangeException {
        super(ACIA_SIZE, "ACIA6551");
        
        JPanel dataRegs = new JPanel();
        txReg = new RegisterPanel("TX Data", 8);
        rxReg = new RegisterPanel("RX Data", 8);
        dataRegs.add(txReg);
        dataRegs.add(rxReg);        
        
        ui.add(dataRegs);
        
        statusReg = new RegisterPanel("Status Reg", 8);
        ui.add(statusReg);
        
        commandReg = new RegisterPanel("Command Reg", 8);
        ui.add(commandReg);
        
        controlReg = new RegisterPanel("Control Reg", 8);
        ui.add(controlReg);
        
        terminal = new JTextArea(5, 30);
        terminal.addKeyListener(this);
        ui.add(terminal);
    }

    @Override
    public int read(int address) throws MemoryAccessException {
       	address = addressOffset(address);

       	switch (address) {
            case DATA_REG:
                return rxRead();
            case STAT_REG:
                return statusReg();
            case CMND_REG:
                return commandRegister;
            case CTRL_REG:
                return controlRegister;
            default:
                throw new MemoryAccessException("No register.");
        }
    }

    @Override
    public void write(int address, int data) throws MemoryAccessException {
       	address = addressOffset(address);

       	switch (address) {
            case 0:
                txWrite(data);
                txReg.set(data);
                terminal.setText(terminal.getText() + "x");
                break;
            case 1:
                reset();
                statusReg.set(data);
                break;
            case 2:
                setCommandRegister(data);
                commandReg.set(data);
                break;
            case 3:
                setControlRegister(data);
                controlReg.set(data);
                break;
            default:
                throw new MemoryAccessException("No register.");
        }
    }


    private void setCommandRegister(int data) {
        commandRegister = data;

        // Bit 1 controls receiver IRQ behavior
        receiveIrqEnabled = (commandRegister & 0x02) == 0;
        // Bits 2 & 3 controls transmit IRQ behavior
        transmitIrqEnabled = (commandRegister & 0x08) == 0 && (commandRegister & 0x04) != 0;
    }

    /**
     * Set the control register and associated state.
     *
     * @param data
     */
    private void setControlRegister(int data) {
        controlRegister = data;
        int rate = 0;

        // If the value of the data is 0, this is a request to reset,
        // otherwise it's a control update.

        if (data == 0) {
            reset();
        } else {
            // Mask the lower three bits to get the baud rate.
            int baudSelector = data & 0x0f;
            switch (baudSelector) {
                case 0:
                    rate = 0;
                    break;
                case 1:
                    rate = 50;
                    break;
                case 2:
                    rate = 75;
                    break;
                case 3:
                    rate = 110; // Real rate is actually 109.92
                    break;
                case 4:
                    rate = 135; // Real rate is actually 134.58
                    break;
                case 5:
                    rate = 150;
                    break;
                case 6:
                    rate = 300;
                    break;
                case 7:
                    rate = 600;
                    break;
                case 8:
                    rate = 1200;
                    break;
                case 9:
                    rate = 1800;
                    break;
                case 10:
                    rate = 2400;
                    break;
                case 11:
                    rate = 3600;
                    break;
                case 12:
                    rate = 4800;
                    break;
                case 13:
                    rate = 7200;
                    break;
                case 14:
                    rate = 9600;
                    break;
                case 15:
                    rate = 19200;
                    break;
            }

            setBaudRate(rate);
        }
    }


    /**
     * @return The contents of the status register.
     */
    @Override
    public int statusReg() {
        // TODO: Parity Error, Framing Error, DTR, DSR, and Interrupt flags.
        int stat = 0;
        if (rxFull && System.nanoTime() >= (lastRxRead + baudRateDelay)) {
            stat |= 0x08;
        }
        if (txEmpty && System.nanoTime() >= (lastTxWrite + baudRateDelay)) {
            stat |= 0x10;
        }
        if (overrun) {
            stat |= 0x04;
        }
        return stat;
    }

    private synchronized void reset() {
        txChar = 0;
        txEmpty = true;
        rxChar = 0;
        rxFull = false;
        receiveIrqEnabled = false;
        transmitIrqEnabled = false;
    }

    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
        //displayInfo(e, "KEY TYPED: ");
    }

    /** Handle the key-pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
    	displayInfo(e, "PRESS");
    	
        char c = e.getKeyChar();
        
        rxReg.set(c);
        rxWrite(c);
    }

    /** Handle the key-released event from the text field. */
    public void keyReleased(KeyEvent e) {
        //displayInfo(e, "KEY RELEASED: ");
    }
    
    private void displayInfo(KeyEvent e, String keyStatus) {     
        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        
        _logger.logInfo(keyStatus);
        
        String keyString;
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
        } else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
        }
        _logger.logInfo(keyString);
        
        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no extended modifiers)";
        }
        _logger.logInfo(modString);
        
        String actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }
        _logger.logInfo(actionString);
        
        String locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }
        
        _logger.logInfo(locationString);
    }
}
