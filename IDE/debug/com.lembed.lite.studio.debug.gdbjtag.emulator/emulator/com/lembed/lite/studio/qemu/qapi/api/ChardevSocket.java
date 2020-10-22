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
 * <pre>QApiStructDescriptor{name=ChardevSocket, data={addr=SocketAddressLegacy, *tls-creds=str, *server=bool, *wait=bool, *nodelay=bool, *telnet=bool, *tn3270=bool, *reconnect=int}, innerTypes=null, fields=null, base=ChardevCommon}</pre>
 */
// QApiStructDescriptor{name=ChardevSocket, data={addr=SocketAddressLegacy, *tls-creds=str, *server=bool, *wait=bool, *nodelay=bool, *telnet=bool, *tn3270=bool, *reconnect=int}, innerTypes=null, fields=null, base=ChardevCommon}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChardevSocket extends ChardevCommon {

	
	@JsonProperty("addr")
	@Nonnull
	public SocketAddressLegacy addr;
	
	@JsonProperty("tls-creds")
	@CheckForNull
	public java.lang.String tlsCreds;
	
	@JsonProperty("server")
	@CheckForNull
	public java.lang.Boolean server;
	
	@JsonProperty("wait")
	@CheckForNull
	public java.lang.Boolean wait;
	
	@JsonProperty("nodelay")
	@CheckForNull
	public java.lang.Boolean nodelay;
	
	@JsonProperty("telnet")
	@CheckForNull
	public java.lang.Boolean telnet;
	
	@JsonProperty("tn3270")
	@CheckForNull
	public java.lang.Boolean tn3270;
	
	@JsonProperty("reconnect")
	@CheckForNull
	public java.lang.Long reconnect;

	@Nonnull
	public ChardevSocket withAddr(SocketAddressLegacy value) {
		this.addr = value;
		return this;
	}

	@Nonnull
	public ChardevSocket withTlsCreds(java.lang.String value) {
		this.tlsCreds = value;
		return this;
	}

	@Nonnull
	public ChardevSocket withServer(java.lang.Boolean value) {
		this.server = value;
		return this;
	}

	@Nonnull
	public ChardevSocket withWait(java.lang.Boolean value) {
		this.wait = value;
		return this;
	}

	@Nonnull
	public ChardevSocket withNodelay(java.lang.Boolean value) {
		this.nodelay = value;
		return this;
	}

	@Nonnull
	public ChardevSocket withTelnet(java.lang.Boolean value) {
		this.telnet = value;
		return this;
	}

	@Nonnull
	public ChardevSocket withTn3270(java.lang.Boolean value) {
		this.tn3270 = value;
		return this;
	}

	@Nonnull
	public ChardevSocket withReconnect(java.lang.Long value) {
		this.reconnect = value;
		return this;
	}

	public ChardevSocket() {
	}

	public ChardevSocket(SocketAddressLegacy addr, java.lang.String tlsCreds, java.lang.Boolean server, java.lang.Boolean wait, java.lang.Boolean nodelay, java.lang.Boolean telnet, java.lang.Boolean tn3270, java.lang.Long reconnect) {
		this.addr = addr;
		this.tlsCreds = tlsCreds;
		this.server = server;
		this.wait = wait;
		this.nodelay = nodelay;
		this.telnet = telnet;
		this.tn3270 = tn3270;
		this.reconnect = reconnect;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("addr");
		names.add("tls-creds");
		names.add("server");
		names.add("wait");
		names.add("nodelay");
		names.add("telnet");
		names.add("tn3270");
		names.add("reconnect");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("addr".equals(name))
			return addr;
		if ("tls-creds".equals(name))
			return tlsCreds;
		if ("server".equals(name))
			return server;
		if ("wait".equals(name))
			return wait;
		if ("nodelay".equals(name))
			return nodelay;
		if ("telnet".equals(name))
			return telnet;
		if ("tn3270".equals(name))
			return tn3270;
		if ("reconnect".equals(name))
			return reconnect;
		return super.getFieldByName(name);
	}
}