package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=DumpGuestMemoryFormat, data=[elf, kdump-zlib, kdump-lzo, kdump-snappy], fields=null}</pre>
 */
// QApiEnumDescriptor{name=DumpGuestMemoryFormat, data=[elf, kdump-zlib, kdump-lzo, kdump-snappy], fields=null}
public enum DumpGuestMemoryFormat {
	elf("elf"),
	kdump_zlib("kdump-zlib"),
	kdump_lzo("kdump-lzo"),
	kdump_snappy("kdump-snappy"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ DumpGuestMemoryFormat(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}