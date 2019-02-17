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
package com.lembed.lite.studio.manager.analysis.editor.hex;

import org.eclipse.swt.widgets.Composite;

import com.lembed.lite.studio.manager.analysis.editor.hex.editors.HexEditor;
import com.lembed.lite.studio.manager.analysis.editor.hex.ui.HexTable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import java.lang.reflect.InvocationTargetException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;

/**
 * The Class HexFileManager.
 */
public class HexFileManager {
    
    /** The Constant buffer. */
    public final static byte[] buffer = new byte[HexEditorConstants.BUFFER_SIZE]; // use buffer in table
    
    private Composite parent;
    private File file;
    private InputStream fileStream;
    private long fileSize;
    private int dataAvailable;

    /**
     * Creates a file manager
     * 
     * @param parent
     *            Composite
     * @param file
     *            File
     */
    public HexFileManager(Composite parent, File file) {
        this.parent = parent;
        this.file = file;

        try {
            fileStream = new FileInputStream(file);
            fileSize = file.length();
        }

        catch (Exception e) {
            fileStream = null;
        }
    }

    /**
     * Loads the data from file into the table
     * 
     * @param table
     *            reference to the hex table
     * @return true if the file has successfully been loaded, false else
     */
    public boolean loadFile(final HexTable table) {

        final boolean[] success = { true };
        //
        // Fill data into the table
        //
        try {
            table.setBufferSize((int) fileSize);

            if (file == null || fileStream == null || fileSize <= 0) {
                //
                // Add a dummy item
                //
                // table.addDummyItem();
            } // if
            else {
                final ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(parent.getShell());
                monitorDialog.setOpenOnRun(false);

                //
                // Define a long running operation
                //
                IRunnableWithProgress op = new IRunnableWithProgress() {
                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        long openTime = System.currentTimeMillis() + 2000L;
                        boolean opened = false;

                        monitor.beginTask(Messages.FileManager_0 + file.getName() + "...", (int) (fileSize / HexEditorConstants.BUFFER_SIZE)); //$NON-NLS-1$

                        //
                        // Load the file
                        //
                        long progressCounter = 0, progressStatus = 0;
                        int bufferIndex = 0;

                        try {
                            while ((dataAvailable = fileStream.read(buffer, 0, HexEditorConstants.BUFFER_SIZE)) > 0) {
                                if (!opened && System.currentTimeMillis() > openTime) {
                                    monitorDialog.getShell().getDisplay().asyncExec(new Runnable() {
                                        @Override
                                        public void run() {
                                            monitorDialog.open();
                                        }
                                    });

                                    opened = true;
                                }

                                //
                                // Update progress info below the progress bar
                                //
                                progressCounter += dataAvailable;
                                progressStatus = 100 * progressCounter / fileSize;

                                if (progressCounter < 1024) {
                                    monitor.subTask("" + progressCounter + Messages.FileManager_3 + progressStatus + "%)"); //$NON-NLS-1$ //$NON-NLS-2$
                                } else if (progressCounter < 1024 * 1024) {
                                    monitor.subTask("" + progressCounter / 1024 + Messages.FileManager_6 + progressStatus + "%)"); //$NON-NLS-1$ //$NON-NLS-2$
                                } else {
                                    monitor.subTask("" + progressCounter / (1024 * 1024) + Messages.FileManager_9 + progressStatus + "%)"); //$NON-NLS-1$ //$NON-NLS-2$
                                }

                                //
                                // Send data stored in the buffer to the table
                                //
                                final int index = bufferIndex;

                                monitorDialog.getShell().getDisplay().syncExec(new Runnable() {
                                    @Override
                                    public void run() {
                                        table.addData(index, buffer, dataAvailable);
                                    }
                                });

                                bufferIndex += dataAvailable;
                                monitor.worked(1);

                                if (!monitor.isCanceled())
                                    continue;

                                success[0] = false;
                                break;
                            } // while
                        }

                        catch (final Exception e) {
                            //
                            // Error reading file - do nothing
                            //
                            parent.getDisplay().syncExec(new Runnable() {
                                @Override
                                public void run() {
                                    MessageDialog.openInformation(parent.getShell(), HexEditorConstants.DIALOG_TITLE_EXCEPTION, Messages.FileManager_11 + file.getName() + "!\n\n" + e.toString()); //$NON-NLS-1$
                                }
                            });
                        } // catch

                        finally {
                            monitor.done();
                        }
                    } // run()
                };

                //
                // Start long-running operation
                //
                monitorDialog.run(true, true, op);
            } // else

            //
            // Close the input stream
            //
            fileStream.close();
        } // try

