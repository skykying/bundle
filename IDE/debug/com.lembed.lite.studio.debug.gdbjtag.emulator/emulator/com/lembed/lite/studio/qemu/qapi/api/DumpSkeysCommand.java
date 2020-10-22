package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=dump-skeys, returns=null, data={filename=str}}</pre>
 */
// QApiCommandDescriptor{name=dump-skeys, returns=null, data={filename=str}}
public class DumpSkeysCommand extends QApiCommand<DumpSkeysCommand.Arguments, DumpSkeysCommand.Response> {
	/** Compound arguments to a DumpSkeysCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("filename")
		@Nonnull
		public java.lang.String filename;

		public Arguments() {
		}

		public Arguments(java.lang.String filename) {
			this.filename = filename;
		}
	}

	/** Response to a DumpSkeysCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new DumpSkeysCommand. */
	public DumpSkeysCommand(@Nonnull DumpSkeysCommand.Arguments argument) {
		super("dump-skeys", Response.class, argument);
	}

	/** Constructs a new DumpSkeysCommand. */
	public DumpSkeysCommand(java.lang.String filename) {
		this(new Arguments(filename));
	}
}