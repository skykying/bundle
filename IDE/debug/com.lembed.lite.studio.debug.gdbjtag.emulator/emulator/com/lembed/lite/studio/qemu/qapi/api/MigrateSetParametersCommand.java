package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=migrate-set-parameters, returns=null, data=MigrateSetParameters}</pre>
 */
// QApiCommandDescriptor{name=migrate-set-parameters, returns=null, data=MigrateSetParameters}
public class MigrateSetParametersCommand extends QApiCommand<MigrateSetParameters, MigrateSetParametersCommand.Response> {

	/** Response to a MigrateSetParametersCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new MigrateSetParametersCommand. */
	public MigrateSetParametersCommand(@Nonnull MigrateSetParameters argument) {
		super("migrate-set-parameters", Response.class, argument);
	}

}
