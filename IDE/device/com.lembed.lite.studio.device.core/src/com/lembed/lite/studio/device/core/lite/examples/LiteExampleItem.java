/*******************************************************************************
* Copyright (c) 2016 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.core.lite.examples;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpEnvironmentProvider;
import com.lembed.lite.studio.device.core.data.ICpExample;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPack.PackState;
import com.lembed.lite.studio.device.item.CmsisMapItem;
import com.lembed.lite.studio.device.utils.AlnumComparator;
import com.lembed.lite.studio.device.utils.VersionComparator;

/**
 * Default implementation of IRteExampleItem
 */
public class LiteExampleItem extends CmsisMapItem<ILiteExampleItem> implements ILiteExampleItem {

	protected Map<String, ICpExample> fExamples = null;
	protected boolean fRoot;

	/**
	 * Default constructor, used for the root node
	 */
	public LiteExampleItem() {
		fName = "All Examples"; //$NON-NLS-1$
		fRoot = true;
	}

	/**
	 * Constructor with name and parent
	 * @param name the name
	 * @param parent the parent
	 */
	public LiteExampleItem(String name, ILiteExampleItem parent) {
		super(parent);
		fName = name;
		fRoot = false;
	}

	@Override
	protected Map<String, ILiteExampleItem> createMap() {
		// create TreeMap with Alpha-Numeric case-insensitive ascending sorting
		return new TreeMap<String, ILiteExampleItem>(new AlnumComparator(false, false));
	}

	public static ILiteExampleItem createTree(Collection<ICpPack> packs){
		ILiteExampleItem root = new LiteExampleItem();
		if(packs == null || packs.isEmpty()) {
			return root;
		}
		for(ICpPack pack : packs) {
			root.addExamples(pack);
		}
		return root;
	}

	@Override
	public void addExample(ICpExample item) {
		if (item == null) {
			return;
		}

		if (fRoot) {
			addExampleItem(item, item.getId());
		} else {
			ICpPack pack = item.getPack();
			String packId = pack.getId();
			if(fExamples == null) {
				fExamples = new TreeMap<String, ICpExample>(new VersionComparator());
			}

			ICpExample example = fExamples.get(packId);
			if (example == null ||
					// new item's pack is installed/downloaded and the one in the tree is not
					(item.getPack().getPackState().ordinal() < example.getPack().getPackState().ordinal())) {
				fExamples.put(packId, item);
			}
		}
	}

	protected void addExampleItem(ICpExample item, final String itemName) {
		ILiteExampleItem ei = getChild(itemName);
		if(ei == null ) {
			ei = new LiteExampleItem(itemName, this);
			addChild(ei);
		}
		ei.addExample(item);
	}

	@Override
	public void removeExample(ICpExample item) {
		if (item == null) {
			return;
		}

		if (fRoot) {
			ILiteExampleItem e = getChild(item.getId());
			if(e == null ) {
				return;
			}
			e.removeExample(item);
		} else {
			if (fExamples == null) {
				return;
			}

			String packId = item.getPackId();
			fExamples.remove(packId);

			if (fExamples.size() == 0) {
				getParent().removeChild(this);
				setParent(null);
			}
			return;
		}
	}

	@Override
	public void removeExamples(ICpPack pack) {
		if(pack == null) {
			return;
		}
		Collection<? extends ICpItem> examples = pack.getGrandChildren(CmsisConstants.EXAMPLES_TAG);
		if (examples != null) {
			for (ICpItem item : examples) {
				if (!(item instanceof ICpExample)) {
					continue;
				}
				ICpExample currentExample = (ICpExample)item;
				removeExample(currentExample);
			}
		}
	}

	@Override
	public ICpExample getExample() {
		if(fExamples != null && !fExamples.isEmpty()) {
			// Return the latest INSTALLED pack's example
			for (ICpExample example : fExamples.values()) {
				if (example.getPack().getPackState() == PackState.INSTALLED) {
					return example;
				}
			}
			// Otherwise return the latest pack's board
			return fExamples.values().iterator().next();
		}
		return null;
	}

	@Override
	public Collection<ICpExample> getExamples() {
		if (fExamples != null) {
			return fExamples.values();
		}
		return null;
	}


	@Override
	public void addExamples(ICpPack pack) {
		if(pack == null) {
			return;
		}
		Collection<? extends ICpItem> examples = pack.getGrandChildren(CmsisConstants.EXAMPLES_TAG);
		if(examples == null) {
			return;
		}
		ICpEnvironmentProvider envProvider = CpPlugIn.getEnvironmentProvider(); 
		
		for(ICpItem item : examples) {
			if(!(item instanceof ICpExample)) {
				continue;
			}
			ICpExample example = (ICpExample)item;
			if(envProvider == null || !envProvider.isSupported(example))
				continue;
			
			addExample(example);
		}
	}

}
