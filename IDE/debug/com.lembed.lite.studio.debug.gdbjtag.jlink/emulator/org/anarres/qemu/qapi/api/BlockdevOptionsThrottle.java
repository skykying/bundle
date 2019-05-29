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
 * <pre>QApiStructDescriptor{name=BlockdevOptionsThrottle, data={throttle-group=str, file=BlockdevRef}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockdevOptionsThrottle, data={throttle-group=str, file=BlockdevRef}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevOptionsThrottle extends QApiType {

	
	@JsonProperty("throttle-group")
	@Nonnull
	public java.lang.String throttleGroup;
	
	@JsonProperty("file")
	@Nonnull
	public BlockdevRef file;

	@Nonnull
	public BlockdevOptionsThrottle withThrottleGroup(java.lang.String value) {
		this.throttleGroup = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsThrottle withFile(BlockdevRef value) {
		this.file = value;
		return this;
	}

	public BlockdevOptionsThrottle() {
	}

	public BlockdevOptionsThrottle(java.lang.String throttleGroup, BlockdevRef file) {
		this.throttleGroup = throttleGroup;
		this.file = file;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("throttle-group");
		names.add("file");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("throttle-group".equals(name))
			return throttleGroup;
		if ("file".equals(name))
			return file;
		return super.getFieldByName(name);
	}
}
