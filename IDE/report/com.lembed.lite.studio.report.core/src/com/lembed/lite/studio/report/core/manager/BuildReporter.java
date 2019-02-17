/*******************************************************************************
 * Copyright (c) 2009, 2014 Alena Laskavaia
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alena Laskavaia  - initial API and implementation
 *******************************************************************************/
package com.lembed.lite.studio.report.core.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.eclipse.cdt.codan.core.CodanCorePlugin;
import org.eclipse.cdt.codan.core.CodanRuntime;
import org.eclipse.cdt.codan.core.model.AbstractProblemReporter;
import org.eclipse.cdt.codan.core.model.IChecker;
import org.eclipse.cdt.codan.core.model.ICheckersRegistry;
import org.eclipse.cdt.codan.core.model.ICodanProblemMarker;
import org.eclipse.cdt.codan.core.model.IProblem;
import org.eclipse.cdt.codan.core.model.IProblemLocation;
import org.eclipse.cdt.codan.core.model.IProblemReporter;
import org.eclipse.cdt.codan.core.model.IProblemReporterPersistent;
import org.eclipse.cdt.codan.core.model.IProblemReporterSessionPersistent;
import org.eclipse.cdt.codan.internal.core.model.CodanMarkerProblemReporter;
import org.eclipse.cdt.codan.internal.core.model.CodanProblemMarker;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.lembed.lite.studio.report.core.BuildAnalyzerPlugin;

/**
 * Problem reported that created eclipse markers
 */
