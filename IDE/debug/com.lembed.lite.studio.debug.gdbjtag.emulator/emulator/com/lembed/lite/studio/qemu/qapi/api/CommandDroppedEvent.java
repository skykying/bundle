package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=COMMAND_DROPPED, data={id=any, reason=CommandDropReason}}</pre>
 */
// QApiEventDescriptor{name=COMMAND_DROPPED, data={id=any, reason=CommandDropReason}}
public class CommandDroppedEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("id")
		@Nonnull
		public java.lang.Object id;
		
		@JsonProperty("reason")
		@Nonnull
		public CommandDropReason reason;
	}

	@JsonProperty("data")
	public Data data;
}