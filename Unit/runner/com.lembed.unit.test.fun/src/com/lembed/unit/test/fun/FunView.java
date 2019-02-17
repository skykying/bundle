package com.lembed.unit.test.fun;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cute.gui.JCuteGui;
import cute.gui.JCuteView;

public class FunView extends ViewPart {

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		JCuteGui v = new JCuteGui();
		v.main(null);
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
