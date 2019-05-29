package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=SPICE_INITIALIZED, data={server=SpiceServerInfo, client=SpiceChannel}}</pre>
 */
// QApiEventDescriptor{name=SPICE_INITIALIZED, data={server=SpiceServerInfo, client=SpiceChannel}}
public class SpiceInitializedEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("server")
		@Nonnull
		public SpiceServerInfo server;
		
		@JsonProperty("client")
		@Nonnull
		public SpiceChannel client;
	}

	@JsonProperty("data")
	public Data data;
}