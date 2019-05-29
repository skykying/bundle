package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=block-job-dismiss, returns=null, data={id=str}}</pre>
 */
// QApiCommandDescriptor{name=block-job-dismiss, returns=null, data={id=str}}
public class BlockJobDismissCommand extends QApiCommand<BlockJobDismissCommand.Arguments, BlockJobDismissCommand.Response> {
	/** Compound arguments to a BlockJobDismissCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("id")
		@Nonnull
		public java.lang.String id;

		public Arguments() {
		}

		public Arguments(java.lang.String id) {
			this.id = id;
		}
	}

	/** Response to a BlockJobDismissCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new BlockJobDismissCommand. */
	public BlockJobDismissCommand(@Nonnull BlockJobDismissCommand.Arguments argument) {
		super("block-job-dismiss", Response.class, argument);
	}

	/** Constructs a new BlockJobDismissCommand. */
	public BlockJobDismissCommand(java.lang.String id) {
		this(new Arguments(id));
	}
}
