package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=chardev-change, returns=ChardevReturn, data={id=str, backend=ChardevBackend}}</pre>
 */
// QApiCommandDescriptor{name=chardev-change, returns=ChardevReturn, data={id=str, backend=ChardevBackend}}
public class ChardevChangeCommand extends QApiCommand<ChardevChangeCommand.Arguments, ChardevChangeCommand.Response> {
	/** Compound arguments to a ChardevChangeCommand. */
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

	/** Response to a ChardevChangeCommand. */
	public static class Response extends QApiResponse<ChardevReturn> {
	}

	/** Constructs a new ChardevChangeCommand. */
	public ChardevChangeCommand(@Nonnull ChardevChangeCommand.Arguments argument) {
		super("chardev-change", Response.class, argument);
	}

	/** Constructs a new ChardevChangeCommand. */
	public ChardevChangeCommand(java.lang.String id, ChardevBackend backend) {
		this(new Arguments(id, backend));
	}
}