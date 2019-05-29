package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=FailoverStatus, data=[none, require, active, completed, relaunch], fields=null}</pre>
 */
// QApiEnumDescriptor{name=FailoverStatus, data=[none, require, active, completed, relaunch], fields=null}
public enum FailoverStatus {
	none("none"),
	require("require"),
	active("active"),
	completed("completed"),
	relaunch("relaunch"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ FailoverStatus(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}
