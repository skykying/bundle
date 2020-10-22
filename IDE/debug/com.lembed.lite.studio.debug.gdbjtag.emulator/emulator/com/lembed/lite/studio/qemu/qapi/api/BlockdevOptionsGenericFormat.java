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
 * <pre>QApiStructDescriptor{name=BlockdevOptionsGenericFormat, data={file=BlockdevRef}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockdevOptionsGenericFormat, data={file=BlockdevRef}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevOptionsGenericFormat extends QApiType {

	
	@JsonProperty("file")
	@Nonnull
	public BlockdevRef file;

	@Nonnull
	public BlockdevOptionsGenericFormat withFile(BlockdevRef value) {
		this.file = value;
		return this;
	}

	public BlockdevOptionsGenericFormat() {
	}

	public BlockdevOptionsGenericFormat(BlockdevRef file) {
		this.file = file;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("file");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("file".equals(name))
			return file;
		return super.getFieldByName(name);
	}
}