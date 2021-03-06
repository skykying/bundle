package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=system_reset, returns=null, data=null}</pre>
 */
// QApiCommandDescriptor{name=system_reset, returns=null, data=null}
public class SystemResetCommand extends QApiCommand<java.lang.Void, SystemResetCommand.Response> {

	/** Response to a SystemResetCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new SystemResetCommand. */
	public SystemResetCommand() {
		super("system_reset", Response.class, null);
	}

}
