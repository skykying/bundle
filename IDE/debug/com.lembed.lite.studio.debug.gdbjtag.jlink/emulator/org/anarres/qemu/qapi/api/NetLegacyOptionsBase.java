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
 * <pre>QApiTypeDescriptor{name=NetLegacyOptionsBase, data={type=NetLegacyOptionsType}, innerTypes=null}</pre>
 */
// QApiTypeDescriptor{name=NetLegacyOptionsBase, data={type=NetLegacyOptionsType}, innerTypes=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NetLegacyOptionsBase extends QApiType {

	
	@JsonProperty("type")
	@Nonnull
	public NetLegacyOptionsType type;

	@Nonnull
	public NetLegacyOptionsBase withType(NetLegacyOptionsType value) {
		this.type = value;
		return this;
	}

	public NetLegacyOptionsBase() {
	}

	public NetLegacyOptionsBase(NetLegacyOptionsType type) {
		this.type = type;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("type");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("type".equals(name))
			return type;
		return super.getFieldByName(name);
	}
}
