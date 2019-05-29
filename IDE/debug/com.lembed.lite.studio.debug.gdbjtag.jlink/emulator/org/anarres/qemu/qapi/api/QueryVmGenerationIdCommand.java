package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=query-vm-generation-id, returns=GuidInfo, data=null}</pre>
 */
// QApiCommandDescriptor{name=query-vm-generation-id, returns=GuidInfo, data=null}
public class QueryVmGenerationIdCommand extends QApiCommand<java.lang.Void, QueryVmGenerationIdCommand.Response> {

	/** Response to a QueryVmGenerationIdCommand. */
	public static class Response extends QApiResponse<GuidInfo> {
	}

	/** Constructs a new QueryVmGenerationIdCommand. */
	public QueryVmGenerationIdCommand() {
		super("query-vm-generation-id", Response.class, null);
	}

}
