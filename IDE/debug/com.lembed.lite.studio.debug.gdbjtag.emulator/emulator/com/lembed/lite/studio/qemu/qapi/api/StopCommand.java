package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=stop, returns=null, data=null}</pre>
 */
// QApiCommandDescriptor{name=stop, returns=null, data=null}
public class StopCommand extends QApiCommand<java.lang.Void, StopCommand.Response> {

	/** Response to a StopCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new StopCommand. */
	public StopCommand() {
		super("stop", Response.class, null);
	}

}