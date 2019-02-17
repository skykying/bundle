/*******************************************************************************
 * Copyright (c) 2016 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.internal.analysis.os.linux.core.inputoutput.handlers;

import org.eclipse.tracecompass.analysis.os.linux.core.trace.IKernelAnalysisEventLayout;
import org.eclipse.tracecompass.common.core.NonNullUtils;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.inputoutput.BlockIO;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.inputoutput.DiskWriteModel;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.inputoutput.InputOutputStateProvider;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.inputoutput.Request;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.kernel.handlers.KernelEventHandler;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventField;

/**
 * IO Request merge event handler
 *
 * @author Houssem Daoud
 */
public class BlockFrontMergeHandler extends KernelEventHandler {

    private final InputOutputStateProvider fStateProvider;

    /**
     * Constructor
     *
     * @param layout
     *            event layout
     * @param sp
     *            The state provider calling this handler
     */
    public BlockFrontMergeHandler(IKernelAnalysisEventLayout layout, InputOutputStateProvider sp) {
        super(layout);
        fStateProvider = sp;
    }

    @Override
    public void handleEvent(ITmfStateSystemBuilder ss, ITmfEvent event) throws AttributeNotFoundException {
        ITmfEventField content = event.getContent();
        long ts = event.getTimestamp().getValue();

        Long sector = NonNullUtils.checkNotNull((Long) content.getField(getLayout().fieldBlockSector()).getValue());
        int nrSector = ((Long) content.getField(getLayout().fieldBlockNrSector()).getValue()).intValue();
        Long rqSector = sector + nrSector;
        int dev = ((Long) content.getField(getLayout().fieldBlockDeviceId()).getValue()).intValue();
        int rwbs = ((Long) content.getField(getLayout().fieldBlockRwbs()).getValue()).intValue();
        DiskWriteModel disk = fStateProvider.getDisk(dev);

        Request request = disk.getWaitingRequest(rqSector);
        if (request == null) {
            BlockIO bio = new BlockIO(rqSector, 1, disk, rwbs);
            request = new Request(bio);
        }
        BlockIO bio = new BlockIO(sector, nrSector, disk, rwbs);
        request.insertBio(bio);

        disk.updateWaitingRequest(ts, request, rqSector);
    }

}
