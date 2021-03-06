package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=VNC_CONNECTED, data={server=VncServerInfo, client=VncBasicInfo}}</pre>
 */
// QApiEventDescriptor{name=VNC_CONNECTED, data={server=VncServerInfo, client=VncBasicInfo}}
public class VncConnectedEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("server")
		@Nonnull
		public VncServerInfo server;
		
		@JsonProperty("client")
		@Nonnull
		public VncBasicInfo client;
	}

	@JsonProperty("data")
	public Data data;
}
