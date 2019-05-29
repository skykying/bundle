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
 * <pre>QApiStructDescriptor{name=BlockdevCreateOptionsVpc, data={file=BlockdevRef, size=size, *subformat=BlockdevVpcSubformat, *force-size=bool}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockdevCreateOptionsVpc, data={file=BlockdevRef, size=size, *subformat=BlockdevVpcSubformat, *force-size=bool}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevCreateOptionsVpc extends QApiType {

	
	@JsonProperty("file")
	@Nonnull
	public BlockdevRef file;
	
	@JsonProperty("size")
	@Nonnull
	public long size;
	
	@JsonProperty("subformat")
	@CheckForNull
	public BlockdevVpcSubformat subformat;
	
	@JsonProperty("force-size")
	@CheckForNull
	public java.lang.Boolean forceSize;

	@Nonnull
	public BlockdevCreateOptionsVpc withFile(BlockdevRef value) {
		this.file = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsVpc withSize(long value) {
		this.size = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsVpc withSubformat(BlockdevVpcSubformat value) {
		this.subformat = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsVpc withForceSize(java.lang.Boolean value) {
		this.forceSize = value;
		return this;
	}

	public BlockdevCreateOptionsVpc() {
	}

	public BlockdevCreateOptionsVpc(BlockdevRef file, long size, BlockdevVpcSubformat subformat, java.lang.Boolean forceSize) {
		this.file = file;
		this.size = size;
		this.subformat = subformat;
		this.forceSize = forceSize;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("file");
		names.add("size");
		names.add("subformat");
		names.add("force-size");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("file".equals(name))
			return file;
		if ("size".equals(name))
			return size;
		if ("subformat".equals(name))
			return subformat;
		if ("force-size".equals(name))
			return forceSize;
		return super.getFieldByName(name);
	}
}
