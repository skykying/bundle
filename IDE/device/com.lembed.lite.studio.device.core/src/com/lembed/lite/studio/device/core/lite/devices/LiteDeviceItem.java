/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.core.lite.devices;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.DeviceVendor;
import com.lembed.lite.studio.device.core.data.CpItem;
import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPack.PackState;
import com.lembed.lite.studio.device.core.enums.EDeviceHierarchyLevel;
import com.lembed.lite.studio.device.generic.IAttributes;
import com.lembed.lite.studio.device.item.CmsisMapItem;
import com.lembed.lite.studio.device.utils.AlnumComparator;
import com.lembed.lite.studio.device.utils.VersionComparator;

/**
 * Default implementation of IRteDeviceItem
 */
public class LiteDeviceItem extends CmsisMapItem<ILiteDeviceItem> implements ILiteDeviceItem {

	protected int fLevel = EDeviceHierarchyLevel.NONE.ordinal();
	protected Map<String, ICpDeviceItem> fDevices = null;
	protected Set<String> fDeviceNames = null;

	/**
	 *
	 */
	public LiteDeviceItem() {
		fLevel = EDeviceHierarchyLevel.ROOT.ordinal();
		fName = "All Devices"; //$NON-NLS-1$
	}

	/**
	 * @param parent
	 */
	public LiteDeviceItem(String name, int level, ILiteDeviceItem parent) {
		super(parent);
		fLevel = level;
		fName= name;
	}


	@Override
	protected Map<String, ILiteDeviceItem> createMap() {
		// create TreeMap with Alpha-Numeric case-insensitive ascending sorting
		return new TreeMap<String, ILiteDeviceItem>(new AlnumComparator(false, false));
	}

	/**
	 * Creates device tree from list of Packs
	 * @param packs collection of packs to use
	 * @return device tree as root IRteDeviceItem
	 */
	public static ILiteDeviceItem createTree(Collection<ICpPack> packs){
		ILiteDeviceItem root = new LiteDeviceItem();
		if(packs == null || packs.isEmpty()) {
			return root;
		}
		for(ICpPack pack : packs) {
			root.addDevices(pack);
		}
		return root;
	}

	@Override
	public int getLevel() {
		return fLevel;
	}

	@Override
	public Collection<ICpDeviceItem> getDevices() {
		if(fDevices != null) {
			return fDevices.values();
		}
		return null;
	}

	@Override
	public ICpDeviceItem getDevice() {
		if(fDevices != null && ! fDevices.isEmpty()) {
			// Return the latest INSTALLED pack's device
			ICpDeviceItem firstDevice = null;
			for (ICpDeviceItem device : fDevices.values()) {
				if (device.getPack().getPackState() == PackState.INSTALLED) {
					return device;
				}
				if (firstDevice == null)
					firstDevice = device;
			}
			// Otherwise return the latest pack's device
			return firstDevice;
		}
		return null;
	}


	@Override
	public String getProcessorName() {
		int i = getName().indexOf(':');
		if (i >= 0) {
			return getName().substring(i + 1);
		}
		return CmsisConstants.EMPTY_STRING;
	}

	@Override
	public ICpItem getEffectiveProperties() {
		ICpDeviceItem device = getDevice();
		if(device != null){
			String processorName = getProcessorName();
			return device.getEffectiveProperties(processorName);
		}
		return null;
	}


	@Override
	public boolean isDevice() {
		if(getLevel() < EDeviceHierarchyLevel.DEVICE.ordinal()) {
			return false;
		}
		if(hasChildren()) {
			return false;
		}
		return getDevice() != null;
	}

	@Override
	public void addDevice(ICpDeviceItem item) {
		if(item == null) {
			return;
		}

		EDeviceHierarchyLevel eLevel = item.getLevel();
		int level = eLevel.ordinal();

		if(fLevel == level || fLevel == EDeviceHierarchyLevel.PROCESSOR.ordinal()) {
			ICpPack pack = item.getPack();
			String packId = pack.getId();
			if(fDevices == null) {
				fDevices = new TreeMap<String, ICpDeviceItem>(new VersionComparator());
			}

			ICpDeviceItem device = fDevices.get(packId);
			if(device == null ||
					// new item's pack is installed/downloaded and the one in the tree is not
					(item.getPack().getPackState().ordinal() < device.getPack().getPackState().ordinal())) {
				fDevices.put(packId, item);
			}
			if(fLevel == EDeviceHierarchyLevel.PROCESSOR.ordinal()) {
				return;
			}
			Collection<ICpDeviceItem> subItems = item.getDeviceItems();
			if(subItems != null && !subItems.isEmpty()) {
				for(ICpDeviceItem i : subItems ){
					addDevice(i);
				}
			} else if(level >= EDeviceHierarchyLevel.DEVICE.ordinal()) {
				addDeviceName(getName()); 
				if( item.getProcessorCount() > 1) {
					// add processor leaves
					Map<String, ICpItem> processors = item.getProcessors();
					for(Entry<String, ICpItem> e : processors.entrySet()) {
						String fullName = item.getName() + ":" + e.getKey(); //$NON-NLS-1$
						addDeviceItem(item, fullName, EDeviceHierarchyLevel.PROCESSOR.ordinal());
					}
				}
			}
			return;
		} else if(fLevel == EDeviceHierarchyLevel.ROOT.ordinal()) {
			String vendorName = DeviceVendor.getOfficialVendorName(item.getVendor());
			addDeviceItem(item, vendorName, EDeviceHierarchyLevel.VENDOR.ordinal());
			return;
		} else if(fLevel > level) {// should not happen if algorithm is correct
			return;
		}
		// other cases
		addDeviceItem(item, item.getName(), level);
	}

