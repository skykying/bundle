package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=blockdev-del, returns=null, data={node-name=str}}</pre>
 */
// QApiCommandDescriptor{name=blockdev-del, returns=null, data={node-name=str}}
public class BlockdevDelCommand extends QApiCommand<BlockdevDelCommand.Arguments, BlockdevDelCommand.Response> {
	/** Compound arguments to a BlockdevDelCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("node-name")
		@Nonnull
		public java.lang.String nodeName;

		public Arguments() {
		}

		public Arguments(java.lang.String nodeName) {
			this.nodeName = nodeName;
		}
	}

	/** Response to a BlockdevDelCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new BlockdevDelCommand. */
	public BlockdevDelCommand(@Nonnull BlockdevDelCommand.Arguments argument) {
		super("blockdev-del", Response.class, argument);
	}

	/** Constructs a new BlockdevDelCommand. */
	public BlockdevDelCommand(java.lang.String nodeName) {
		this(new Arguments(nodeName));
	}
}
