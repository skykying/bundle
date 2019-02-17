/*******************************************************************************
 * Copyright (c) 2017 Lembed Electronic.
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version 
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.datamodel;

import com.lembed.lite.studio.core.Xml;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.device.core.svd.AbstractTreePreOrderIterator;
import com.lembed.lite.studio.debug.gdbjtag.data.SVDPathManagerProxy;
import com.lembed.lite.studio.debug.gdbjtag.data.SVDPathPackManagerProxy;
import com.lembed.lite.studio.device.core.svd.ITreeIterator;
import com.lembed.lite.studio.device.core.svd.Leaf;
import com.lembed.lite.studio.device.core.svd.SvdGenericParser;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utilities for processing CMSIS SVD files.
 * <p>
 * According to the SVD.xsd, a scaledNonNegativeInteger is defined as
 * "[+]?(0x|0X|#)?[0-9a-fA-F]+[kmgtKMGT]?"
 */
public class SvdUtils {

	// ------------------------------------------------------------------------

	private static long computeScale(String str) {

		long scale = 1;
		String lastChar = str.substring(str.length() - 1);
		if (lastChar.matches("[kmgtKMGT]")) { //NON-NLS-1$ //$NON-NLS-1$
			lastChar = lastChar.toLowerCase();
			if ("k".equals(lastChar)) {//NON-NLS-1$ //$NON-NLS-1$
				scale = 1024;
			} else if ("m".equals(lastChar)) {//NON-NLS-1$ //$NON-NLS-1$
				scale = 1024 * 1024;
			} else if ("g".equals(lastChar)) {//NON-NLS-1$ //$NON-NLS-1$
				scale = 1024 * 1024 * 1024;
			} else if ("7".equals(lastChar)) {//NON-NLS-1$ //$NON-NLS-1$
				scale = 1024 * 1024 * 1024 * 1024;
			}
		}
		return scale;
	}

	private static String adjustForScale(String str, long scale) {
		if (scale != 1) {
			return str.substring(0, str.length() - 2);
		}
		return str;
	}

	private static int computeRadix(String str) {

		int radix = 10;
		if ((str.startsWith("0x")) || (str.startsWith("0X"))) { //NON-NLS-2 //$NON-NLS-1$ //$NON-NLS-2$
			radix = 16;
		} else if ((str.startsWith("0b")) || (str.startsWith("0B"))) {//NON-NLS-2 //$NON-NLS-1$ //$NON-NLS-2$
			radix = 2;
		} else if (str.startsWith("#")) {//NON-NLS-1$ //$NON-NLS-1$
			radix = 2;
		}

		return radix;
	}

	private static String adjustForRadix(String str) {

		if ((str.startsWith("0x")) || (str.startsWith("0X"))) {//NON-NLS-2 //$NON-NLS-1$ //$NON-NLS-2$
			return str.substring(2); // Skip 0x, 0X
		} else if ((str.startsWith("0b")) || (str.startsWith("0B"))) { //NON-NLS-2 //$NON-NLS-1$ //$NON-NLS-2$
			return str.substring(2); // Skip 0b, 0B
		} else if (str.startsWith("#")) {//NON-NLS-1$ //$NON-NLS-1$
			return str.substring(1); // Skip #
		}

		// Decimal numbers have no radix prefix to skip.
		return str;
	}

	private static String adjustForSign(String str) {

		if (str.startsWith("+")) {//NON-NLS-1$ //$NON-NLS-1$
			return str.substring(1);
		}

		return str;
	}

	/**
	 * Parses the scaled non negative long.
	 *
	 * @param str the String
	 * @return the long
	 * @throws NumberFormatException the number format exception
	 */
	public static long parseScaledNonNegativeLong(String str) throws NumberFormatException {
	    String adjustedString = str;
	    adjustedString = adjustForSign(adjustedString);
		int radix = computeRadix(adjustedString);
		adjustedString = adjustForRadix(adjustedString);
		long scale = computeScale(adjustedString);
		adjustedString = adjustForScale(adjustedString, scale);

		long value = Long.parseLong(adjustedString, radix);
		if (scale != 1) {
			value *= scale;
		}
		return value;
	}

	/**
	 * Parses the scaled non negative big integer.
	 *
	 * @param str the String
	 * @return the big integer
	 * @throws NumberFormatException the number format exception
	 */
	public static BigInteger parseScaledNonNegativeBigInteger(String str) throws NumberFormatException {
	    String adjustedString = str;
	    adjustedString = adjustForSign(adjustedString);
		int radix = computeRadix(adjustedString);
		adjustedString = adjustForRadix(adjustedString);
		long scale = computeScale(adjustedString);
		adjustedString = adjustForScale(adjustedString, scale);

		BigInteger value = new BigInteger(adjustedString, radix);
		if (scale != 1) {
			value = value.multiply(new BigInteger(String.valueOf(scale)));
		}
		return value;
	}

	// ------------------------------------------------------------------------
	
