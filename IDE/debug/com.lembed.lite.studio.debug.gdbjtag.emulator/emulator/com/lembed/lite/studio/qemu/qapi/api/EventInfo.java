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
 * <pre>QApiStructDescriptor{name=EventInfo, data={name=str}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=EventInfo, data={name=str}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EventInfo extends QApiType {

	
	@JsonProperty("name")
	@Nonnull
	public java.lang.String name;

	@Nonnull
	public EventInfo withName(java.lang.String value) {
		this.name = value;
		return this;
	}

	public EventInfo() {
	}

	public EventInfo(java.lang.String name) {
		this.name = name;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("name");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("name".equals(name))
			return name;
		return super.getFieldByName(name);
	}
}
