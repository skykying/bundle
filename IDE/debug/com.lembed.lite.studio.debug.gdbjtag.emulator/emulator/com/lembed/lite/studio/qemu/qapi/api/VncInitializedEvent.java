package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=VNC_INITIALIZED, data={server=VncServerInfo, client=VncClientInfo}}</pre>
 */
// QApiEventDescriptor{name=VNC_INITIALIZED, data={server=VncServerInfo, client=VncClientInfo}}
public class VncInitializedEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("server")
		@Nonnull
		public VncServerInfo server;
		
		@JsonProperty("client")
		@Nonnull
		public VncClientInfo client;
	}

	@JsonProperty("data")
	public Data data;
}
