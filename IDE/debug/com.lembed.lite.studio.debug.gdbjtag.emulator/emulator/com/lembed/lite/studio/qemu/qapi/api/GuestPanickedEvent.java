package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=GUEST_PANICKED, data={action=GuestPanicAction, *info=GuestPanicInformation}}</pre>
 */
// QApiEventDescriptor{name=GUEST_PANICKED, data={action=GuestPanicAction, *info=GuestPanicInformation}}
public class GuestPanickedEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("action")
		@Nonnull
		public GuestPanicAction action;
		
		@JsonProperty("info")
		@CheckForNull
		public GuestPanicInformation info;
	}

	@JsonProperty("data")
	public Data data;
}