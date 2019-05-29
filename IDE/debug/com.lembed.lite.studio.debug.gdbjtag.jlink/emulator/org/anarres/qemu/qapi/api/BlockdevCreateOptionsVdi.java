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
 * <pre>QApiStructDescriptor{name=BlockdevCreateOptionsVdi, data={file=BlockdevRef, size=size, *preallocation=PreallocMode}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockdevCreateOptionsVdi, data={file=BlockdevRef, size=size, *preallocation=PreallocMode}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevCreateOptionsVdi extends QApiType {

	
	@JsonProperty("file")
	@Nonnull
	public BlockdevRef file;
	
	@JsonProperty("size")
	@Nonnull
	public long size;
	
	@JsonProperty("preallocation")
	@CheckForNull
	public PreallocMode preallocation;

	@Nonnull
	public BlockdevCreateOptionsVdi withFile(BlockdevRef value) {
		this.file = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsVdi withSize(long value) {
		this.size = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsVdi withPreallocation(PreallocMode value) {
		this.preallocation = value;
		return this;
	}

	public BlockdevCreateOptionsVdi() {
	}

	public BlockdevCreateOptionsVdi(BlockdevRef file, long size, PreallocMode preallocation) {
		this.file = file;
		this.size = size;
		this.preallocation = preallocation;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("file");
		names.add("size");
		names.add("preallocation");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("file".equals(name))
			return file;
		if ("size".equals(name))
			return size;
		if ("preallocation".equals(name))
			return preallocation;
		return super.getFieldByName(name);
	}
}