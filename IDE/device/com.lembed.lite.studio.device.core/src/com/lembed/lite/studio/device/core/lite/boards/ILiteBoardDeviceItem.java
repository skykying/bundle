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

package com.lembed.lite.studio.device.core.lite.boards;

import java.util.Collection;
import java.util.Set;

import com.lembed.lite.studio.device.core.data.ICpBoard;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.item.ICmsisMapItem;

/**
 * Interface base element for BoardDevice elements.
 * This first level are boards, then mounted and compatible devices of this board
 */
public interface ILiteBoardDeviceItem extends ICmsisMapItem<ILiteBoardDeviceItem>  {

	/**
	 * Return true if this node is the root
	 * @return true if this node is the root
	 */
	boolean isRoot();

	/**
	 * Add board
	 * @param item
	 */
	void addBoard(ICpBoard item);

	/**
	 * Add boards from supplied pack
	 * @param pack IcpPack to add boards from
	 */
	void addBoards(ICpPack pack);

	/**
	 * Remove board
	 * @param item
	 */
	void removeBoard(ICpBoard item);

	/**
	 * Remove boards from supplied pack
	 * @param pack IcpPack to remove boards from
	 */
	void removeBoards(ICpPack pack);

	/**
	 * Get the board of the latest installed pack,
	 * or the board from the latest pack
	 * @return
	 */
	ICpBoard getBoard();

	/**
	 * Return the board with the id
	 * @param boardId
	 * @return board with the id, or null
	 */
	ILiteBoardDeviceItem findBoard(String boardId);

	/**
	 * Get boards from all the packs
	 * @return boards from all the packs
	 */
	Collection<ICpBoard> getBoards();

	/**
	 * Get a set of mounted devices of this board
	 * @return a set of mounted devices of this board
	 */
	ILiteDeviceItem getMountedDevices();

	/**
	 * Get a set of compatible devices of this board
	 * @return a set of compatible devices of this board
	 */
	ILiteDeviceItem getCompatibleDevices();
	
	/**
	 * Get names of all devices in this item
	 * @return set of device names 
	 */
	Set<String> getAllDeviceNames();

	
}
