package com.shinyhut.vernacular;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.ui.awt.Platform;
import com.lembed.lite.studio.ui.awt.SwingComponentConstructor;

public class VncView {

	public static void createVNCViewer(ViewPart cview, Composite composite) {

		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		JScrollPane jscp = new JScrollPane();

		VernacularViewer view = new VernacularViewer();

		jscp.setViewportView(view);
		jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscp.revalidate();

		createComponentScroll(cview, composite, jscp);

	}

	public static void createComponentScroll(ViewPart view, Composite parent, final JScrollPane jp) {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(() -> {
			org.eclipse.swt.widgets.Control[] cc = parent.getChildren();
			for (org.eclipse.swt.widgets.Control c : cc) {
				c.dispose();
			}

			org.eclipse.swt.graphics.Rectangle vb = view.getSite().getShell().getBounds();

			SwingComponentConstructor embeddedComposite = new SwingComponentConstructor() {

				@Override
				public JComponent createSwingComponent() {

					jp.setBounds(vb.x, vb.y, vb.width, vb.height);
					return jp;
				}
			};

			Platform.createComposite(parent, view.getSite().getShell().getDisplay(), embeddedComposite);

			parent.redraw();
			parent.requestLayout();
		});
	}

}
