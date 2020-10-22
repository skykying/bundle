package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=migrate-set-capabilities, returns=null, data={capabilities=[MigrationCapabilityStatus]}}</pre>
 */
// QApiCommandDescriptor{name=migrate-set-capabilities, returns=null, data={capabilities=[MigrationCapabilityStatus]}}
public class MigrateSetCapabilitiesCommand extends QApiCommand<MigrateSetCapabilitiesCommand.Arguments, MigrateSetCapabilitiesCommand.Response> {
	/** Compound arguments to a MigrateSetCapabilitiesCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("capabilities")
		@Nonnull
		public java.util.List<MigrationCapabilityStatus> capabilities;

		public Arguments() {
		}

		public Arguments(java.util.List<MigrationCapabilityStatus> capabilities) {
			this.capabilities = capabilities;
		}
	}

	/** Response to a MigrateSetCapabilitiesCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new MigrateSetCapabilitiesCommand. */
	public MigrateSetCapabilitiesCommand(@Nonnull MigrateSetCapabilitiesCommand.Arguments argument) {
		super("migrate-set-capabilities", Response.class, argument);
	}

	/** Constructs a new MigrateSetCapabilitiesCommand. */
	public MigrateSetCapabilitiesCommand(java.util.List<MigrationCapabilityStatus> capabilities) {
		this(new Arguments(capabilities));
	}
}