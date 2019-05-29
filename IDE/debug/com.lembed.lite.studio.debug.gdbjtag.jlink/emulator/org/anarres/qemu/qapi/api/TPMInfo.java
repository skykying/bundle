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
 * <pre>QApiStructDescriptor{name=TPMInfo, data={id=str, model=TpmModel, options=TpmTypeOptions}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=TPMInfo, data={id=str, model=TpmModel, options=TpmTypeOptions}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TPMInfo extends QApiType {

	
	@JsonProperty("id")
	@Nonnull
	public java.lang.String id;
	
	@JsonProperty("model")
	@Nonnull
	public TpmModel model;
	
	@JsonProperty("options")
	@Nonnull
	public TpmTypeOptions options;

	@Nonnull
	public TPMInfo withId(java.lang.String value) {
		this.id = value;
		return this;
	}

	@Nonnull
	public TPMInfo withModel(TpmModel value) {
		this.model = value;
		return this;
	}

	@Nonnull
	public TPMInfo withOptions(TpmTypeOptions value) {
		this.options = value;
		return this;
	}

	public TPMInfo() {
	}

	public TPMInfo(java.lang.String id, TpmModel model, TpmTypeOptions options) {
		this.id = id;
		this.model = model;
		this.options = options;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("id");
		names.add("model");
		names.add("options");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("id".equals(name))
			return id;
		if ("model".equals(name))
			return model;
		if ("options".equals(name))
			return options;
		return super.getFieldByName(name);
	}
}