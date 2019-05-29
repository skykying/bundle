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
 * <pre>QApiStructDescriptor{name=BlockDirtyBitmapSha256, data={sha256=str}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockDirtyBitmapSha256, data={sha256=str}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockDirtyBitmapSha256 extends QApiType {

	
	@JsonProperty("sha256")
	@Nonnull
	public java.lang.String sha256;

	@Nonnull
	public BlockDirtyBitmapSha256 withSha256(java.lang.String value) {
		this.sha256 = value;
		return this;
	}

	public BlockDirtyBitmapSha256() {
	}

	public BlockDirtyBitmapSha256(java.lang.String sha256) {
		this.sha256 = sha256;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("sha256");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("sha256".equals(name))
			return sha256;
		return super.getFieldByName(name);
	}
}