package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=drive-mirror, returns=null, data=DriveMirror}</pre>
 */
// QApiCommandDescriptor{name=drive-mirror, returns=null, data=DriveMirror}
public class DriveMirrorCommand extends QApiCommand<DriveMirror, DriveMirrorCommand.Response> {

	/** Response to a DriveMirrorCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new DriveMirrorCommand. */
	public DriveMirrorCommand(@Nonnull DriveMirror argument) {
		super("drive-mirror", Response.class, argument);
	}

}