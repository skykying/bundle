package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=query-cpu-definitions, returns=[CpuDefinitionInfo], data=null}</pre>
 */
// QApiCommandDescriptor{name=query-cpu-definitions, returns=[CpuDefinitionInfo], data=null}
public class QueryCpuDefinitionsCommand extends QApiCommand<java.lang.Void, QueryCpuDefinitionsCommand.Response> {

	/** Response to a QueryCpuDefinitionsCommand. */
	public static class Response extends QApiResponse<java.util.List<CpuDefinitionInfo>> {
	}

	/** Constructs a new QueryCpuDefinitionsCommand. */
	public QueryCpuDefinitionsCommand() {
		super("query-cpu-definitions", Response.class, null);
	}

}