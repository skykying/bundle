package com.lembed.unit.test.ext.actions;

import org.eclipse.jface.action.IAction;

import com.lembed.unit.test.ext.ICppUTestFactory;

public class UTestActionCopyDefaultMockToClipboard extends UTestAction {
	public UTestActionCopyDefaultMockToClipboard() {
		super();
	}
	public UTestActionCopyDefaultMockToClipboard(ICppUTestFactory factory) {
		super(factory);
	}

	public void run(IAction action) {		
		factory.createStubCodeUI().copyStubCodeInEditorToClipboard(
				factory.createDefaultMockStubber());
	}
	
}