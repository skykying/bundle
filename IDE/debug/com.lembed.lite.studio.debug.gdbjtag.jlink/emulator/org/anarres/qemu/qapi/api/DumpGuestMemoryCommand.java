package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=dump-guest-memory, returns=null, data={paging=bool, protocol=str, *detach=bool, *begin=int, *length=int, *format=DumpGuestMemoryFormat}}</pre>
 */
// QApiCommandDescriptor{name=dump-guest-memory, returns=null, data={paging=bool, protocol=str, *detach=bool, *begin=int, *length=int, *format=DumpGuestMemoryFormat}}
public class DumpGuestMemoryCommand extends QApiCommand<DumpGuestMemoryCommand.Arguments, DumpGuestMemoryCommand.Response> {
	/** Compound arguments to a DumpGuestMemoryCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("paging")
		@Nonnull
		public boolean paging;
		
		@JsonProperty("protocol")
		@Nonnull
		public java.lang.String protocol;
		
		@JsonProperty("detach")
		@CheckForNull
		public java.lang.Boolean detach;
		
		@JsonProperty("begin")
		@CheckForNull
		public java.lang.Long begin;
		
		@JsonProperty("length")
		@CheckForNull
		public java.lang.Long length;
		
		@JsonProperty("format")
		@CheckForNull
		public DumpGuestMemoryFormat format;

		public Arguments() {
		}

		public Arguments(boolean paging, java.lang.String protocol, java.lang.Boolean detach, java.lang.Long begin, java.lang.Long length, DumpGuestMemoryFormat format) {
			this.paging = paging;
			this.protocol = protocol;
			this.detach = detach;
			this.begin = begin;
			this.length = length;
			this.format = format;
		}
	}

	/** Response to a DumpGuestMemoryCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new DumpGuestMemoryCommand. */
	public DumpGuestMemoryCommand(@Nonnull DumpGuestMemoryCommand.Arguments argument) {
		super("dump-guest-memory", Response.class, argument);
	}

	/** Constructs a new DumpGuestMemoryCommand. */
	public DumpGuestMemoryCommand(boolean paging, java.lang.String protocol, java.lang.Boolean detach, java.lang.Long begin, java.lang.Long length, DumpGuestMemoryFormat format) {
		this(new Arguments(paging, protocol, detach, begin, length, format));
	}
}