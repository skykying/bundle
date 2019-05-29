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
 * <pre>QApiStructDescriptor{name=BlockIOThrottle, data={*device=str, *id=str, bps=int, bps_rd=int, bps_wr=int, iops=int, iops_rd=int, iops_wr=int, *bps_max=int, *bps_rd_max=int, *bps_wr_max=int, *iops_max=int, *iops_rd_max=int, *iops_wr_max=int, *bps_max_length=int, *bps_rd_max_length=int, *bps_wr_max_length=int, *iops_max_length=int, *iops_rd_max_length=int, *iops_wr_max_length=int, *iops_size=int, *group=str}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockIOThrottle, data={*device=str, *id=str, bps=int, bps_rd=int, bps_wr=int, iops=int, iops_rd=int, iops_wr=int, *bps_max=int, *bps_rd_max=int, *bps_wr_max=int, *iops_max=int, *iops_rd_max=int, *iops_wr_max=int, *bps_max_length=int, *bps_rd_max_length=int, *bps_wr_max_length=int, *iops_max_length=int, *iops_rd_max_length=int, *iops_wr_max_length=int, *iops_size=int, *group=str}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockIOThrottle extends QApiType {

	
	@JsonProperty("device")
	@CheckForNull
	public java.lang.String device;
	
	@JsonProperty("id")
	@CheckForNull
	public java.lang.String id;
	
	@JsonProperty("bps")
	@Nonnull
	public long bps;
	
	@JsonProperty("bps_rd")
	@Nonnull
	public long bpsRd;
	
	@JsonProperty("bps_wr")
	@Nonnull
	public long bpsWr;
	
	@JsonProperty("iops")
	@Nonnull
	public long iops;
	
	@JsonProperty("iops_rd")
	@Nonnull
	public long iopsRd;
	
	@JsonProperty("iops_wr")
	@Nonnull
	public long iopsWr;
	
	@JsonProperty("bps_max")
	@CheckForNull
	public java.lang.Long bpsMax;
	
	@JsonProperty("bps_rd_max")
	@CheckForNull
	public java.lang.Long bpsRdMax;
	
	@JsonProperty("bps_wr_max")
	@CheckForNull
	public java.lang.Long bpsWrMax;
	
	@JsonProperty("iops_max")
	@CheckForNull
	public java.lang.Long iopsMax;
	
	@JsonProperty("iops_rd_max")
	@CheckForNull
	public java.lang.Long iopsRdMax;
	
	@JsonProperty("iops_wr_max")
	@CheckForNull
	public java.lang.Long iopsWrMax;
	
	@JsonProperty("bps_max_length")
	@CheckForNull
	public java.lang.Long bpsMaxLength;
	
	@JsonProperty("bps_rd_max_length")
	@CheckForNull
	public java.lang.Long bpsRdMaxLength;
	
	@JsonProperty("bps_wr_max_length")
	@CheckForNull
	public java.lang.Long bpsWrMaxLength;
	
	@JsonProperty("iops_max_length")
	@CheckForNull
	public java.lang.Long iopsMaxLength;
	
	@JsonProperty("iops_rd_max_length")
	@CheckForNull
	public java.lang.Long iopsRdMaxLength;
	
	@JsonProperty("iops_wr_max_length")
	@CheckForNull
	public java.lang.Long iopsWrMaxLength;
	
	@JsonProperty("iops_size")
	@CheckForNull
	public java.lang.Long iopsSize;
	
	@JsonProperty("group")
	@CheckForNull
	public java.lang.String group;

	@Nonnull
	public BlockIOThrottle withDevice(java.lang.String value) {
		this.device = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withId(java.lang.String value) {
		this.id = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBps(long value) {
		this.bps = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBpsRd(long value) {
		this.bpsRd = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBpsWr(long value) {
		this.bpsWr = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIops(long value) {
		this.iops = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsRd(long value) {
		this.iopsRd = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsWr(long value) {
		this.iopsWr = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBpsMax(java.lang.Long value) {
		this.bpsMax = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBpsRdMax(java.lang.Long value) {
		this.bpsRdMax = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBpsWrMax(java.lang.Long value) {
		this.bpsWrMax = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsMax(java.lang.Long value) {
		this.iopsMax = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsRdMax(java.lang.Long value) {
		this.iopsRdMax = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsWrMax(java.lang.Long value) {
		this.iopsWrMax = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBpsMaxLength(java.lang.Long value) {
		this.bpsMaxLength = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBpsRdMaxLength(java.lang.Long value) {
		this.bpsRdMaxLength = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withBpsWrMaxLength(java.lang.Long value) {
		this.bpsWrMaxLength = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsMaxLength(java.lang.Long value) {
		this.iopsMaxLength = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsRdMaxLength(java.lang.Long value) {
		this.iopsRdMaxLength = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsWrMaxLength(java.lang.Long value) {
		this.iopsWrMaxLength = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withIopsSize(java.lang.Long value) {
		this.iopsSize = value;
		return this;
	}

	@Nonnull
	public BlockIOThrottle withGroup(java.lang.String value) {
		this.group = value;
		return this;
	}

	public BlockIOThrottle() {
	}

	public BlockIOThrottle(java.lang.String device, java.lang.String id, long bps, long bpsRd, long bpsWr, long iops, long iopsRd, long iopsWr, java.lang.Long bpsMax, java.lang.Long bpsRdMax, java.lang.Long bpsWrMax, java.lang.Long iopsMax, java.lang.Long iopsRdMax, java.lang.Long iopsWrMax, java.lang.Long bpsMaxLength, java.lang.Long bpsRdMaxLength, java.lang.Long bpsWrMaxLength, java.lang.Long iopsMaxLength, java.lang.Long iopsRdMaxLength, java.lang.Long iopsWrMaxLength, java.lang.Long iopsSize, java.lang.String group) {
		this.device = device;
		this.id = id;
		this.bps = bps;
		this.bpsRd = bpsRd;
		this.bpsWr = bpsWr;
		this.iops = iops;
		this.iopsRd = iopsRd;
		this.iopsWr = iopsWr;
		this.bpsMax = bpsMax;
		this.bpsRdMax = bpsRdMax;
		this.bpsWrMax = bpsWrMax;
		this.iopsMax = iopsMax;
		this.iopsRdMax = iopsRdMax;
		this.iopsWrMax = iopsWrMax;
		this.bpsMaxLength = bpsMaxLength;
		this.bpsRdMaxLength = bpsRdMaxLength;
		this.bpsWrMaxLength = bpsWrMaxLength;
		this.iopsMaxLength = iopsMaxLength;
		this.iopsRdMaxLength = iopsRdMaxLength;
		this.iopsWrMaxLength = iopsWrMaxLength;
		this.iopsSize = iopsSize;
		this.group = group;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("device");
		names.add("id");
		names.add("bps");
		names.add("bps_rd");
		names.add("bps_wr");
		names.add("iops");
		names.add("iops_rd");
		names.add("iops_wr");
		names.add("bps_max");
		names.add("bps_rd_max");
		names.add("bps_wr_max");
		names.add("iops_max");
		names.add("iops_rd_max");
		names.add("iops_wr_max");
		names.add("bps_max_length");
		names.add("bps_rd_max_length");
		names.add("bps_wr_max_length");
		names.add("iops_max_length");
		names.add("iops_rd_max_length");
		names.add("iops_wr_max_length");
		names.add("iops_size");
		names.add("group");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("device".equals(name))
			return device;
		if ("id".equals(name))
			return id;
		if ("bps".equals(name))
			return bps;
		if ("bps_rd".equals(name))
			return bpsRd;
		if ("bps_wr".equals(name))
			return bpsWr;
		if ("iops".equals(name))
			return iops;
		if ("iops_rd".equals(name))
			return iopsRd;
		if ("iops_wr".equals(name))
			return iopsWr;
		if ("bps_max".equals(name))
			return bpsMax;
		if ("bps_rd_max".equals(name))
			return bpsRdMax;
		if ("bps_wr_max".equals(name))
			return bpsWrMax;
		if ("iops_max".equals(name))
			return iopsMax;
		if ("iops_rd_max".equals(name))
			return iopsRdMax;
		if ("iops_wr_max".equals(name))
			return iopsWrMax;
		if ("bps_max_length".equals(name))
			return bpsMaxLength;
		if ("bps_rd_max_length".equals(name))
			return bpsRdMaxLength;
		if ("bps_wr_max_length".equals(name))
			return bpsWrMaxLength;
		if ("iops_max_length".equals(name))
			return iopsMaxLength;
		if ("iops_rd_max_length".equals(name))
			return iopsRdMaxLength;
		if ("iops_wr_max_length".equals(name))
			return iopsWrMaxLength;
		if ("iops_size".equals(name))
			return iopsSize;
		if ("group".equals(name))
			return group;
		return super.getFieldByName(name);
	}
}
