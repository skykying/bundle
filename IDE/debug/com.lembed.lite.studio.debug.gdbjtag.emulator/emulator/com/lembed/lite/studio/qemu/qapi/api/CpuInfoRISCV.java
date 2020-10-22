package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiStructDescriptor{name=CpuInfoRISCV, data={pc=int}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=CpuInfoRISCV, data={pc=int}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CpuInfoRISCV extends QApiType {

	
	@JsonProperty("pc")
	@Nonnull
	public long pc;

	@Nonnull
	public CpuInfoRISCV withPc(long value) {
		this.pc = value;
		return this;
	}

	public CpuInfoRISCV() {
	}

	public CpuInfoRISCV(long pc) {
		this.pc = pc;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("pc");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("pc".equals(name))
			return pc;
		return super.getFieldByName(name);
	}
}