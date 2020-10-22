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
 * <pre>QApiStructDescriptor{name=QCryptoBlockOptionsQCow, data={*key-secret=str}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=QCryptoBlockOptionsQCow, data={*key-secret=str}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QCryptoBlockOptionsQCow extends QApiType {

	
	@JsonProperty("key-secret")
	@CheckForNull
	public java.lang.String keySecret;

	@Nonnull
	public QCryptoBlockOptionsQCow withKeySecret(java.lang.String value) {
		this.keySecret = value;
		return this;
	}

	public QCryptoBlockOptionsQCow() {
	}

	public QCryptoBlockOptionsQCow(java.lang.String keySecret) {
		this.keySecret = keySecret;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("key-secret");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("key-secret".equals(name))
			return keySecret;
		return super.getFieldByName(name);
	}
}