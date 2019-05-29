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
 * <pre>QApiStructDescriptor{name=BlockdevCreateOptionsSheepdog, data={location=BlockdevOptionsSheepdog, size=size, *backing-file=str, *preallocation=PreallocMode, *redundancy=SheepdogRedundancy, *object-size=size}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockdevCreateOptionsSheepdog, data={location=BlockdevOptionsSheepdog, size=size, *backing-file=str, *preallocation=PreallocMode, *redundancy=SheepdogRedundancy, *object-size=size}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevCreateOptionsSheepdog extends QApiType {

	
	@JsonProperty("location")
	@Nonnull
	public BlockdevOptionsSheepdog location;
	
	@JsonProperty("size")
	@Nonnull
	public long size;
	
	@JsonProperty("backing-file")
	@CheckForNull
	public java.lang.String backingFile;
	
	@JsonProperty("preallocation")
	@CheckForNull
	public PreallocMode preallocation;
	
	@JsonProperty("redundancy")
	@CheckForNull
	public SheepdogRedundancy redundancy;
	
	@JsonProperty("object-size")
	@CheckForNull
	public java.lang.Long objectSize;

	@Nonnull
	public BlockdevCreateOptionsSheepdog withLocation(BlockdevOptionsSheepdog value) {
		this.location = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsSheepdog withSize(long value) {
		this.size = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsSheepdog withBackingFile(java.lang.String value) {
		this.backingFile = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsSheepdog withPreallocation(PreallocMode value) {
		this.preallocation = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsSheepdog withRedundancy(SheepdogRedundancy value) {
		this.redundancy = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsSheepdog withObjectSize(java.lang.Long value) {
		this.objectSize = value;
		return this;
	}

	public BlockdevCreateOptionsSheepdog() {
	}

	public BlockdevCreateOptionsSheepdog(BlockdevOptionsSheepdog location, long size, java.lang.String backingFile, PreallocMode preallocation, SheepdogRedundancy redundancy, java.lang.Long objectSize) {
		this.location = location;
		this.size = size;
		this.backingFile = backingFile;
		this.preallocation = preallocation;
		this.redundancy = redundancy;
		this.objectSize = objectSize;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("location");
		names.add("size");
		names.add("backing-file");
		names.add("preallocation");
		names.add("redundancy");
		names.add("object-size");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("location".equals(name))
			return location;
		if ("size".equals(name))
			return size;
		if ("backing-file".equals(name))
			return backingFile;
		if ("preallocation".equals(name))
			return preallocation;
		if ("redundancy".equals(name))
			return redundancy;
		if ("object-size".equals(name))
			return objectSize;
		return super.getFieldByName(name);
	}
}
