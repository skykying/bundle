package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=query-vnc, returns=VncInfo, data=null}</pre>
 */
// QApiCommandDescriptor{name=query-vnc, returns=VncInfo, data=null}
public class QueryVncCommand extends QApiCommand<java.lang.Void, QueryVncCommand.Response> {

	/** Response to a QueryVncCommand. */
	public static class Response extends QApiResponse<VncInfo> {
	}

	/** Constructs a new QueryVncCommand. */
	public QueryVncCommand() {
		super("query-vnc", Response.class, null);
	}

}
