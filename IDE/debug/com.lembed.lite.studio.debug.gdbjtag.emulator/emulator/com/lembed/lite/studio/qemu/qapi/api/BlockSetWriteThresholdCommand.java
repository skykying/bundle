package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=block-set-write-threshold, returns=null, data={node-name=str, write-threshold=uint64}}</pre>
 */
// QApiCommandDescriptor{name=block-set-write-threshold, returns=null, data={node-name=str, write-threshold=uint64}}
public class BlockSetWriteThresholdCommand extends QApiCommand<BlockSetWriteThresholdCommand.Arguments, BlockSetWriteThresholdCommand.Response> {
	/** Compound arguments to a BlockSetWriteThresholdCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("node-name")
		@Nonnull
		public java.lang.String nodeName;
		
		@JsonProperty("write-threshold")
		@Nonnull
		public long writeThreshold;

		public Arguments() {
		}

		public Arguments(java.lang.String nodeName, long writeThreshold) {
			this.nodeName = nodeName;
			this.writeThreshold = writeThreshold;
		}
	}

	/** Response to a BlockSetWriteThresholdCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new BlockSetWriteThresholdCommand. */
	public BlockSetWriteThresholdCommand(@Nonnull BlockSetWriteThresholdCommand.Arguments argument) {
		super("block-set-write-threshold", Response.class, argument);
	}

	/** Constructs a new BlockSetWriteThresholdCommand. */
	public BlockSetWriteThresholdCommand(java.lang.String nodeName, long writeThreshold) {
		this(new Arguments(nodeName, writeThreshold));
	}
}
