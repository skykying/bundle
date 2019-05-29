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
 * <pre>QApiStructDescriptor{name=InetSocketAddress, data={*numeric=bool, *to=uint16, *ipv4=bool, *ipv6=bool}, innerTypes=null, fields=null, base=InetSocketAddressBase}</pre>
 */
// QApiStructDescriptor{name=InetSocketAddress, data={*numeric=bool, *to=uint16, *ipv4=bool, *ipv6=bool}, innerTypes=null, fields=null, base=InetSocketAddressBase}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InetSocketAddress extends InetSocketAddressBase {

	
	@JsonProperty("numeric")
	@CheckForNull
	public java.lang.Boolean numeric;
	
	@JsonProperty("to")
	@CheckForNull
	public java.lang.Integer to;
	
	@JsonProperty("ipv4")
	@CheckForNull
	public java.lang.Boolean ipv4;
	
	@JsonProperty("ipv6")
	@CheckForNull
	public java.lang.Boolean ipv6;

	@Nonnull
	public InetSocketAddress withNumeric(java.lang.Boolean value) {
		this.numeric = value;
		return this;
	}

	@Nonnull
	public InetSocketAddress withTo(java.lang.Integer value) {
		this.to = value;
		return this;
	}

	@Nonnull
	public InetSocketAddress withIpv4(java.lang.Boolean value) {
		this.ipv4 = value;
		return this;
	}

	@Nonnull
	public InetSocketAddress withIpv6(java.lang.Boolean value) {
		this.ipv6 = value;
		return this;
	}

	public InetSocketAddress() {
	}

	public InetSocketAddress(java.lang.Boolean numeric, java.lang.Integer to, java.lang.Boolean ipv4, java.lang.Boolean ipv6) {
		this.numeric = numeric;
		this.to = to;
		this.ipv4 = ipv4;
		this.ipv6 = ipv6;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("numeric");
		names.add("to");
		names.add("ipv4");
		names.add("ipv6");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("numeric".equals(name))
			return numeric;
		if ("to".equals(name))
			return to;
		if ("ipv4".equals(name))
			return ipv4;
		if ("ipv6".equals(name))
			return ipv6;
		return super.getFieldByName(name);
	}
}
