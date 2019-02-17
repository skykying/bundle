
package com.lembed.lite.studio.debug.gdbjtag.views;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.IExpressionsListener;
import org.eclipse.debug.core.model.IExpression;
import org.eclipse.debug.internal.ui.views.expression.ExpressionView;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.progress.UIJob;

import com.lembed.lite.studio.core.SystemUIJob;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;

/**
 * The Class LiveExpressionsView.
 */
@SuppressWarnings("restriction")
public class LiveExpressionsView extends ExpressionView implements IDebugEventSetListener, IExpressionsListener {

	Set<IExpression> expressionContainer;

	private UIJob fRefreshUIjob = new SystemUIJob(LiveExpressionsView.class.getSimpleName() + "#refreshUIjob") { //$NON-NLS-1$

		@Override
        public IStatus runInUIThread(IProgressMonitor pm) {

			Viewer viewer = getViewer();
			if (viewer != null) {
				viewer.refresh();
			}
			return Status.OK_STATUS;
		}
	};

	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		
		for (DebugEvent event : events) {
			GdbActivator.log("LiveExpressionsView.handleDebugEvents(1)"+event.getKind()); //$NON-NLS-1$
			if (event.getKind() == DebugEvent.MODEL_SPECIFIC) {
				// Currently no longer fired
				Object object = event.getSource();
				GdbActivator.log("LiveExpressionsView.handleDebugEvents(2)"+object.getClass().getSimpleName()); //$NON-NLS-1$
				if ((object instanceof LiveExpressionsView)) {
					refresh();
					GdbActivator.log("LiveExpressionsView.handleDebugEvents(3)"); //$NON-NLS-1$
				}
			}
		}

	}

	private void addExpressionListener() {
		DebugPlugin.getDefault().getExpressionManager().addExpressionListener(this);
	}

	private void removeExpressionListener() {
		DebugPlugin.getDefault().getExpressionManager().removeExpressionListener(this);
	}

	private void addDebugEventListener() {
		DebugPlugin.getDefault().addDebugEventListener(this);
	}

	private void removeDebugEventListener() {
		DebugPlugin.getDefault().removeDebugEventListener(this);
	}

	@Override
	public Viewer createViewer(Composite parent) {

		addExpressionListener();
		addDebugEventListener();

		return super.createViewer(parent);
	}

	@Override
	public void dispose() {
		super.dispose();

		removeDebugEventListener();
		removeExpressionListener();
	}

	private void refresh() {
		GdbActivator.log("LiveExpressionsView.refresh()"); //$NON-NLS-1$
		fRefreshUIjob.schedule();
	}

	@Override
	public void expressionsAdded(IExpression[] expressions) {
		// These are notifications that the memory blocks were already added
		for (int i = 0; i < expressions.length; i++) {
			if ((expressions[i] != null)) {

				IExpression exp = expressions[i];

				GdbActivator.log("LiveExpressionsView.expressionsAdded() [] " + exp); //$NON-NLS-1$

				expressionContainer.add(exp);
			}
		}

	}

	@Override
	public void expressionsRemoved(IExpression[] expressions) {

		for (int i = 0; i < expressions.length; i++) {
			if ((expressions[i] != null)) {

				IExpression exp = expressions[i];

				GdbActivator.log("LiveExpressionsView.expressionsRemoved() [] " + exp); //$NON-NLS-1$

				expressionContainer.remove(exp);
			}
		}

		// Update the check box status when the memory block is removed from the
		// monitor view.
		refresh();
	}

	@Override
	public void expressionsChanged(IExpression[] expressions) {
		for (int i = 0; i < expressions.length; i++) {
			if ((expressions[i] != null)) {

				IExpression exp = expressions[i];
				if (!expressionContainer.contains(exp)) {
					expressionContainer.add(exp);
				}
				GdbActivator.log("LiveExpressionsView.expressionsChanged() [] " + exp); //$NON-NLS-1$
			}
		}
		refresh();
	}

}