	protected void addDeviceItem(ICpDeviceItem item, final String itemName, final int level) {
		String fullName = itemName;
		if(level >= EDeviceHierarchyLevel.DEVICE.ordinal()){
			Map<String, ICpItem> processors = item.getProcessors();
			if(processors.size() == 1) {
				Entry<String, ICpItem> e = processors.entrySet().iterator().next();
				String procName = e.getKey();
				if(procName != null && ! procName.isEmpty()) {
					fullName += ':' + procName;
				}
			}
		}

		ILiteDeviceItem di = getChild(fullName);
		if(di == null ) {
			di = new LiteDeviceItem(fullName, level, this);
			addChild(di);
		}
		di.addDevice(item);
	}

	@Override
	public void addDevices(ICpPack pack) {
		if(pack == null) {
			return;
		}
		Collection<? extends ICpItem> devices = pack.getGrandChildren(CmsisConstants.DEVICES_TAG);
		if(devices == null) {
			return;
		}
		for(ICpItem item : devices) {
			if(!(item instanceof ICpDeviceItem)) {
				continue;
			}
			ICpDeviceItem deviceItem = (ICpDeviceItem)item;
			addDevice(deviceItem);
		}
	}

	@Override
	public void removeDevice(ICpDeviceItem item) {
		if (item == null) {
			return;
		}

		EDeviceHierarchyLevel eLevel = item.getLevel();
		int level = eLevel.ordinal();

		if(fLevel == level || fLevel == EDeviceHierarchyLevel.PROCESSOR.ordinal()) {
			ICpPack pack = item.getPack();
			String packId = pack.getId();
			if(fDevices == null) {
				return;
			}

			fDevices.remove(packId);

			if(fLevel == EDeviceHierarchyLevel.PROCESSOR.ordinal()) {
				getParent().removeChild(this);
				return;
			}
			Collection<ICpDeviceItem> subItems = item.getDeviceItems();
			if(subItems != null && !subItems.isEmpty()) {
				for(ICpDeviceItem subItem : subItems ){
					removeDevice(subItem);
				}
			} else if(level >= EDeviceHierarchyLevel.DEVICE.ordinal() && item.getProcessorCount() > 1) {
				// add processor leaves
				Map<String, ICpItem> processors = item.getProcessors();
				for(Entry<String, ICpItem> e : processors.entrySet()) {
					String procName = item.getName() + ":" + e.getKey(); //$NON-NLS-1$
					removeDeviceItem(item, procName, EDeviceHierarchyLevel.PROCESSOR.ordinal());
				}
			}
			if (fDevices.size() == 0) {
				removeDeviceName(fName);
				getParent().removeChild(this);
			}
			return;
		} else if(fLevel == EDeviceHierarchyLevel.ROOT.ordinal()) {
			ILiteDeviceItem d = findItem(item.getName(), item.getVendor(), false);
			if (d != null) {
				d.removeDevice(item);
				ILiteDeviceItem p = d.getParent();
				while (p != null && p.getLevel() > EDeviceHierarchyLevel.ROOT.ordinal()) {
					if (p.getChildren() == null ||
							p.getChildren().isEmpty()) {
						ILiteDeviceItem pp = p.getParent();
						pp.removeChild(p);
						p = pp;
					} else {
						break;
					}
				}
			}
			return;
		} else if(fLevel > level) {// should not happen if algorithm is correct
			return;
		}

		removeDeviceItem(item, item.getName(), level);
	}

	protected void removeDeviceItem(ICpDeviceItem item, String itemName, int level) {
		ILiteDeviceItem di = getChild(itemName);
		if (di != null) {
			di.removeDevice(item);
		}
	}

