package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=ACPISlotType, data=[DIMM, CPU], fields=null}</pre>
 */
// QApiEnumDescriptor{name=ACPISlotType, data=[DIMM, CPU], fields=null}
public enum ACPISlotType {
	DIMM("DIMM"),
	CPU("CPU"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ ACPISlotType(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}