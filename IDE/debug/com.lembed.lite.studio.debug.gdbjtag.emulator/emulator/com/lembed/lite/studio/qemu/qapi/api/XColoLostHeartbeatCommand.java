package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=x-colo-lost-heartbeat, returns=null, data=null}</pre>
 */
// QApiCommandDescriptor{name=x-colo-lost-heartbeat, returns=null, data=null}
public class XColoLostHeartbeatCommand extends QApiCommand<java.lang.Void, XColoLostHeartbeatCommand.Response> {

	/** Response to a XColoLostHeartbeatCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new XColoLostHeartbeatCommand. */
	public XColoLostHeartbeatCommand() {
		super("x-colo-lost-heartbeat", Response.class, null);
	}

}