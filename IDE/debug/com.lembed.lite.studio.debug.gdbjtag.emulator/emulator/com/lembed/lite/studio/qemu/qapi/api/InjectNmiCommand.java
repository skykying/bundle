package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=inject-nmi, returns=null, data=null}</pre>
 */
// QApiCommandDescriptor{name=inject-nmi, returns=null, data=null}
public class InjectNmiCommand extends QApiCommand<java.lang.Void, InjectNmiCommand.Response> {

	/** Response to a InjectNmiCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new InjectNmiCommand. */
	public InjectNmiCommand() {
		super("inject-nmi", Response.class, null);
	}

}
