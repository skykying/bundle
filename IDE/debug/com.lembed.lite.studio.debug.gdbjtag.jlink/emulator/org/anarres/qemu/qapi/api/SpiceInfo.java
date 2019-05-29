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
 * <pre>QApiStructDescriptor{name=SpiceInfo, data={enabled=bool, migrated=bool, *host=str, *port=int, *tls-port=int, *auth=str, *compiled-version=str, mouse-mode=SpiceQueryMouseMode, *channels=[SpiceChannel]}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=SpiceInfo, data={enabled=bool, migrated=bool, *host=str, *port=int, *tls-port=int, *auth=str, *compiled-version=str, mouse-mode=SpiceQueryMouseMode, *channels=[SpiceChannel]}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SpiceInfo extends QApiType {

	
	@JsonProperty("enabled")
	@Nonnull
	public boolean enabled;
	
	@JsonProperty("migrated")
	@Nonnull
	public boolean migrated;
	
	@JsonProperty("host")
	@CheckForNull
	public java.lang.String host;
	
	@JsonProperty("port")
	@CheckForNull
	public java.lang.Long port;
	
	@JsonProperty("tls-port")
	@CheckForNull
	public java.lang.Long tlsPort;
	
	@JsonProperty("auth")
	@CheckForNull
	public java.lang.String auth;
	
	@JsonProperty("compiled-version")
	@CheckForNull
	public java.lang.String compiledVersion;
	
	@JsonProperty("mouse-mode")
	@Nonnull
	public SpiceQueryMouseMode mouseMode;
	
	@JsonProperty("channels")
	@CheckForNull
	public java.util.List<SpiceChannel> channels;

	@Nonnull
	public SpiceInfo withEnabled(boolean value) {
		this.enabled = value;
		return this;
	}

	@Nonnull
	public SpiceInfo withMigrated(boolean value) {
		this.migrated = value;
		return this;
	}

	@Nonnull
	public SpiceInfo withHost(java.lang.String value) {
		this.host = value;
		return this;
	}

	@Nonnull
	public SpiceInfo withPort(java.lang.Long value) {
		this.port = value;
		return this;
	}

	@Nonnull
	public SpiceInfo withTlsPort(java.lang.Long value) {
		this.tlsPort = value;
		return this;
	}

	@Nonnull
	public SpiceInfo withAuth(java.lang.String value) {
		this.auth = value;
		return this;
	}

	@Nonnull
	public SpiceInfo withCompiledVersion(java.lang.String value) {
		this.compiledVersion = value;
		return this;
	}

	@Nonnull
	public SpiceInfo withMouseMode(SpiceQueryMouseMode value) {
		this.mouseMode = value;
		return this;
	}

	@Nonnull
	public SpiceInfo withChannels(java.util.List<SpiceChannel> value) {
		this.channels = value;
		return this;
	}

	public SpiceInfo() {
	}

	public SpiceInfo(boolean enabled, boolean migrated, java.lang.String host, java.lang.Long port, java.lang.Long tlsPort, java.lang.String auth, java.lang.String compiledVersion, SpiceQueryMouseMode mouseMode, java.util.List<SpiceChannel> channels) {
		this.enabled = enabled;
		this.migrated = migrated;
		this.host = host;
		this.port = port;
		this.tlsPort = tlsPort;
		this.auth = auth;
		this.compiledVersion = compiledVersion;
		this.mouseMode = mouseMode;
		this.channels = channels;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("enabled");
		names.add("migrated");
		names.add("host");
		names.add("port");
		names.add("tls-port");
		names.add("auth");
		names.add("compiled-version");
		names.add("mouse-mode");
		names.add("channels");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("enabled".equals(name))
			return enabled;
		if ("migrated".equals(name))
			return migrated;
		if ("host".equals(name))
			return host;
		if ("port".equals(name))
			return port;
		if ("tls-port".equals(name))
			return tlsPort;
		if ("auth".equals(name))
			return auth;
		if ("compiled-version".equals(name))
			return compiledVersion;
		if ("mouse-mode".equals(name))
			return mouseMode;
		if ("channels".equals(name))
			return channels;
		return super.getFieldByName(name);
	}
}
