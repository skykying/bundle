package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=SHUTDOWN, data={guest=bool}}</pre>
 */
// QApiEventDescriptor{name=SHUTDOWN, data={guest=bool}}
public class ShutdownEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("guest")
		@Nonnull
		public boolean guest;
	}

	@JsonProperty("data")
	public Data data;
}
