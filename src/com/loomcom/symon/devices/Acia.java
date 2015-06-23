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

import uk.org.wookey.atari.sim.Simulator;
import uk.org.wookey.atari.utils.Logger;

import com.loomcom.symon.MemoryRange;
import com.loomcom.symon.exceptions.MemoryRangeException;


/**
 * Abstract base class for ACIAS such as the 6551 and 6580
 */

public abstract class Acia extends Device {
	private final static Logger _logger = new Logger(Simulator.class.getName());

    boolean receiveIrqEnabled = false;
    boolean transmitIrqEnabled = false;
    boolean overrun = false;
    
	long lastTxWrite   = 0;
    long lastRxRead    = 0;
    int  baudRate      = 0;
    long baudRateDelay = 0;
	
	/**
     * Read/Write buffers
     */
    int rxChar = 0;
    int txChar = 0;

    boolean rxFull  = false;
    boolean txEmpty = true;
	
	
    public Acia(int size, String name) throws MemoryRangeException {
        super(new MemoryRange(size), name);
    }


    /*
     * Calculate the delay in nanoseconds between successive read/write operations, based on the
     * configured baud rate.
     */
    private long calculateBaudRateDelay() {
        if (baudRate > 0) {
            // TODO: This is a pretty rough approximation based on 8 bits per character,
            // and 1/baudRate per bit. It could certainly be improved
            return (long)((1.0 / baudRate) * 1000000000 * 8);
        } else {
            return 0;
        }
    }

    /**
     * @return The simulated baud rate in bps.
     */
    public int getBaudRate() {
        return baudRate;
    }

    /**
     * Set the baud rate of the simulated ACIA.
     *
     * @param rate The baud rate in bps. 0 means no simulated baud rate delay.
     */
    public void setBaudRate(int rate) {
        this.baudRate = rate;
		this.baudRateDelay = calculateBaudRateDelay();
    }

    /**
     * @return The contents of the status register.
     */
    public abstract int statusReg();

    public synchronized int rxRead() {
        lastRxRead = System.nanoTime();
        overrun = false;
        rxFull = false;
        return rxChar;
    }

    public synchronized void rxWrite(int data) {
    	_logger.logInfo("Attempt to put char '" + (char) data + "' into the rx reg");
    	
        if(rxFull) {
        	_logger.logError("RX Overrun");
            overrun = true;
        }
        
        rxFull = true;

        if (receiveIrqEnabled) {
            getBus().assertIrq();
        }

        rxChar = data;
    }

    public synchronized int txRead() {
        txEmpty = true;

        if (transmitIrqEnabled) {
            getBus().assertIrq();
        }

        return txChar;
    }

    public synchronized void txWrite(int data) {
        lastTxWrite = System.nanoTime();
        txChar = data;
        txEmpty = false;
    }

    /**
     * @return true if there is character data in the TX register.
     */
    public boolean hasTxChar() {
        return !txEmpty;
    }

    /**
     * @return true if there is character data in the RX register.
     */
    public boolean hasRxChar() {
        return rxFull;
    }

    public boolean hasUI() {
    	return true;
    }
}