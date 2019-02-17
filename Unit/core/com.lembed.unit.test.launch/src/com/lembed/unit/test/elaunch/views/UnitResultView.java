package com.lembed.unit.test.elaunch.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

import com.lembed.unit.test.runner.internal.ui.view.ResultsView;

public class UnitResultView extends ResultsView {

	@Override
	public void createPartControl(Composite parent) {		
		super.createPartControl(parent);
	}

	@Override
	public void setFocus() {		
		super.setFocus();
	}

	@Override
	public void dispose() {		
		super.dispose();
	}

	@Override
	public void setOrientation(Orientation orientation) {		
		super.setOrientation(orientation);
	}

	@Override
	public void updateActionsFromSession() {		
		super.updateActionsFromSession();
	}

	@Override
	public void setCaption(String message) {		
		super.setCaption(message);
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {		
		super.init(site, memento);
	}

	@Override
	public void saveState(IMemento memento) {		
		super.saveState(memento);
	}

	@Override
	public boolean isDisposed() {		
		return super.isDisposed();
	}
	
	
	

}
