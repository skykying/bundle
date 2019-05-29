package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=TraceEventState, data=[unavailable, disabled, enabled], fields=null}</pre>
 */
// QApiEnumDescriptor{name=TraceEventState, data=[unavailable, disabled, enabled], fields=null}
public enum TraceEventState {
	unavailable("unavailable"),
	disabled("disabled"),
	enabled("enabled"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ TraceEventState(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}
