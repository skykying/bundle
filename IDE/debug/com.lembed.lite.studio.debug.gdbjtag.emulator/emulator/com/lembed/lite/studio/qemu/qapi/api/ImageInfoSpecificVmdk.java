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
 * <pre>QApiStructDescriptor{name=ImageInfoSpecificVmdk, data={create-type=str, cid=int, parent-cid=int, extents=[ImageInfo]}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=ImageInfoSpecificVmdk, data={create-type=str, cid=int, parent-cid=int, extents=[ImageInfo]}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImageInfoSpecificVmdk extends QApiType {

	
	@JsonProperty("create-type")
	@Nonnull
	public java.lang.String createType;
	
	@JsonProperty("cid")
	@Nonnull
	public long cid;
	
	@JsonProperty("parent-cid")
	@Nonnull
	public long parentCid;
	
	@JsonProperty("extents")
	@Nonnull
	public java.util.List<ImageInfo> extents;

	@Nonnull
	public ImageInfoSpecificVmdk withCreateType(java.lang.String value) {
		this.createType = value;
		return this;
	}

	@Nonnull
	public ImageInfoSpecificVmdk withCid(long value) {
		this.cid = value;
		return this;
	}

	@Nonnull
	public ImageInfoSpecificVmdk withParentCid(long value) {
		this.parentCid = value;
		return this;
	}

	@Nonnull
	public ImageInfoSpecificVmdk withExtents(java.util.List<ImageInfo> value) {
		this.extents = value;
		return this;
	}

	public ImageInfoSpecificVmdk() {
	}

	public ImageInfoSpecificVmdk(java.lang.String createType, long cid, long parentCid, java.util.List<ImageInfo> extents) {
		this.createType = createType;
		this.cid = cid;
		this.parentCid = parentCid;
		this.extents = extents;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("create-type");
		names.add("cid");
		names.add("parent-cid");
		names.add("extents");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("create-type".equals(name))
			return createType;
		if ("cid".equals(name))
			return cid;
		if ("parent-cid".equals(name))
			return parentCid;
		if ("extents".equals(name))
			return extents;
		return super.getFieldByName(name);
	}
}
