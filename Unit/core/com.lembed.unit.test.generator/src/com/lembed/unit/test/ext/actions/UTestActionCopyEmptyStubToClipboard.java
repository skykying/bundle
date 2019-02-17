package com.lembed.unit.test.ext.actions;

import org.eclipse.jface.action.IAction;

import com.lembed.unit.test.ext.ICppUTestFactory;

public class UTestActionCopyEmptyStubToClipboard extends UTestAction {
	public UTestActionCopyEmptyStubToClipboard() {
		super();
	}
	public UTestActionCopyEmptyStubToClipboard(ICppUTestFactory factory) {
		super(factory);
	}

	public void run(IAction action) {		
		factory.createStubCodeUI().copyStubCodeInEditorToClipboard(
				factory.createEmptyStubber());
	}
	
}