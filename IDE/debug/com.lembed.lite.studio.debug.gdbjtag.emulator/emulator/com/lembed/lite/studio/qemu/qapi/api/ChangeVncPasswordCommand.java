package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=change-vnc-password, returns=null, data={password=str}}</pre>
 */
// QApiCommandDescriptor{name=change-vnc-password, returns=null, data={password=str}}
public class ChangeVncPasswordCommand extends QApiCommand<ChangeVncPasswordCommand.Arguments, ChangeVncPasswordCommand.Response> {
	/** Compound arguments to a ChangeVncPasswordCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("password")
		@Nonnull
		public java.lang.String password;

		public Arguments() {
		}

		public Arguments(java.lang.String password) {
			this.password = password;
		}
	}

	/** Response to a ChangeVncPasswordCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new ChangeVncPasswordCommand. */
	public ChangeVncPasswordCommand(@Nonnull ChangeVncPasswordCommand.Arguments argument) {
		super("change-vnc-password", Response.class, argument);
	}

	/** Constructs a new ChangeVncPasswordCommand. */
	public ChangeVncPasswordCommand(java.lang.String password) {
		this(new Arguments(password));
	}
}