@SuppressWarnings("restriction")
public class BuildReporter extends AbstractProblemReporter
        implements IProblemReporterPersistent, IProblemReporterSessionPersistent {
    private IResource resource;
    private IChecker checker;
    private ArrayList<ICodanProblemMarker> toAdd = new ArrayList<>();
    private IProblemReporter originReporter;

    private long index = 0;

    /**
     * Create instance, which can be use as factory for
     * IProblemReporterSessionPersistent or as IProblemReporterPersistent.
     */
    public BuildReporter() {
        super();
    }

    /**
     * @param resource
     *            IResource
     * @param checker
     *            IChecker
     */
    public BuildReporter(IResource resource, IChecker checker) {
        this.resource = resource;
        this.checker = checker;
    }

    @Override
    public IResource getResource() {
        return resource;
    }

    @Override
    public IChecker getChecker() {
        return checker;
    }

    /**
     * Hook problem.
     *
     * @param pm
     *            the pm
     * @param cker
     *            the checker
     */
    protected void hookProblem(ICodanProblemMarker pm, IChecker cker) {
        long millis = System.currentTimeMillis();
        index++;
        millis = millis + index;

        BuildManager manager = BuildManager.getInstance();
        manager.addProblem(millis, pm);

        // IProblem problem = pm.getProblem();
        // log(problem.getParentCategory().getName());
        // log(checker.getClass().getName());

    }

    @Override
    protected void reportProblem(ICodanProblemMarker codanProblemMarker) {
        if (checker == null) {
            createProblem(codanProblemMarker);
        } else {
            toAdd.add(codanProblemMarker);
        }

        hookProblem(codanProblemMarker, checker);
    }

    /**
     * Sets the parent.
     *
     * @param parent
     *            the new parent
     */
    public void setParent(IProblemReporter parent) {
        originReporter = parent;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public IProblemReporter getParent() {
        return originReporter;
    }

    /**
     * Register.
     */
    public void register() {
        // log("--------------------------");
        CodanRuntime runtime = CodanRuntime.getInstance();

        IProblemReporter parent = runtime.getProblemReporter();
        BuildReporter newRepoter = new BuildReporter();
        newRepoter.setParent(parent);

        runtime.setProblemReporter(newRepoter);
    }

    /**
     * reverse register.
     */
    public void deRegister() {
        CodanRuntime runtime = CodanRuntime.getInstance();
        IProblemReporter child = runtime.getProblemReporter();

        if (child == null) {
            CodanMarkerProblemReporter codanReporter = new CodanMarkerProblemReporter();
            runtime.setProblemReporter(codanReporter);
        } else if (child.getClass().getName().equals(BuildReporter.class.getName())) {
            BuildReporter base = (BuildReporter) child;
            IProblemReporter parent = base.getParent();
            runtime.setProblemReporter(parent);
        }

    }

    /**
     * @param codanProblemMarker ICodanProblemMarker
     * @return  IMarker
     */
    protected IMarker createProblem(ICodanProblemMarker codanProblemMarker) {
        try {
            return codanProblemMarker.createMarker();
        } catch (CoreException e) {
            CodanCorePlugin.log(e);
            return null;
        }
    }

    @Override
    public void deleteProblems(IResource file) {
        try {
            file.deleteMarkers(GENERIC_CODE_ANALYSIS_MARKER_TYPE, true, IResource.DEPTH_ZERO);
        } catch (CoreException ce) {
            CodanCorePlugin.log(ce);
        }
    }

    @Override
    public void deleteAllProblems() {
        try {
            ResourcesPlugin.getWorkspace().getRoot().deleteMarkers(GENERIC_CODE_ANALYSIS_MARKER_TYPE,
                    true, IResource.DEPTH_INFINITE);
        } catch (CoreException e) {
            CodanCorePlugin.log(e);
        }

        BuildAnalyzerPlugin.log("deleteAllProblems"); //$NON-NLS-1$
    }

    @Override
    public void deleteProblems(final IResource file, final IChecker cker) {
        try {
            ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
                @Override
                public void run(IProgressMonitor monitor) throws CoreException {
                    Collection<IMarker> markers = findResourceMarkers(file, cker);
                    for (IMarker marker : markers) {
                        marker.delete();
                    }
                }
            }, null, IWorkspace.AVOID_UPDATE, null);
        } catch (CoreException e) {
            CodanCorePlugin.log(e);
        }
    }

    /**
     * Find resource markers.
     *
     * @param rese the resource
     * @param cker the cker
     * @return the collection
     * @throws CoreException the core exception
     */
    protected Collection<IMarker> findResourceMarkers(IResource rese, IChecker cker) throws CoreException {
        Collection<IMarker> res = new ArrayList<>();
        IMarker[] markers;
        if (rese.exists()) {
            markers = rese.findMarkers(GENERIC_CODE_ANALYSIS_MARKER_TYPE, true,
                    IResource.DEPTH_INFINITE);
        } else {
            if (rese.getProject() == null || !rese.getProject().isAccessible())
                return res;
            // non resource markers attached to a project itself
            markers = rese.getProject().findMarkers(GENERIC_CODE_ANALYSIS_MARKER_TYPE, true,
                    IResource.DEPTH_ZERO);
        }
        ICheckersRegistry reg = CodanRuntime.getInstance().getCheckersRegistry();
        Collection<IProblem> problems = reg.getRefProblems(cker);
        for (IMarker m : markers) {
            String id = m.getAttribute(ICodanProblemMarker.ID, ""); //$NON-NLS-1$
            for (IProblem problem : problems) {
                if (problem.getId().equals(id)) {
                    res.add(m);
                }
            }
        }
        return res;
    }

    /**
     * @return session aware problem reporter
     * @since 1.1
     */
    @Override
    public IProblemReporterSessionPersistent createReporter(IResource rese, IChecker cker) {
        return new BuildReporter(rese, cker);
    }

    @Override
    public void start() {
        if (checker == null)
            deleteProblems(false);
    }

    @Override
    public void done() {
        if (checker != null) {
            if (toAdd.isEmpty()) {
                deleteProblems(false);
            } else {
                reconcileMarkers();
            }
            toAdd.clear();
        }
    }

    /**
     * Reconcile markers.
     */
    protected void reconcileMarkers() {
        try {
            ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
                @Override
                public void run(IProgressMonitor monitor) throws CoreException {
                    Collection<IMarker> markers = findResourceMarkers(resource, checker);
                    for (IMarker m : markers) {
                        ICodanProblemMarker cm = similarMarker(m);
                        if (cm == null) {
                            m.delete();
                        } else {
                            updateMarker(m, cm);
                            toAdd.remove(cm);
                        }
                    }
                    for (ICodanProblemMarker cm : toAdd) {
                        cm.createMarker();
                    }
                }
            }, null, IWorkspace.AVOID_UPDATE, null);
        } catch (CoreException e) {
            CodanCorePlugin.log(e);
        }
    }

    /**
     * @param m  IMarker
     * @param cm ICodanProblemMarker
     */
    protected void updateMarker(IMarker m, ICodanProblemMarker cm) {
        IProblemLocation loc = cm.getLocation();
        try {
            if (m.getAttribute(IMarker.LINE_NUMBER, 0) != loc.getLineNumber())
                m.setAttribute(IMarker.LINE_NUMBER, loc.getLineNumber());
            if (m.getAttribute(IMarker.CHAR_START, 0) != loc.getStartingChar())
                m.setAttribute(IMarker.CHAR_START, loc.getStartingChar());
            if (m.getAttribute(IMarker.CHAR_END, 0) != loc.getEndingChar())
                m.setAttribute(IMarker.CHAR_END, loc.getEndingChar());
            int severity = cm.getProblem().getSeverity().intValue();
            if (m.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) != severity)
                m.setAttribute(IMarker.SEVERITY, severity);
        } catch (CoreException e) {
            try {
                m.delete();
                cm.createMarker();
            } catch (CoreException e1) {
                CodanCorePlugin.log(e1);
            }
        }
    }

    /**
     * Similar marker.
     *
     * @param m the m
     * @return the i codan problem marker
     */
    protected ICodanProblemMarker similarMarker(IMarker m) {
        ICodanProblemMarker mcm = CodanProblemMarker.createCodanProblemMarkerFromResourceMarker(m);
        ArrayList<ICodanProblemMarker> cand = new ArrayList<>();
        for (ICodanProblemMarker cm : toAdd) {
            if (mcm.equals(cm))
                return cm;
            if (markersAreSimilar(mcm, cm)) {
                cand.add(cm);
            }
        }
        if (cand.size() == 1)
            return cand.get(0);
        return null;
    }

    /**
     * Markers are similar.
     *
     * @param marker1 the marker 1
     * @param marker2 the marker 2
     * @return true, if successful
     */
    private static boolean markersAreSimilar(ICodanProblemMarker marker1, ICodanProblemMarker marker2) {
        if (!marker1.getProblem().getId().equals(marker2.getProblem().getId()))
            return false;
        if (!Arrays.equals(marker1.getArgs(), marker2.getArgs()))
            return false;
        IProblemLocation loc1 = marker1.getLocation();
        IProblemLocation loc2 = marker2.getLocation();
        if (!loc1.getFile().equals(loc2.getFile()))
            return false;
        if (Math.abs(loc1.getLineNumber() - loc2.getLineNumber()) > 2)
            return false;
        return true;
    }

    @Override
    public void deleteProblems(boolean all) {
        if (all)
            throw new UnsupportedOperationException();
        deleteProblems(resource, checker);
    }
}
