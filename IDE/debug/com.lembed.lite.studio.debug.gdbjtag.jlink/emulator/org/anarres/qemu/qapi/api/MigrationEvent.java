package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=MIGRATION, data={status=MigrationStatus}}</pre>
 */
// QApiEventDescriptor{name=MIGRATION, data={status=MigrationStatus}}
public class MigrationEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("status")
		@Nonnull
		public MigrationStatus status;
	}

	@JsonProperty("data")
	public Data data;
}