	/**
	 * Identify the SVD file associated with the given device and parse it with
	 * the generic parser. this is modified for get svd file from cmsis package
	 * 
	 * @param deviceVendorId
	 *            a string with the numeric vendor id.
	 * @param deviceName
	 *            a string with the CMSIS device name.
	 * @return a tree with the parsed SVD.
	 * @throws CoreException
	 *             differentiate when the Packs plug-in is not installed or when
	 *             the device is not found in the installed packages.
	 */
	public static Leaf getTree(String deviceVendorId, String deviceName) throws CoreException {

		// MessageConsoleStream out = ConsoleStream.getConsoleOut();

		IPath path = null;

		// Try to get the SVD from a custom provider, like in DAVE.
		SVDPathPackManagerProxy pathManager = SVDPathPackManagerProxy.getInstance();
		path = pathManager.getSVDAbsolutePathById(deviceVendorId, deviceName);
		
		if(path == null){
			GdbActivator.log("There are no peripherals descriptions available, install the required packs"); //$NON-NLS-1$
			return null;
		}
        GdbActivator.log("svd path = "+ path); //$NON-NLS-1$

		try {

			File file = path.toFile();
			if (file == null) {
				throw new IOException(path + " File object null."); //$NON-NLS-1$
			}

			Document document = Xml.parseFile(file);

			/*
			 * insert the below code @2017.3.11 and comment the blow
			 * code @2017.3.13 PdscParser pparser = new PdscParser(); return
			 * (Leaf) pparser.parseFile(path.toOSString());
			 */

			/*
			 * replace the below code with up codes @2017.3.11 end reenable the
			 * code @2017.3.13
			 */
			SvdGenericParser parser = new SvdGenericParser();
			return parser.parse(document);

		} catch (ParserConfigurationException e) {
			GdbActivator.log(e);
			throw new CoreException(
					new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Failed to get the peripherals descriptions.", e)); //$NON-NLS-1$
		} catch (SAXException e) {
			GdbActivator.log(e);
			throw new CoreException(
					new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Failed to get the peripherals descriptions.", e)); //$NON-NLS-1$
		} catch (IOException e) {
			GdbActivator.log(e);
			throw new CoreException(
					new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Failed to get the peripherals descriptions.", e)); //$NON-NLS-1$
		}
	}
	
	/**
	 * Identify the SVD file associated with the given device and parse it with
	 * the generic parser.
	 * 
	 * @param deviceVendorId
	 *            a string with the numeric vendor id.
	 * @param deviceName
	 *            a string with the CMSIS device name.
	 * @return a tree with the parsed SVD or null.
	 * @throws CoreException
	 *             differentiate when the Packs plug-in is not installed or when
	 *             the device is not found in the installed packages.
	 */
	public static Leaf getTreeOld(String deviceVendorId, String deviceName) throws CoreException {

		// MessageConsoleStream out = ConsoleStream.getConsoleOut();

		IPath path = null;

		// Try to get the SVD from a custom provider, like in DAVE.
		SVDPathManagerProxy pathManager = SVDPathManagerProxy.getInstance();
		path = pathManager.getSVDAbsolutePathById(deviceVendorId, deviceName);

		if (path == null) {

			// // Try to get the SVD from the CMSIS Packs.
			// IPacksDataManager dataManager =
			// PacksDataManagerFactoryProxy.getInstance().createDataManager();
			// if (dataManager == null) {
			// throw new CoreException(new Status(Status.ERROR,
			// Activator.PLUGIN_ID,
			// "Peripherals descriptions are available only via the Packs
			// plug-in."));
			// }
			//
			// path = dataManager.getSVDAbsolutePath(deviceVendorId,
			// deviceName);
			// if (path == null) {
			// throw new CoreException(new Status(Status.ERROR,
			// Activator.PLUGIN_ID,
			// "There are no peripherals descriptions available, install the
			// required packs."));
			// }
		}

		if (path == null) {
			// ICpPackManager manager = new CpPackManager();
			// IRteDeviceItem devices = manager.getDevices();
			// IRteDeviceItem device = devices.findItem(deviceName,
			// deviceVendorId, true);
			// ICpDeviceItem d = device.getDevice();
		    return null;
		}

		try {

			// out.println("Parsing SVD file \"" + path.toOSString() + "\"...");

			File file = path.toFile();
			if (file == null) {
				throw new IOException(path + " File object null."); //$NON-NLS-1$
			}

			Document document = Xml.parseFile(file);

			/*
			 * insert the below code @2017.3.11 and comment the blow
			 * code @2017.3.13 PdscParser pparser = new PdscParser(); return
			 * (Leaf) pparser.parseFile(path.toOSString());
			 */

			/*
			 * replace the below code with up codes @2017.3.11 end reenable the
			 * code @2017.3.13
			 */
			SvdGenericParser parser = new SvdGenericParser();
			return parser.parse(document);

		} catch (ParserConfigurationException e) {
			GdbActivator.log(e);
			throw new CoreException(
					new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Failed to get the peripherals descriptions.", e)); //$NON-NLS-1$
		} catch (SAXException e) {
			GdbActivator.log(e);
			throw new CoreException(
					new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Failed to get the peripherals descriptions.", e)); //$NON-NLS-1$
		} catch (IOException e) {
			GdbActivator.log(e);
			throw new CoreException(
					new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Failed to get the peripherals descriptions.", e)); //$NON-NLS-1$
		}
	}

	/**
	 * Gets the peripherals.
	 *
	 * @param tree the tree
	 * @return the peripherals
	 */
	public static List<Leaf> getPeripherals(Leaf tree) {

		class SvdPeriphIterator extends AbstractTreePreOrderIterator {

			@Override
			public boolean isIterable(Leaf node) {
				if (node.isType("peripheral")) { //$NON-NLS-1$
					return true;
				}
				return false;
			}

			@Override
			public boolean isLeaf(Leaf node) {
				if (node.isType("peripheral")) { //$NON-NLS-1$
					return true;
				}
				return false;
			}
		}

		List<Leaf> list = new LinkedList<>();
		if (tree != null) {

			ITreeIterator peripheralNodes = new SvdPeriphIterator();

			peripheralNodes.setTreeNode(tree);
			for (Leaf peripheral : peripheralNodes) {
				list.add(peripheral);
			}

			if (!list.isEmpty()) {
				return list;
			}

		}

		return list;
	}
	
	// ------------------------------------------------------------------------
}
