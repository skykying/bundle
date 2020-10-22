package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=block_set_io_throttle, returns=null, data=BlockIOThrottle}</pre>
 */
// QApiCommandDescriptor{name=block_set_io_throttle, returns=null, data=BlockIOThrottle}
public class BlockSetIoThrottleCommand extends QApiCommand<BlockIOThrottle, BlockSetIoThrottleCommand.Response> {

	/** Response to a BlockSetIoThrottleCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new BlockSetIoThrottleCommand. */
	public BlockSetIoThrottleCommand(@Nonnull BlockIOThrottle argument) {
		super("block_set_io_throttle", Response.class, argument);
	}

}