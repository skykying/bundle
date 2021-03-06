package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiUnionDescriptor{name=ImageInfoSpecific, discriminator=null, data={qcow2=ImageInfoSpecificQCow2, vmdk=ImageInfoSpecificVmdk, luks=QCryptoBlockInfoLUKS}, innerTypes=null, fields=null, base=null, discriminatorField=null}</pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImageInfoSpecific extends QApiType implements QApiUnion {
	public static enum Discriminator {
		qcow2,
		vmdk,
		luks,
		__NONE;
	}

	
	@Nonnull
	@JsonProperty("type")
	public Discriminator type;

	@Nonnull
	public final Discriminator getType() {
		return type;
	}

	// union {
	
	@JsonProperty("qcow2")
	@JsonUnwrapped
	@CheckForNull
	public ImageInfoSpecificQCow2 qcow2;
	
	@JsonProperty("vmdk")
	@JsonUnwrapped
	@CheckForNull
	public ImageInfoSpecificVmdk vmdk;
	
	@JsonProperty("luks")
	@JsonUnwrapped
	@CheckForNull
	public QCryptoBlockInfoLUKS luks;
	// }

	@Nonnull
	public static ImageInfoSpecific qcow2(@Nonnull ImageInfoSpecificQCow2 qcow2) {
		ImageInfoSpecific self = new ImageInfoSpecific();
		self.type = Discriminator.qcow2;
		self.qcow2 = qcow2;
		return self;
	}

	@Nonnull
	public static ImageInfoSpecific vmdk(@Nonnull ImageInfoSpecificVmdk vmdk) {
		ImageInfoSpecific self = new ImageInfoSpecific();
		self.type = Discriminator.vmdk;
		self.vmdk = vmdk;
		return self;
	}

	@Nonnull
	public static ImageInfoSpecific luks(@Nonnull QCryptoBlockInfoLUKS luks) {
		ImageInfoSpecific self = new ImageInfoSpecific();
		self.type = Discriminator.luks;
		self.luks = luks;
		return self;
	}

	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("qcow2");
		names.add("vmdk");
		names.add("luks");
		return names;
	}

	@JsonIgnore
	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("qcow2".equals(name))
			return qcow2;
		if ("vmdk".equals(name))
			return vmdk;
		if ("luks".equals(name))
			return luks;
		return super.getFieldByName(name);
	}

	@JsonIgnore
	public boolean isValidUnion() {
		int count = 0;
		if (qcow2 != null)
			count++;
		if (vmdk != null)
			count++;
		if (luks != null)
			count++;
		return (count == 1);
	}
}
