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
 * <pre>QApiStructDescriptor{name=NetdevL2TPv3Options, data={src=str, dst=str, *srcport=str, *dstport=str, *ipv6=bool, *udp=bool, *cookie64=bool, *counter=bool, *pincounter=bool, *txcookie=uint64, *rxcookie=uint64, txsession=uint32, *rxsession=uint32, *offset=uint32}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=NetdevL2TPv3Options, data={src=str, dst=str, *srcport=str, *dstport=str, *ipv6=bool, *udp=bool, *cookie64=bool, *counter=bool, *pincounter=bool, *txcookie=uint64, *rxcookie=uint64, txsession=uint32, *rxsession=uint32, *offset=uint32}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NetdevL2TPv3Options extends QApiType {

	
	@JsonProperty("src")
	@Nonnull
	public java.lang.String src;
	
	@JsonProperty("dst")
	@Nonnull
	public java.lang.String dst;
	
	@JsonProperty("srcport")
	@CheckForNull
	public java.lang.String srcport;
	
	@JsonProperty("dstport")
	@CheckForNull
	public java.lang.String dstport;
	
	@JsonProperty("ipv6")
	@CheckForNull
	public java.lang.Boolean ipv6;
	
	@JsonProperty("udp")
	@CheckForNull
	public java.lang.Boolean udp;
	
	@JsonProperty("cookie64")
	@CheckForNull
	public java.lang.Boolean cookie64;
	
	@JsonProperty("counter")
	@CheckForNull
	public java.lang.Boolean counter;
	
	@JsonProperty("pincounter")
	@CheckForNull
	public java.lang.Boolean pincounter;
	
	@JsonProperty("txcookie")
	@CheckForNull
	public java.lang.Long txcookie;
	
	@JsonProperty("rxcookie")
	@CheckForNull
	public java.lang.Long rxcookie;
	
	@JsonProperty("txsession")
	@Nonnull
	public long txsession;
	
	@JsonProperty("rxsession")
	@CheckForNull
	public java.lang.Long rxsession;
	
	@JsonProperty("offset")
	@CheckForNull
	public java.lang.Long offset;

	@Nonnull
	public NetdevL2TPv3Options withSrc(java.lang.String value) {
		this.src = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withDst(java.lang.String value) {
		this.dst = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withSrcport(java.lang.String value) {
		this.srcport = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withDstport(java.lang.String value) {
		this.dstport = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withIpv6(java.lang.Boolean value) {
		this.ipv6 = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withUdp(java.lang.Boolean value) {
		this.udp = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withCookie64(java.lang.Boolean value) {
		this.cookie64 = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withCounter(java.lang.Boolean value) {
		this.counter = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withPincounter(java.lang.Boolean value) {
		this.pincounter = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withTxcookie(java.lang.Long value) {
		this.txcookie = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withRxcookie(java.lang.Long value) {
		this.rxcookie = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withTxsession(long value) {
		this.txsession = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withRxsession(java.lang.Long value) {
		this.rxsession = value;
		return this;
	}

	@Nonnull
	public NetdevL2TPv3Options withOffset(java.lang.Long value) {
		this.offset = value;
		return this;
	}

	public NetdevL2TPv3Options() {
	}

	public NetdevL2TPv3Options(java.lang.String src, java.lang.String dst, java.lang.String srcport, java.lang.String dstport, java.lang.Boolean ipv6, java.lang.Boolean udp, java.lang.Boolean cookie64, java.lang.Boolean counter, java.lang.Boolean pincounter, java.lang.Long txcookie, java.lang.Long rxcookie, long txsession, java.lang.Long rxsession, java.lang.Long offset) {
		this.src = src;
		this.dst = dst;
		this.srcport = srcport;
		this.dstport = dstport;
		this.ipv6 = ipv6;
		this.udp = udp;
		this.cookie64 = cookie64;
		this.counter = counter;
		this.pincounter = pincounter;
		this.txcookie = txcookie;
		this.rxcookie = rxcookie;
		this.txsession = txsession;
		this.rxsession = rxsession;
		this.offset = offset;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("src");
		names.add("dst");
		names.add("srcport");
		names.add("dstport");
		names.add("ipv6");
		names.add("udp");
		names.add("cookie64");
		names.add("counter");
		names.add("pincounter");
		names.add("txcookie");
		names.add("rxcookie");
		names.add("txsession");
		names.add("rxsession");
		names.add("offset");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("src".equals(name))
			return src;
		if ("dst".equals(name))
			return dst;
		if ("srcport".equals(name))
			return srcport;
		if ("dstport".equals(name))
			return dstport;
		if ("ipv6".equals(name))
			return ipv6;
		if ("udp".equals(name))
			return udp;
		if ("cookie64".equals(name))
			return cookie64;
		if ("counter".equals(name))
			return counter;
		if ("pincounter".equals(name))
			return pincounter;
		if ("txcookie".equals(name))
			return txcookie;
		if ("rxcookie".equals(name))
			return rxcookie;
		if ("txsession".equals(name))
			return txsession;
		if ("rxsession".equals(name))
			return rxsession;
		if ("offset".equals(name))
			return offset;
		return super.getFieldByName(name);
	}
}
