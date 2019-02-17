package com.lembed.lite.studio.debug.gdbjtag.jlink;

import java.util.Collection;

import org.eclipse.cdt.debug.gdbjtag.core.jtagdevice.DefaultGDBJtagDeviceImpl;

public class Jlink extends DefaultGDBJtagDeviceImpl {

	@Override
	public void doDelay(int delay, Collection<String> commands) {
	}

	@Override
	public void doHalt(Collection<String> commands) {
	}

	@Override
	public void doReset(Collection<String> commands) {
	}

}
