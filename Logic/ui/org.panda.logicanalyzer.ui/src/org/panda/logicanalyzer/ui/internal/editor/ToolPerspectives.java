package org.panda.logicanalyzer.ui.internal.editor;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class ToolPerspectives implements IPerspectiveFactory {

	public static final String ID = "org.panda.logicanalyzer.ui.ToolPerspectives";

	private IPageLayout fFactory;

	public void createInitialLayout(IPageLayout layout) {
		fFactory = layout;

		addViews();
		addViewShortcuts();
		System.out.println("ToolPerspectives");

	}

	private void addViews() {

		IFolderLayout bottom = fFactory.createFolder("logicBottom", // NON-NLS-1
		                       IPageLayout.BOTTOM, 0.75f, fFactory.getEditorArea());
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		// bottom.addView("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		// bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);

		IFolderLayout topLeft = fFactory.createFolder("logicDevice", // NON-NLS-1
		                        IPageLayout.LEFT, 0.20f, fFactory.getEditorArea());
		// topLeft.addView(IPageLayout.ID_RES_NAV);
		topLeft.addView("org.panda.logicanalyzer.ui.ge.DeviceView");
		// topLeft.addView("org.panda.ide.packs.ui.views.BoardsView");
		// topLeft.addView("org.panda.ide.packs.ui.views.KeywordsView");

		IFolderLayout topRight = fFactory.createFolder("logicOutline", // NON-NLS-1
		                         IPageLayout.RIGHT, 0.2f, fFactory.getEditorArea());
		// topRight.addView(IPageLayout.ID_OUTLINE);
		// topRight.addView("org.panda.ide.packs.ui.views.PackagesView");
		// // Leave 20% for the editor
		topRight.addView("org.panda.logicanalyzer.ui.ge.toolInspectorView"); // NON-NLS-1


		// Leave 20% for the editor
		// fFactory.addView("org.panda.logicanalyzer.ui.ge.toolInspectorView", // NON-NLS-1
		//                  IPageLayout.TOP, 0.8f, fFactory.getEditorArea());

	}

	@SuppressWarnings("deprecation")
	private void addViewShortcuts() {

		fFactory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		// fFactory.addShowViewShortcut(JavaUI.ID_PACKAGES);
		fFactory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		fFactory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		fFactory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	}

}
