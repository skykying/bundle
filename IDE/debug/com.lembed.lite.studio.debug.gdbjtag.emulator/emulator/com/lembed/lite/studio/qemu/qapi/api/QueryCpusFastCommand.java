package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=query-cpus-fast, returns=[CpuInfoFast], data=null}</pre>
 */
// QApiCommandDescriptor{name=query-cpus-fast, returns=[CpuInfoFast], data=null}
public class QueryCpusFastCommand extends QApiCommand<java.lang.Void, QueryCpusFastCommand.Response> {

	/** Response to a QueryCpusFastCommand. */
	public static class Response extends QApiResponse<java.util.List<CpuInfoFast>> {
	}

	/** Constructs a new QueryCpusFastCommand. */
	public QueryCpusFastCommand() {
		super("query-cpus-fast", Response.class, null);
	}

}