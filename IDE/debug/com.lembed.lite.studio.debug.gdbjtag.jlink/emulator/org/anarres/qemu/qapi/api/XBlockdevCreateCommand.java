package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=x-blockdev-create, returns=null, data=BlockdevCreateOptions}</pre>
 */
// QApiCommandDescriptor{name=x-blockdev-create, returns=null, data=BlockdevCreateOptions}
public class XBlockdevCreateCommand extends QApiCommand<BlockdevCreateOptions, XBlockdevCreateCommand.Response> {

	/** Response to a XBlockdevCreateCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new XBlockdevCreateCommand. */
	public XBlockdevCreateCommand(@Nonnull BlockdevCreateOptions argument) {
		super("x-blockdev-create", Response.class, argument);
	}

}