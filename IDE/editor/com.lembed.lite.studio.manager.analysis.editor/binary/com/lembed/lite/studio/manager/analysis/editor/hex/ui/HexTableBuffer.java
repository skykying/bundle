/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.hex.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;



import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.manager.analysis.editor.binary.Activator;
import com.lembed.lite.studio.manager.analysis.editor.hex.HexEditorConstants;
import com.lembed.lite.studio.manager.analysis.editor.hex.Messages;


@SuppressWarnings("javadoc")
public class HexTableBuffer {
	private static final int BUFFER_DEFAULT_THRESHOLD = 64 * 1024;  // by default 64 kB

	private HexTable table;
	private byte[] buffer = null;
	private File file = null;
	private int regionSize = 0;
	
	/**
	 * Constructor
	 * @param table
	 */
	public HexTableBuffer(HexTable table) {
		this.table = table;
	}

	/**
	 * Stores the table region from the specified position to the end to the buffer
	 * @param from table pointer
	 * @param to table pointer
	 * @return 
	 */
	public boolean storeTableRegion(HexTablePointer from, HexTablePointer to) {
//		if (PropertyChangeListener.isDebugMode())
//			PropertyChangeListener.log("storeTableRegion(): rowIndexBegin=" + from.getRowIndex() + ", columnIndexBegin=" + from.getColumnIndex() + ", rowIndexEnd=" + to.getRowIndex() + ", columnIndexEnd=" + to.getColumnIndex()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		regionSize = (to.getRowIndex() * HexEditorConstants.TABLE_NUM_DATA_COLUMNS + to.getColumnIndex() + 1) - (from.getRowIndex() * HexEditorConstants.TABLE_NUM_DATA_COLUMNS + from.getColumnIndex());
		
		if (regionSize > BUFFER_DEFAULT_THRESHOLD) {
			//
			// Internal buffer is too small to backup the specified region - use temp file
			//
			return saveData(from);
		} // if
        //
        // Use internal buffer to backup specified region
        //
        return putData(from);
	}

	/**
	 * Restores the table region from buffer into the table from specified position
	 * @param from table pointer
	 * @return result
	 */
	public boolean restoreTableRegion(HexTablePointer from) {
		if (file != null) {
			//
			// Get data from the backup file
			//
			return loadData(from);
		} // if
        //
        // Get data from the memory buffer
        //
        return getData(from);
	}

	/**
	 * Puts the data into the memory buffer
	 * @param p 
	 * @return boolean true if the data was successfully written to the buffer, otherwise false
	 */
	private boolean putData(HexTablePointer p) {
		//
		// Allocate the buffer
		//
		buffer = new byte[regionSize];
		table.getData(buffer, p.getOffset(), regionSize);

		return true;
	}

	/**
	 * Gets data from the memory buffer to the table from specified position
	 * @param p position where put data from the memory buffer
	 * @return boolean
	 */
	private boolean getData(HexTablePointer p) {
		if (buffer == null) return false;
		
//		if (PropertyChangeListener.isDebugMode())
//			PropertyChangeListener.log("getData(): p=" + p.toString()); //$NON-NLS-1$
		
		table.setData(buffer, p.getOffset(), buffer.length);

		return true;
	}

	/**
	 * Saves the data to the temp file
	 * @param p 
	 * @return boolean true if the data was successfully written to the file, otherwise false
	 */
	private boolean saveData(final HexTablePointer p) {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().run(true, true, new IRunnableWithProgress() {
				@Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					try {
						doSaveData(p, monitor);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					}
					if (monitor.isCanceled())
						throw new InterruptedException();
				}
			});
		} catch (InterruptedException e) {
			return false;
		} catch (Exception e) {
			// should not occur
			e.printStackTrace();
		}		
		return true;
	}
	
	private void doSaveData(HexTablePointer p, IProgressMonitor monitor) throws Exception {
		//
		// Get the temp file descriptor
		//
		file = getTempFile();
		
		if (file.exists()) {
			//
			// Temp file already exists, delete it
			//
			file.delete();
		} // if

		byte[] buf = new byte[BUFFER_DEFAULT_THRESHOLD];

		monitor.beginTask(Messages.HexTableBuffer_5, regionSize / buf.length);

		try (FileOutputStream out = new FileOutputStream(file)){

			int m = 0;
			while (m < regionSize) {
				int n = table.getData(buf, p.getOffset(), Math.min(buf.length, regionSize - m));
				if (n == 0)
					break;
				out.write(buf, 0, n);
				if (monitor.isCanceled())
					break;
				monitor.worked(1);
				
				p.move(n);
				m += n;
			}
		} finally {
			
			monitor.done();
			
			buf = null;
			buffer = null;

			if (monitor.isCanceled()) {
				file.delete();
			}

		}
	}
	
	/**
	 * Loads the data from the temp file
	 * @param p Table pointer where to load the data
	 * @return boolean true if the data was successfully loaded from the file, otherwise false
	 */
	private boolean loadData(final HexTablePointer p) {
		if (file == null) return false;
		
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().run(true, true, new IRunnableWithProgress() {
				@Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					try {
						doLoadData(p, monitor);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					}
					if (monitor.isCanceled())
						throw new InterruptedException();
				}
			});
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			MessageDialog.openError(table.getShell(), Messages.HexTableBuffer_6, Messages.HexTableBuffer_7 + file.getAbsolutePath() + "!"); //$NON-NLS-1$
		}		
		return true;
	}

	private boolean doLoadData(HexTablePointer p, IProgressMonitor monitor) throws Exception {

		byte[] buf = new byte[BUFFER_DEFAULT_THRESHOLD];
		
		monitor.beginTask(Messages.HexTableBuffer_9, regionSize / buf.length);

		try (FileInputStream in = new FileInputStream(file)) {
			
			int regionEnd = p.getOffset() + regionSize;

			while (p.getOffset() < regionEnd) {
				int n = in.read(buf);
				table.setData(buf, p.getOffset(), n);
				
				if (monitor.isCanceled())
					break;
				monitor.worked(1);
				
				p.move(n);
			} // while
		}finally {
			
			monitor.done();
			
			file.delete();				
		}

		return true;
	}

	/**
	 * Gets the temp. file descriptor
	 * @return the temp file descriptor
	 */
	private File getTempFile() {
		IPath path = Activator.getDefault().getStateLocation();
		String filename;
		Random random = new Random();
		
		do {
			filename = "tmp" + Math.abs(random.nextInt()) + ".bin"; //$NON-NLS-1$ //$NON-NLS-2$
			file = new File(path.toOSString() + File.separatorChar + filename);
		} while (file.exists());
		
		random = null;
		
		return file;
	}

	/**
	 * Gets the table region size
	 * @return the table region size
	 */
	public int getTableRegionSize() {
		return regionSize;
	}

	/**
	 * Disposes the object
	 */
	public void dispose() {
		buffer = null;
		if (file != null)
			file.delete();
		file = null;
	}
}
