package com.loomcom.symon.machines;

import com.loomcom.symon.Bus;
import com.loomcom.symon.Cpu;
import com.loomcom.symon.devices.Acia;
import com.loomcom.symon.devices.Crtc;
import com.loomcom.symon.devices.Memory;
import com.loomcom.symon.devices.Pia;
import com.loomcom.symon.exceptions.MemoryRangeException;

public class DynamicMachine implements Machine {

	@Override
	public Bus getBus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cpu getCpu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Memory getRam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Acia getAcia() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pia getPia() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Crtc getCrtc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Memory getRom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRom(Memory rom) throws MemoryRangeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRomBase() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRomSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMemorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
