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
 * <pre>QApiStructDescriptor{name=BlockdevOptionsNfs, data={server=NFSServer, path=str, *user=int, *group=int, *tcp-syn-count=int, *readahead-size=int, *page-cache-size=int, *debug=int}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=BlockdevOptionsNfs, data={server=NFSServer, path=str, *user=int, *group=int, *tcp-syn-count=int, *readahead-size=int, *page-cache-size=int, *debug=int}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevOptionsNfs extends QApiType {

	
	@JsonProperty("server")
	@Nonnull
	public NFSServer server;
	
	@JsonProperty("path")
	@Nonnull
	public java.lang.String path;
	
	@JsonProperty("user")
	@CheckForNull
	public java.lang.Long user;
	
	@JsonProperty("group")
	@CheckForNull
	public java.lang.Long group;
	
	@JsonProperty("tcp-syn-count")
	@CheckForNull
	public java.lang.Long tcpSynCount;
	
	@JsonProperty("readahead-size")
	@CheckForNull
	public java.lang.Long readaheadSize;
	
	@JsonProperty("page-cache-size")
	@CheckForNull
	public java.lang.Long pageCacheSize;
	
	@JsonProperty("debug")
	@CheckForNull
	public java.lang.Long debug;

	@Nonnull
	public BlockdevOptionsNfs withServer(NFSServer value) {
		this.server = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsNfs withPath(java.lang.String value) {
		this.path = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsNfs withUser(java.lang.Long value) {
		this.user = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsNfs withGroup(java.lang.Long value) {
		this.group = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsNfs withTcpSynCount(java.lang.Long value) {
		this.tcpSynCount = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsNfs withReadaheadSize(java.lang.Long value) {
		this.readaheadSize = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsNfs withPageCacheSize(java.lang.Long value) {
		this.pageCacheSize = value;
		return this;
	}

	@Nonnull
	public BlockdevOptionsNfs withDebug(java.lang.Long value) {
		this.debug = value;
		return this;
	}

	public BlockdevOptionsNfs() {
	}

	public BlockdevOptionsNfs(NFSServer server, java.lang.String path, java.lang.Long user, java.lang.Long group, java.lang.Long tcpSynCount, java.lang.Long readaheadSize, java.lang.Long pageCacheSize, java.lang.Long debug) {
		this.server = server;
		this.path = path;
		this.user = user;
		this.group = group;
		this.tcpSynCount = tcpSynCount;
		this.readaheadSize = readaheadSize;
		this.pageCacheSize = pageCacheSize;
		this.debug = debug;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("server");
		names.add("path");
		names.add("user");
		names.add("group");
		names.add("tcp-syn-count");
		names.add("readahead-size");
		names.add("page-cache-size");
		names.add("debug");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("server".equals(name))
			return server;
		if ("path".equals(name))
			return path;
		if ("user".equals(name))
			return user;
		if ("group".equals(name))
			return group;
		if ("tcp-syn-count".equals(name))
			return tcpSynCount;
		if ("readahead-size".equals(name))
			return readaheadSize;
		if ("page-cache-size".equals(name))
			return pageCacheSize;
		if ("debug".equals(name))
			return debug;
		return super.getFieldByName(name);
	}
}