	@Override
	public void removeDevices(ICpPack pack) {
		if(pack == null) {
			return;
		}
		Collection<? extends ICpItem> devices = pack.getGrandChildren(CmsisConstants.DEVICES_TAG);
		if(devices != null) {
			for(ICpItem item : devices) {
				if(!(item instanceof ICpDeviceItem)) {
					continue;
				}
				ICpDeviceItem deviceItem = (ICpDeviceItem)item;
				removeDevice(deviceItem);
			}
		}
	}

	@Override
	public ILiteDeviceItem findItem(final String deviceName, final String vendor, final boolean onlyDevice) {
		if(fLevel == EDeviceHierarchyLevel.ROOT.ordinal() && vendor != null && !vendor.isEmpty()) {
			String vendorName = DeviceVendor.getOfficialVendorName(vendor);
			ILiteDeviceItem dti = getChild(vendorName);
			if(dti != null) {
				return dti.findItem(deviceName, vendorName, onlyDevice);
			}
		} else {
			// check if device item can be found directly on this level
			ILiteDeviceItem dti = getChild(deviceName);
			if(dti != null) {
				if (!onlyDevice) {
					if (deviceName.contains("*")) { //$NON-NLS-1$
						// TODO: find a better criterion in this case
						return dti.getParent();
					}
					return dti;
				} else if (dti.getLevel() > EDeviceHierarchyLevel.SUBFAMILY.ordinal()) {
					return dti;
				}
			}
			// search in children
			Collection<? extends ILiteDeviceItem> children = getChildren();
			if(children == null) {
				return null;
			}
			for(ILiteDeviceItem child : children){
				dti = child.findItem(deviceName, vendor, onlyDevice);
				if(dti != null) {
					if (!onlyDevice) {
						return dti;
					} else if (dti.getLevel() > EDeviceHierarchyLevel.SUBFAMILY.ordinal()) {
						return dti;
					}
				}
			}
		}
		return null;
	}


	@Override
	public ILiteDeviceItem findItem(final IAttributes attributes) {
		String deviceName = CpItem.getDeviceName(attributes);
		if(deviceName == null || deviceName.isEmpty()) {
			return null;
		}
		String vendor = attributes.getAttribute(CmsisConstants.DVENDOR);
		return findItem(deviceName, vendor, true);
	}

	@Override
	public ILiteDeviceItem getVendorItem() {
		if(getLevel() == EDeviceHierarchyLevel.VENDOR.ordinal()) {
			return this;
		} else if(getLevel() > EDeviceHierarchyLevel.VENDOR.ordinal()) {
			if(getParent() != null) {
				return getParent().getVendorItem();
			}
		}
		return null;
	}

	@Override
	public ILiteDeviceItem getVendorItem(String vendor) {
		vendor = DeviceVendor.getOfficialVendorName(vendor);
		if(getLevel() == EDeviceHierarchyLevel.ROOT.ordinal()) {
			return getChild(vendor);
		}
		ILiteDeviceItem root = getRoot();
		if(root != null) {
			return root.getVendorItem(vendor);
		}
		return null;
	}

	@Override
	public String getDescription() {
		ICpDeviceItem deviceItem = getDevice();
		if(deviceItem != null) {
			String description = deviceItem.getDescription();
			if(description != null && !description.isEmpty()) {
				return description;
			}
		}
		if(getParent() != null) {
			return getParent().getDescription();
		}
		return CmsisConstants.EMPTY_STRING;
	}

	@Override
	public String getUrl() {
		ICpDeviceItem device = getDevice();
		if(device != null) {
			return device.getUrl();
		}
		return null;
	}

	@Override
	public String getDoc() {
		ICpDeviceItem device = getDevice();
		if(device != null)
		{
			return device.getDoc(); // TODO: return a collection of documents
		}
		return null;
	}

	@Override
	public Set<String> getAllDeviceNames() {
		if(fDeviceNames == null ){
			fDeviceNames = new HashSet<String>();
			if(isDevice())
				addDeviceName(getDevice().getName());
			if (fChildMap != null) {
				for (ILiteDeviceItem item : fChildMap.values()) {
					fDeviceNames.addAll(item.getAllDeviceNames());
				}
			}
		} 
		return fDeviceNames;
	}

	@Override
	public String getVendorName() {
		if (fLevel == EDeviceHierarchyLevel.VENDOR.ordinal()) {
			return fName;
		}
		if (getParent() != null) {
			return getParent().getVendorName();
		}
		return CmsisConstants.EMPTY_STRING;
	}

	@Override
	public void addDeviceName(String name) {
		if (fDeviceNames != null ) 
			fDeviceNames.add(name);
		if (getParent() != null) {
			getParent().addDeviceName(name);
		}
	}

	
	@Override
	public void removeDeviceName(String name) {
		if (fDeviceNames != null)
			fDeviceNames.remove(name);
		
		if (getParent() != null) {
			getParent().removeDeviceName(name);
		}
	}

}
