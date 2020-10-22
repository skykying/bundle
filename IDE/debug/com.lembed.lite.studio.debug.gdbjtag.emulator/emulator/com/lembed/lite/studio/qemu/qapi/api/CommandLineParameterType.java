package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=CommandLineParameterType, data=[string, boolean, number, size], fields=null}</pre>
 */
// QApiEnumDescriptor{name=CommandLineParameterType, data=[string, boolean, number, size], fields=null}
public enum CommandLineParameterType {
	string("string"),
	_boolean("boolean"),
	number("number"),
	size("size"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ CommandLineParameterType(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}