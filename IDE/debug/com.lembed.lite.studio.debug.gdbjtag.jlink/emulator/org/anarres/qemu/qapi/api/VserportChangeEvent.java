package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=VSERPORT_CHANGE, data={id=str, open=bool}}</pre>
 */
// QApiEventDescriptor{name=VSERPORT_CHANGE, data={id=str, open=bool}}
public class VserportChangeEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("id")
		@Nonnull
		public java.lang.String id;
		
		@JsonProperty("open")
		@Nonnull
		public boolean open;
	}

	@JsonProperty("data")
	public Data data;
}