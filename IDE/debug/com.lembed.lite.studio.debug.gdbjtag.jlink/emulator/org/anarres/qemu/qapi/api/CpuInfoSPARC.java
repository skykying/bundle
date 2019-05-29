package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiStructDescriptor{name=CpuInfoSPARC, data={pc=int, npc=int}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=CpuInfoSPARC, data={pc=int, npc=int}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CpuInfoSPARC extends QApiType {

	
	@JsonProperty("pc")
	@Nonnull
	public long pc;
	
	@JsonProperty("npc")
	@Nonnull
	public long npc;

	@Nonnull
	public CpuInfoSPARC withPc(long value) {
		this.pc = value;
		return this;
	}

	@Nonnull
	public CpuInfoSPARC withNpc(long value) {
		this.npc = value;
		return this;
	}

	public CpuInfoSPARC() {
	}

	public CpuInfoSPARC(long pc, long npc) {
		this.pc = pc;
		this.npc = npc;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("pc");
		names.add("npc");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("pc".equals(name))
			return pc;
		if ("npc".equals(name))
			return npc;
		return super.getFieldByName(name);
	}
}
