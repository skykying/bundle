package com.lembed.unit.test.parser.actions;

import org.eclipse.jface.action.IAction;

import com.lembed.unit.test.parser.ICppUTestFactory;

public class CppUTestActionCopyEmptyStubToClipboard extends CppUTestAction {
	public CppUTestActionCopyEmptyStubToClipboard() {
		super();
	}

	public CppUTestActionCopyEmptyStubToClipboard(ICppUTestFactory factory) {
		super(factory);
	}

	public void run(IAction action) {
		factory.createStubCodeUI().copyStubCodeInEditorToClipboard(factory.createEmptyStubber());
	}

}