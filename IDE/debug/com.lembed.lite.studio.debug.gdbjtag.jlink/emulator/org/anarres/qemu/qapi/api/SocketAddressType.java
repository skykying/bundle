package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=SocketAddressType, data=[inet, unix, vsock, fd], fields=null}</pre>
 */
// QApiEnumDescriptor{name=SocketAddressType, data=[inet, unix, vsock, fd], fields=null}
public enum SocketAddressType {
	inet("inet"),
	unix("unix"),
	vsock("vsock"),
	fd("fd"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ SocketAddressType(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}
