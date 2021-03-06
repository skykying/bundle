package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiUnionDescriptor{name=SocketAddressLegacy, discriminator=null, data={inet=InetSocketAddress, unix=UnixSocketAddress, vsock=VsockSocketAddress, fd=String}, innerTypes=null, fields=null, base=null, discriminatorField=null}</pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SocketAddressLegacy extends QApiType implements QApiUnion {
	public static enum Discriminator {
		inet,
		unix,
		vsock,
		fd,
		__NONE;
	}

	
	@Nonnull
	@JsonProperty("type")
	public Discriminator type;

	@Nonnull
	public final Discriminator getType() {
		return type;
	}

	// union {
	
	@JsonProperty("inet")
	@JsonUnwrapped
	@CheckForNull
	public InetSocketAddress inet;
	
	@JsonProperty("unix")
	@JsonUnwrapped
	@CheckForNull
	public UnixSocketAddress unix;
	
	@JsonProperty("vsock")
	@JsonUnwrapped
	@CheckForNull
	public VsockSocketAddress vsock;
	
	@JsonProperty("fd")
	@JsonUnwrapped
	@CheckForNull
	public String fd;
	// }

	@Nonnull
	public static SocketAddressLegacy inet(@Nonnull InetSocketAddress inet) {
		SocketAddressLegacy self = new SocketAddressLegacy();
		self.type = Discriminator.inet;
		self.inet = inet;
		return self;
	}

	@Nonnull
	public static SocketAddressLegacy unix(@Nonnull UnixSocketAddress unix) {
		SocketAddressLegacy self = new SocketAddressLegacy();
		self.type = Discriminator.unix;
		self.unix = unix;
		return self;
	}

	@Nonnull
	public static SocketAddressLegacy vsock(@Nonnull VsockSocketAddress vsock) {
		SocketAddressLegacy self = new SocketAddressLegacy();
		self.type = Discriminator.vsock;
		self.vsock = vsock;
		return self;
	}

	@Nonnull
	public static SocketAddressLegacy fd(@Nonnull String fd) {
		SocketAddressLegacy self = new SocketAddressLegacy();
		self.type = Discriminator.fd;
		self.fd = fd;
		return self;
	}

	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("inet");
		names.add("unix");
		names.add("vsock");
		names.add("fd");
		return names;
	}

	@JsonIgnore
	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("inet".equals(name))
			return inet;
		if ("unix".equals(name))
			return unix;
		if ("vsock".equals(name))
			return vsock;
		if ("fd".equals(name))
			return fd;
		return super.getFieldByName(name);
	}

	@JsonIgnore
	public boolean isValidUnion() {
		int count = 0;
		if (inet != null)
			count++;
		if (unix != null)
			count++;
		if (vsock != null)
			count++;
		if (fd != null)
			count++;
		return (count == 1);
	}
}
