package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=chardev-add, returns=ChardevReturn, data={id=str, backend=ChardevBackend}}</pre>
 */
// QApiCommandDescriptor{name=chardev-add, returns=ChardevReturn, data={id=str, backend=ChardevBackend}}
public class ChardevAddCommand extends QApiCommand<ChardevAddCommand.Arguments, ChardevAddCommand.Response> {
	/** Compound arguments to a ChardevAddCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("id")
		@Nonnull
		public java.lang.String id;
		
		@JsonProperty("backend")
		@Nonnull
		public ChardevBackend backend;

		public Arguments() {
		}

		public Arguments(java.lang.String id, ChardevBackend backend) {
			this.id = id;
			this.backend = backend;
		}
	}

	/** Response to a ChardevAddCommand. */
	public static class Response extends QApiResponse<ChardevReturn> {
	}

	/** Constructs a new ChardevAddCommand. */
	public ChardevAddCommand(@Nonnull ChardevAddCommand.Arguments argument) {
		super("chardev-add", Response.class, argument);
	}

	/** Constructs a new ChardevAddCommand. */
	public ChardevAddCommand(java.lang.String id, ChardevBackend backend) {
		this(new Arguments(id, backend));
	}
}
