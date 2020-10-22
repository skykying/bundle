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
 * <pre>QApiStructDescriptor{name=BlockdevOptionsRbd, data={pool=str, image=str, *conf=str, *snapshot=str, *user=str, *server=[InetSocketAddressBase]}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockdevOptionsRbd, data={pool=str, image=str, *conf=str, *snapshot=str, *user=str, *server=[InetSocketAddressBase]}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevOptionsRbd extends QApiType {

	
	@JsonProperty("pool")
	@Nonnull
	public java.lang.String pool;
	
	@JsonProperty("image")
	@Nonnull
	public java.lang.String image;
	
	@JsonProperty("conf")
	@CheckForNull
	public java.lang.String conf;
	
	@JsonProperty("snapshot")
	@CheckForNull
	public java.lang.String snapshot;
	
	@JsonProperty("user")
	@CheckForNull
	public java.lang.String user;
	
	@JsonProperty("server")
	@CheckForNull
	public java.util.List<InetSocketAddressBase> server;

	@Nonnull
	public BlockdevOptionsRbd withPool(java.lang.String value) {
		this.pool = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsRbd withImage(java.lang.String value) {
		this.image = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsRbd withConf(java.lang.String value) {
		this.conf = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsRbd withSnapshot(java.lang.String value) {
		this.snapshot = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsRbd withUser(java.lang.String value) {
		this.user = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsRbd withServer(java.util.List<InetSocketAddressBase> value) {
		this.server = value;
		return this;
	}

	public BlockdevOptionsRbd() {
	}

	public BlockdevOptionsRbd(java.lang.String pool, java.lang.String image, java.lang.String conf, java.lang.String snapshot, java.lang.String user, java.util.List<InetSocketAddressBase> server) {
		this.pool = pool;
		this.image = image;
		this.conf = conf;
		this.snapshot = snapshot;
		this.user = user;
		this.server = server;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("pool");
		names.add("image");
		names.add("conf");
		names.add("snapshot");
		names.add("user");
		names.add("server");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("pool".equals(name))
			return pool;
		if ("image".equals(name))
			return image;
		if ("conf".equals(name))
			return conf;
		if ("snapshot".equals(name))
			return snapshot;
		if ("user".equals(name))
			return user;
		if ("server".equals(name))
			return server;
		return super.getFieldByName(name);
	}
}