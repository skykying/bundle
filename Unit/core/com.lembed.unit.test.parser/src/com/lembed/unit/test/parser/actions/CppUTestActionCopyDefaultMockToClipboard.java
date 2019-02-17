package com.lembed.unit.test.parser.actions;

import org.eclipse.jface.action.IAction;

import com.lembed.unit.test.parser.ICppUTestFactory;

public class CppUTestActionCopyDefaultMockToClipboard extends CppUTestAction {
	public CppUTestActionCopyDefaultMockToClipboard() {
		super();
	}

	public CppUTestActionCopyDefaultMockToClipboard(ICppUTestFactory factory) {
		super(factory);
	}

	public void run(IAction action) {
		factory.createStubCodeUI().copyStubCodeInEditorToClipboard(factory.createDefaultMockStubber());
	}

}