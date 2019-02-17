package org.eclipse.tracecompass.btf.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

@SuppressWarnings("javadoc")
public class PerpectiveHelper {

    public void postStartup() {
        IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        WorkbenchEditorsTracker workkbenchPartListener = new WorkbenchEditorsTracker();
        WorkflowStudioPerspectiveAdapter perspectiveListener = new WorkflowStudioPerspectiveAdapter(workkbenchPartListener);
        IPartService service = workbenchWindow.getPartService();
        service.addPartListener(workkbenchPartListener);
        workbenchWindow.addPerspectiveListener(perspectiveListener);
    }

    public class WorkflowStudioPerspectiveAdapter extends PerspectiveAdapter {

        private WorkbenchEditorsTracker workbenchEditorsTracker;

        public WorkflowStudioPerspectiveAdapter(WorkbenchEditorsTracker workbenchEditorsTracker) {
            this.workbenchEditorsTracker = workbenchEditorsTracker;
        }

        @Override
        public void perspectiveActivated(IWorkbenchPage page,
                IPerspectiveDescriptor perspectiveDescriptor) {
            super.perspectiveActivated(page, perspectiveDescriptor);
            // ----------------------------- update editors --------------------------------
            // ------------------- Only show editors related to the new perspective
            // -------------
            // Hide all the editors
            IEditorReference[] editors = page.getEditorReferences();
            for (int i = 0; i < editors.length; i++) {
                page.hideEditor(editors[i]);
            }

            // Show the editors associated with this perspective
            ArrayList<IEditorReference> editorRefs = workbenchEditorsTracker.getEditorsForPerspective(perspectiveDescriptor.getId());
            if (editorRefs != null) {
                for (Iterator<IEditorReference> it = editorRefs.iterator(); it.hasNext();) {
                    IEditorReference editorInput = it.next();
                    page.showEditor(editorInput);
                }

                // Send the last active editor to the top
                IEditorReference lastActiveRef = workbenchEditorsTracker.getLastActiveEditor(perspectiveDescriptor.getId());
                if (lastActiveRef != null) {
                    page.bringToTop(lastActiveRef.getPart(true));
                }
            }
        }

        @Override
        public void perspectiveDeactivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
            IEditorPart activeEditor = page.getActiveEditor();
            if (activeEditor != null) {
                // Find the editor reference that relates to this editor input
                IEditorReference[] editorRefs = page.findEditors(activeEditor.getEditorInput(), null, IWorkbenchPage.MATCH_INPUT);
                if (editorRefs.length > 0) {
                    workbenchEditorsTracker.setLastActiveEditor(perspective.getId(), editorRefs[0]);
                }
            }
        }
    }

    public class WorkbenchEditorsTracker implements IPartListener {

        /**
         * Stores a list of editors corresponding to each perspective
         */
        private HashMap<String, ArrayList<IEditorReference>> perspectiveEditors = new HashMap<>();

        /**
         * Stores an active editor for each perspective
         */
        private HashMap<String, IEditorReference> lastActiveEditors = new HashMap<>();

        @Override
        public void partActivated(IWorkbenchPart part) {
            // TODO Auto-generated method stub

        }

        @Override
        public void partBroughtToTop(IWorkbenchPart part) {
            // TODO Auto-generated method stub

        }

        @Override
        public void partClosed(IWorkbenchPart part) {
            if (part instanceof EditorPart) {
                // remove the editor from the perspective editors
                IWorkbenchPage page = part.getSite().getPage();
                IPerspectiveDescriptor activePerspective = page.getPerspective();
                ArrayList<IEditorReference> editors = perspectiveEditors.get(activePerspective.getId());
                if (editors == null) {
                    return;
                }
                Iterator<IEditorReference> iterator = editors.iterator();
                IEditorReference referenceToRemmove = null;
                while (iterator.hasNext()) {
                    IEditorReference reference = iterator.next();
                    if (reference.getPart(false) == part) {
                        referenceToRemmove = reference;
                        break;
                    }
                }
                if (referenceToRemmove != null) {
                    editors.remove(referenceToRemmove);
                }
            }
        }

        @Override
        public void partDeactivated(IWorkbenchPart part) {
            // TODO Auto-generated method stub

        }

        @Override
        public void partOpened(IWorkbenchPart part) {
            if (part instanceof EditorPart) {
                EditorPart editor = (EditorPart) part;
                IWorkbenchPage page = part.getSite().getPage();
                IEditorInput editorInput = editor.getEditorInput();
                IPerspectiveDescriptor activePerspective = page.getPerspective();

                ArrayList<IEditorReference> editors = perspectiveEditors.get(activePerspective.getId());
                if (editors == null) {
                    editors = new ArrayList<>();
                }

                // Find the editor reference that relates to this editor input
                IEditorReference[] editorRefs = page.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);

                if (editorRefs.length > 0) {
                    editors.add(editorRefs[0]);
                    perspectiveEditors.put(activePerspective.getId(), editors);
                }
            }
        }

        public ArrayList<IEditorReference> getEditorsForPerspective(String activePerspectiveId) {
            return perspectiveEditors.get(activePerspectiveId);
        }

        public IEditorReference getLastActiveEditor(String activePerspectiveId) {
            return lastActiveEditors.get(activePerspectiveId);
        }

        public void setLastActiveEditor(String perspectiveID, IEditorReference editor) {
            lastActiveEditors.put(perspectiveID, editor);

        }

    }

}
