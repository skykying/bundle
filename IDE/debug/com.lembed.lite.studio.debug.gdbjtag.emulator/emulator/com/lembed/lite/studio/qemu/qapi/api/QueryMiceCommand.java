package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=query-mice, returns=[MouseInfo], data=null}</pre>
 */
// QApiCommandDescriptor{name=query-mice, returns=[MouseInfo], data=null}
public class QueryMiceCommand extends QApiCommand<java.lang.Void, QueryMiceCommand.Response> {

	/** Response to a QueryMiceCommand. */
	public static class Response extends QApiResponse<java.util.List<MouseInfo>> {
	}

	/** Constructs a new QueryMiceCommand. */
	public QueryMiceCommand() {
		super("query-mice", Response.class, null);
	}

}