package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=xen-set-global-dirty-log, returns=null, data={enable=bool}}</pre>
 */
// QApiCommandDescriptor{name=xen-set-global-dirty-log, returns=null, data={enable=bool}}
public class XenSetGlobalDirtyLogCommand extends QApiCommand<XenSetGlobalDirtyLogCommand.Arguments, XenSetGlobalDirtyLogCommand.Response> {
	/** Compound arguments to a XenSetGlobalDirtyLogCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("enable")
		@Nonnull
		public boolean enable;

		public Arguments() {
		}

		public Arguments(boolean enable) {
			this.enable = enable;
		}
	}

	/** Response to a XenSetGlobalDirtyLogCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new XenSetGlobalDirtyLogCommand. */
	public XenSetGlobalDirtyLogCommand(@Nonnull XenSetGlobalDirtyLogCommand.Arguments argument) {
		super("xen-set-global-dirty-log", Response.class, argument);
	}

	/** Constructs a new XenSetGlobalDirtyLogCommand. */
	public XenSetGlobalDirtyLogCommand(boolean enable) {
		this(new Arguments(enable));
	}
}