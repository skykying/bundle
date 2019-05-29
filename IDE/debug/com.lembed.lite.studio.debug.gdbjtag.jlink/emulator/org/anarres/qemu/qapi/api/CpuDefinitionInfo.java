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
 * <pre>QApiStructDescriptor{name=CpuDefinitionInfo, data={name=str, *migration-safe=bool, static=bool, *unavailable-features=[str], typename=str}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=CpuDefinitionInfo, data={name=str, *migration-safe=bool, static=bool, *unavailable-features=[str], typename=str}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CpuDefinitionInfo extends QApiType {

	
	@JsonProperty("name")
	@Nonnull
	public java.lang.String name;
	
	@JsonProperty("migration-safe")
	@CheckForNull
	public java.lang.Boolean migrationSafe;
	
	@JsonProperty("static")
	@Nonnull
	public boolean _static;
	
	@JsonProperty("unavailable-features")
	@CheckForNull
	public java.util.List<java.lang.String> unavailableFeatures;
	
	@JsonProperty("typename")
	@Nonnull
	public java.lang.String typename;

	@Nonnull
	public CpuDefinitionInfo withName(java.lang.String value) {
		this.name = value;
		return this;
	}

	@Nonnull
	public CpuDefinitionInfo withMigrationSafe(java.lang.Boolean value) {
		this.migrationSafe = value;
		return this;
	}

	@Nonnull
	public CpuDefinitionInfo with_static(boolean value) {
		this._static = value;
		return this;
	}

	@Nonnull
	public CpuDefinitionInfo withUnavailableFeatures(java.util.List<java.lang.String> value) {
		this.unavailableFeatures = value;
		return this;
	}

	@Nonnull
	public CpuDefinitionInfo withTypename(java.lang.String value) {
		this.typename = value;
		return this;
	}

	public CpuDefinitionInfo() {
	}

	public CpuDefinitionInfo(java.lang.String name, java.lang.Boolean migrationSafe, boolean _static, java.util.List<java.lang.String> unavailableFeatures, java.lang.String typename) {
		this.name = name;
		this.migrationSafe = migrationSafe;
		this._static = _static;
		this.unavailableFeatures = unavailableFeatures;
		this.typename = typename;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("name");
		names.add("migration-safe");
		names.add("static");
		names.add("unavailable-features");
		names.add("typename");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("name".equals(name))
			return name;
		if ("migration-safe".equals(name))
			return migrationSafe;
		if ("static".equals(name))
			return _static;
		if ("unavailable-features".equals(name))
			return unavailableFeatures;
		if ("typename".equals(name))
			return typename;
		return super.getFieldByName(name);
	}
}
