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

import java.io.*;
import java.util.*;

import com.loomcom.symon.MemoryRange;
import com.loomcom.symon.exceptions.*;

public class Memory extends Device {
    private boolean readOnly;
    private int[] mem;

    /* Initialize all locations to 0x00 (BRK) */
    private static final int DEFAULT_FILL = 0x00;

    public Memory(int size, boolean readOnly) throws MemoryRangeException {
        super(new MemoryRange(size), readOnly ? "RO Memory" : "RW Memory");
        
        this.readOnly = readOnly;
        this.mem = new int[getMemoryRange().length()];
        this.fill(DEFAULT_FILL);
    }

    public Memory(int size) throws MemoryRangeException {
        this(size, false);
    }

    public static Memory makeROM(int size, File f) throws MemoryRangeException, IOException {
        Memory memory = new Memory(size, true);
        memory.loadFromFile(f);
        return memory;
    }

    public static Memory makeRAM(int size) throws MemoryRangeException {
        Memory memory = new Memory(size, false);
        return memory;
    }

    public void write(int address, int data) throws MemoryAccessException {
        if (readOnly) {
            throw new MemoryAccessException("Cannot write to read-only memory at address " + address);
        } else {
           	address = addressOffset(address);
           	this.mem[address] = data;
        }
    }

    /**
     * Load the memory from a file.
     *
     * @param file The file to read an array of bytes from.
     * @throws MemoryRangeException if the file and memory size do not match.
     * @throws IOException if the file read fails.
     */
    public void loadFromFile(File file) throws MemoryRangeException, IOException {
        if (file.canRead()) {
            long fileSize = file.length();

            if (fileSize > mem.length) {
                throw new MemoryRangeException("File will not fit in available memory.");
            } else {
                int i = 0;
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() != 0) {
                    mem[i++] = dis.readUnsignedByte();
                }
                dis.close();
            }
        } else {
            throw new IOException("Cannot open file " + file);
        }

    }

    public int read(int address) throws MemoryAccessException {
       	address = addressOffset(address);
        return this.mem[address];
    }

    public void fill(int val) {
        Arrays.fill(this.mem, val);
    }

    public int[] getDmaAccess() {
        return mem;
    }
    
    public int getSize() {
    	return getMemoryRange().length();
    }
}