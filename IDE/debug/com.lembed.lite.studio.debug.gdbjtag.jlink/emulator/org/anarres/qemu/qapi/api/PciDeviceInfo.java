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
 * <pre>QApiStructDescriptor{name=PciDeviceInfo, data={bus=int, slot=int, function=int, class_info=PciDeviceClass, id=PciDeviceId, *irq=int, qdev_id=str, *pci_bridge=PciBridgeInfo, regions=[PciMemoryRegion]}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=PciDeviceInfo, data={bus=int, slot=int, function=int, class_info=PciDeviceClass, id=PciDeviceId, *irq=int, qdev_id=str, *pci_bridge=PciBridgeInfo, regions=[PciMemoryRegion]}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PciDeviceInfo extends QApiType {

	
	@JsonProperty("bus")
	@Nonnull
	public long bus;
	
	@JsonProperty("slot")
	@Nonnull
	public long slot;
	
	@JsonProperty("function")
	@Nonnull
	public long function;
	
	@JsonProperty("class_info")
	@Nonnull
	public PciDeviceClass classInfo;
	
	@JsonProperty("id")
	@Nonnull
	public PciDeviceId id;
	
	@JsonProperty("irq")
	@CheckForNull
	public java.lang.Long irq;
	
	@JsonProperty("qdev_id")
	@Nonnull
	public java.lang.String qdevId;
	
	@JsonProperty("pci_bridge")
	@CheckForNull
	public PciBridgeInfo pciBridge;
	
	@JsonProperty("regions")
	@Nonnull
	public java.util.List<PciMemoryRegion> regions;

	@Nonnull
	public PciDeviceInfo withBus(long value) {
		this.bus = value;
		return this;
	}

	@Nonnull
	public PciDeviceInfo withSlot(long value) {
		this.slot = value;
		return this;
	}

	@Nonnull
	public PciDeviceInfo withFunction(long value) {
		this.function = value;
		return this;
	}

	@Nonnull
	public PciDeviceInfo withClassInfo(PciDeviceClass value) {
		this.classInfo = value;
		return this;
	}

	@Nonnull
	public PciDeviceInfo withId(PciDeviceId value) {
		this.id = value;
		return this;
	}

	@Nonnull
	public PciDeviceInfo withIrq(java.lang.Long value) {
		this.irq = value;
		return this;
	}

	@Nonnull
	public PciDeviceInfo withQdevId(java.lang.String value) {
		this.qdevId = value;
		return this;
	}

	@Nonnull
	public PciDeviceInfo withPciBridge(PciBridgeInfo value) {
		this.pciBridge = value;
		return this;
	}

	@Nonnull
	public PciDeviceInfo withRegions(java.util.List<PciMemoryRegion> value) {
		this.regions = value;
		return this;
	}

	public PciDeviceInfo() {
	}

	public PciDeviceInfo(long bus, long slot, long function, PciDeviceClass classInfo, PciDeviceId id, java.lang.Long irq, java.lang.String qdevId, PciBridgeInfo pciBridge, java.util.List<PciMemoryRegion> regions) {
		this.bus = bus;
		this.slot = slot;
		this.function = function;
		this.classInfo = classInfo;
		this.id = id;
		this.irq = irq;
		this.qdevId = qdevId;
		this.pciBridge = pciBridge;
		this.regions = regions;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("bus");
		names.add("slot");
		names.add("function");
		names.add("class_info");
		names.add("id");
		names.add("irq");
		names.add("qdev_id");
		names.add("pci_bridge");
		names.add("regions");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("bus".equals(name))
			return bus;
		if ("slot".equals(name))
			return slot;
		if ("function".equals(name))
			return function;
		if ("class_info".equals(name))
			return classInfo;
		if ("id".equals(name))
			return id;
		if ("irq".equals(name))
			return irq;
		if ("qdev_id".equals(name))
			return qdevId;
		if ("pci_bridge".equals(name))
			return pciBridge;
		if ("regions".equals(name))
			return regions;
		return super.getFieldByName(name);
	}
}