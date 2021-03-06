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
 * <pre>QApiStructDescriptor{name=CpuInfoTricore, data={PC=int}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=CpuInfoTricore, data={PC=int}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CpuInfoTricore extends QApiType {

	
	@JsonProperty("PC")
	@Nonnull
	public long PC;

	@Nonnull
	public CpuInfoTricore withPC(long value) {
		this.PC = value;
		return this;
	}

	public CpuInfoTricore() {
	}

	public CpuInfoTricore(long PC) {
		this.PC = PC;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("PC");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("PC".equals(name))
			return PC;
		return super.getFieldByName(name);
	}
}
