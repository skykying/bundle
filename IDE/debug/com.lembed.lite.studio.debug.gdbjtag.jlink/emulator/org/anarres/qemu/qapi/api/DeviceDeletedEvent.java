package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=DEVICE_DELETED, data={*device=str, path=str}}</pre>
 */
// QApiEventDescriptor{name=DEVICE_DELETED, data={*device=str, path=str}}
public class DeviceDeletedEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("device")
		@CheckForNull
		public java.lang.String device;
		
		@JsonProperty("path")
		@Nonnull
		public java.lang.String path;
	}

	@JsonProperty("data")
	public Data data;
}