        catch (InvocationTargetException e) {
            //
            // Handle exception
            //
            Throwable realCause = e.getCause();
            Throwable realException = e.getTargetException();
            MessageDialog.openError(parent.getShell(), HexEditorConstants.DIALOG_TITLE_EXCEPTION, Messages.FileManager_13 + realException.getMessage() + Messages.FileManager_14 + realCause.getMessage());
        }

        catch (OutOfMemoryError e) {

            throw new RuntimeException(Messages.FileManager_15 + e);

        }

        catch (Exception e) {
            //
            // Error reading file - do nothing
            //
            MessageDialog.openInformation(parent.getShell(), HexEditorConstants.DIALOG_TITLE_EXCEPTION, Messages.FileManager_16 + file.getName() + "!\n\n" + e.toString()); //$NON-NLS-1$
        } // catch

        // at last set the virtual table item count
        // if an empty file is opened, a dummy item has been created
        if (table.getTable().getItemCount() == 0)
            table.getTable().setItemCount(table.getItemCount());

        //
        // Compress the table column widths
        //
        table.packColumns();
        return success[0];
    }

    /**
     * Saves the data from table
     * 
     * @param hexEditor
     *            HexEditor
     * @param table
     *            HexTable
     * @param ioFile
     *            File
     * @param monitor
     *            IProgressMonitor
     * @param isSaveAs
     *            boolean
     */
    public void saveFile(HexEditor hexEditor, HexTable table, File ioFile, IProgressMonitor monitor, boolean isSaveAs) {
        //
        // Get file location and name
        //
        String fileName = ioFile.getName();
        String filePathAndName = ioFile.getAbsolutePath();

        if (!isSaveAs) {
            //
            // Check file existency, accessibility and read-only status
            //
            if (!ioFile.exists()) {
                MessageDialog.openInformation(parent.getShell(), HexEditorConstants.DIALOG_TITLE_WARNING, Messages.FileManager_18 + filePathAndName + Messages.FileManager_19);
                return;
            }

            if (!ioFile.isFile()) {
                MessageDialog.openInformation(parent.getShell(), HexEditorConstants.DIALOG_TITLE_WARNING, Messages.FileManager_20 + filePathAndName + "!"); //$NON-NLS-1$
                return;
            }

            if (!ioFile.canWrite()) {
                MessageDialog.openInformation(parent.getShell(), HexEditorConstants.DIALOG_TITLE_WARNING, Messages.FileManager_22 + filePathAndName + Messages.FileManager_23);
                return;
            }
        } // if

        File pathFile = new File(filePathAndName);

        if (isSaveAs) {
            //
            // Create empty file - only if SaveAs was selected
            //
            try {
                HexManager.log(Messages.FileManager_24 + pathFile.getAbsolutePath() + "..."); //$NON-NLS-1$
                pathFile.createNewFile();
            } catch (Exception e) {
                HexManager.log(Messages.FileManager_26 + pathFile.getAbsolutePath() + "!"); //$NON-NLS-1$
                MessageDialog.openInformation(parent.getShell(), HexEditorConstants.DIALOG_TITLE_EXCEPTION, Messages.FileManager_28 + pathFile.getAbsolutePath() + "'!\n" + e.toString()); //$NON-NLS-1$
                return;
            }
        } // if

        //FileOutputStream outputStream = null;
        int sizeEstimate = table.getItemCount() * HexEditorConstants.TABLE_NUM_DATA_COLUMNS;

        table.setRedraw(false);

        //
        // Show progress bar only for files larger than 1 kB
        //
        boolean showProgress = (monitor != null) && (sizeEstimate > 1024);

        if (monitor != null) {

            try(FileOutputStream outputStream = new FileOutputStream(pathFile)) {

                if (showProgress) {
                    //
                    // Initialize the progress bar
                    //
                    monitor.beginTask(Messages.FileManager_30 + fileName + " ...", sizeEstimate); //$NON-NLS-1$
                } // if

                synchronized (buffer) {
                    //
                    // Get one-row data from the table into the buffer
                    //

                    int index = 0;
                    while (true) {
                        int n = table.getData(buffer, index, buffer.length);
                        if (n == 0)
                            break;
                        //
                        // Write data to the output stream
                        //
                        outputStream.write(buffer, 0, n);
                        index += n;

                        if (showProgress) {
                            //
                            // Update progress monitor
                            //
                            monitor.worked(n);
                        } // if

                    }
                    table.setCellStatus(0, 0, table.getBufferSize(), HexEditorConstants.CELL_STATUS_NORMAL);
                } // synchronized
 
                hexEditor.setDirty(false);

                if (showProgress) {
                    monitor.done();
                }
            } catch (Exception e) {
                MessageDialog.openInformation(parent.getShell(), HexEditorConstants.DIALOG_TITLE_EXCEPTION, Messages.FileManager_32 + e.toString());
            } 
        }
        table.setRedraw(true);
        table.redraw();
    }
}
