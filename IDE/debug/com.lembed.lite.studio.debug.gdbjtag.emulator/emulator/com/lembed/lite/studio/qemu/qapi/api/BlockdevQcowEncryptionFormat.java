package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=BlockdevQcowEncryptionFormat, data=[aes], fields=null}</pre>
 */
// QApiEnumDescriptor{name=BlockdevQcowEncryptionFormat, data=[aes], fields=null}
public enum BlockdevQcowEncryptionFormat {
	aes("aes"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ BlockdevQcowEncryptionFormat(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}