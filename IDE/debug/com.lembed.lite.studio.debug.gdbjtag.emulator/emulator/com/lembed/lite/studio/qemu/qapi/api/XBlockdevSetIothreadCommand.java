package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=x-blockdev-set-iothread, returns=null, data={node-name=str, iothread=StrOrNull, *force=bool}}</pre>
 */
// QApiCommandDescriptor{name=x-blockdev-set-iothread, returns=null, data={node-name=str, iothread=StrOrNull, *force=bool}}
public class XBlockdevSetIothreadCommand extends QApiCommand<XBlockdevSetIothreadCommand.Arguments, XBlockdevSetIothreadCommand.Response> {
	/** Compound arguments to a XBlockdevSetIothreadCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("node-name")
		@Nonnull
		public java.lang.String nodeName;
		
		@JsonProperty("iothread")
		@Nonnull
		public StrOrNull iothread;
		
		@JsonProperty("force")
		@CheckForNull
		public java.lang.Boolean force;

		public Arguments() {
		}

		public Arguments(java.lang.String nodeName, StrOrNull iothread, java.lang.Boolean force) {
			this.nodeName = nodeName;
			this.iothread = iothread;
			this.force = force;
		}
	}

	/** Response to a XBlockdevSetIothreadCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new XBlockdevSetIothreadCommand. */
	public XBlockdevSetIothreadCommand(@Nonnull XBlockdevSetIothreadCommand.Arguments argument) {
		super("x-blockdev-set-iothread", Response.class, argument);
	}

	/** Constructs a new XBlockdevSetIothreadCommand. */
	public XBlockdevSetIothreadCommand(java.lang.String nodeName, StrOrNull iothread, java.lang.Boolean force) {
		this(new Arguments(nodeName, iothread, force));
	}
}
