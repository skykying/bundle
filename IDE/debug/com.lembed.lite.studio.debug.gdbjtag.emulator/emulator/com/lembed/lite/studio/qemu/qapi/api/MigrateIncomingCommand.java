package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=migrate-incoming, returns=null, data={uri=str}}</pre>
 */
// QApiCommandDescriptor{name=migrate-incoming, returns=null, data={uri=str}}
public class MigrateIncomingCommand extends QApiCommand<MigrateIncomingCommand.Arguments, MigrateIncomingCommand.Response> {
	/** Compound arguments to a MigrateIncomingCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("uri")
		@Nonnull
		public java.lang.String uri;

		public Arguments() {
		}

		public Arguments(java.lang.String uri) {
			this.uri = uri;
		}
	}

	/** Response to a MigrateIncomingCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new MigrateIncomingCommand. */
	public MigrateIncomingCommand(@Nonnull MigrateIncomingCommand.Arguments argument) {
		super("migrate-incoming", Response.class, argument);
	}

	/** Constructs a new MigrateIncomingCommand. */
	public MigrateIncomingCommand(java.lang.String uri) {
		this(new Arguments(uri));
	}
}
