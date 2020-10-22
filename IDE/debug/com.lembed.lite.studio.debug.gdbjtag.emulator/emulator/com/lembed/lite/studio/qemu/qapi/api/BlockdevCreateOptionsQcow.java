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
 * <pre>QApiStructDescriptor{name=BlockdevCreateOptionsQcow, data={file=BlockdevRef, size=size, *backing-file=str, *encrypt=QCryptoBlockCreateOptions}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockdevCreateOptionsQcow, data={file=BlockdevRef, size=size, *backing-file=str, *encrypt=QCryptoBlockCreateOptions}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevCreateOptionsQcow extends QApiType {

	
	@JsonProperty("file")
	@Nonnull
	public BlockdevRef file;
	
	@JsonProperty("size")
	@Nonnull
	public long size;
	
	@JsonProperty("backing-file")
	@CheckForNull
	public java.lang.String backingFile;
	
	@JsonProperty("encrypt")
	@CheckForNull
	public QCryptoBlockCreateOptions encrypt;

	@Nonnull
	public BlockdevCreateOptionsQcow withFile(BlockdevRef value) {
		this.file = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsQcow withSize(long value) {
		this.size = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsQcow withBackingFile(java.lang.String value) {
		this.backingFile = value;
		return this;
	}

	@Nonnull
	public BlockdevCreateOptionsQcow withEncrypt(QCryptoBlockCreateOptions value) {
		this.encrypt = value;
		return this;
	}

	public BlockdevCreateOptionsQcow() {
	}

	public BlockdevCreateOptionsQcow(BlockdevRef file, long size, java.lang.String backingFile, QCryptoBlockCreateOptions encrypt) {
		this.file = file;
		this.size = size;
		this.backingFile = backingFile;
		this.encrypt = encrypt;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("file");
		names.add("size");
		names.add("backing-file");
		names.add("encrypt");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("file".equals(name))
			return file;
		if ("size".equals(name))
			return size;
		if ("backing-file".equals(name))
			return backingFile;
		if ("encrypt".equals(name))
			return encrypt;
		return super.getFieldByName(name);
	}
}