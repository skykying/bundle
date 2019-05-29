package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=RockerPortDuplex, data=[half, full], fields=null}</pre>
 */
// QApiEnumDescriptor{name=RockerPortDuplex, data=[half, full], fields=null}
public enum RockerPortDuplex {
	half("half"),
	full("full"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ RockerPortDuplex(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}