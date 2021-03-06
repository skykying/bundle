package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=WATCHDOG, data={action=WatchdogAction}}</pre>
 */
// QApiEventDescriptor{name=WATCHDOG, data={action=WatchdogAction}}
public class WatchdogEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("action")
		@Nonnull
		public WatchdogAction action;
	}

	@JsonProperty("data")
	public Data data;
}
