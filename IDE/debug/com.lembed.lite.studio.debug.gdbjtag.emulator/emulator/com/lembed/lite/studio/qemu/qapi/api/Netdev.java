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
 * <pre>QApiUnionDescriptor{name=Netdev, discriminator=type, data={none=NetdevNoneOptions, nic=NetLegacyNicOptions, user=NetdevUserOptions, tap=NetdevTapOptions, l2tpv3=NetdevL2TPv3Options, socket=NetdevSocketOptions, vde=NetdevVdeOptions, bridge=NetdevBridgeOptions, hubport=NetdevHubPortOptions, netmap=NetdevNetmapOptions, vhost-user=NetdevVhostUserOptions}, innerTypes=null, fields=null, base={id=str, type=NetClientDriver}, discriminatorField=null}</pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Netdev extends NetdevBase implements QApiUnion {

	// union {
	
	@JsonProperty("none")
	@JsonUnwrapped
	@CheckForNull
	public NetdevNoneOptions none;
	
	@JsonProperty("nic")
	@JsonUnwrapped
	@CheckForNull
	public NetLegacyNicOptions nic;
	
	@JsonProperty("user")
	@JsonUnwrapped
	@CheckForNull
	public NetdevUserOptions user;
	
	@JsonProperty("tap")
	@JsonUnwrapped
	@CheckForNull
	public NetdevTapOptions tap;
	
	@JsonProperty("l2tpv3")
	@JsonUnwrapped
	@CheckForNull
	public NetdevL2TPv3Options l2tpv3;
	
	@JsonProperty("socket")
	@JsonUnwrapped
	@CheckForNull
	public NetdevSocketOptions socket;
	
	@JsonProperty("vde")
	@JsonUnwrapped
	@CheckForNull
	public NetdevVdeOptions vde;
	
	@JsonProperty("bridge")
	@JsonUnwrapped
	@CheckForNull
	public NetdevBridgeOptions bridge;
	
	@JsonProperty("hubport")
	@JsonUnwrapped
	@CheckForNull
	public NetdevHubPortOptions hubport;
	
	@JsonProperty("netmap")
	@JsonUnwrapped
	@CheckForNull
	public NetdevNetmapOptions netmap;
	
	@JsonProperty("vhost-user")
	@JsonUnwrapped
	@CheckForNull
	public NetdevVhostUserOptions vhostUser;
	// }

	@Nonnull
	public static Netdev none(@Nonnull NetdevNoneOptions none) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.none;
		self.none = none;
		return self;
	}

	@Nonnull
	public static Netdev nic(@Nonnull NetLegacyNicOptions nic) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.nic;
		self.nic = nic;
		return self;
	}

	@Nonnull
	public static Netdev user(@Nonnull NetdevUserOptions user) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.user;
		self.user = user;
		return self;
	}

	@Nonnull
	public static Netdev tap(@Nonnull NetdevTapOptions tap) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.tap;
		self.tap = tap;
		return self;
	}

	@Nonnull
	public static Netdev l2tpv3(@Nonnull NetdevL2TPv3Options l2tpv3) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.l2tpv3;
		self.l2tpv3 = l2tpv3;
		return self;
	}

	@Nonnull
	public static Netdev socket(@Nonnull NetdevSocketOptions socket) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.socket;
		self.socket = socket;
		return self;
	}

	@Nonnull
	public static Netdev vde(@Nonnull NetdevVdeOptions vde) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.vde;
		self.vde = vde;
		return self;
	}

	@Nonnull
	public static Netdev bridge(@Nonnull NetdevBridgeOptions bridge) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.bridge;
		self.bridge = bridge;
		return self;
	}

	@Nonnull
	public static Netdev hubport(@Nonnull NetdevHubPortOptions hubport) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.hubport;
		self.hubport = hubport;
		return self;
	}

	@Nonnull
	public static Netdev netmap(@Nonnull NetdevNetmapOptions netmap) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.netmap;
		self.netmap = netmap;
		return self;
	}

	@Nonnull
	public static Netdev vhostUser(@Nonnull NetdevVhostUserOptions vhostUser) {
		Netdev self = new Netdev();
		self.type = NetClientDriver.vhost_user;
		self.vhostUser = vhostUser;
		return self;
	}

	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("none");
		names.add("nic");
		names.add("user");
		names.add("tap");
		names.add("l2tpv3");
		names.add("socket");
		names.add("vde");
		names.add("bridge");
		names.add("hubport");
		names.add("netmap");
		names.add("vhost-user");
		return names;
	}

	@JsonIgnore
	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("none".equals(name))
			return none;
		if ("nic".equals(name))
			return nic;
		if ("user".equals(name))
			return user;
		if ("tap".equals(name))
			return tap;
		if ("l2tpv3".equals(name))
			return l2tpv3;
		if ("socket".equals(name))
			return socket;
		if ("vde".equals(name))
			return vde;
		if ("bridge".equals(name))
			return bridge;
		if ("hubport".equals(name))
			return hubport;
		if ("netmap".equals(name))
			return netmap;
		if ("vhost-user".equals(name))
			return vhostUser;
		return super.getFieldByName(name);
	}

	@JsonIgnore
	public boolean isValidUnion() {
		int count = 0;
		if (none != null)
			count++;
		if (nic != null)
			count++;
		if (user != null)
			count++;
		if (tap != null)
			count++;
		if (l2tpv3 != null)
			count++;
		if (socket != null)
			count++;
		if (vde != null)
			count++;
		if (bridge != null)
			count++;
		if (hubport != null)
			count++;
		if (netmap != null)
			count++;
		if (vhostUser != null)
			count++;
		return (count == 1);
	}
}
