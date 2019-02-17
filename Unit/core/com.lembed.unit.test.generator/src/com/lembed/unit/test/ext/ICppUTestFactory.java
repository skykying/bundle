package com.lembed.unit.test.ext;

import org.eclipse.ui.IWorkbenchWindow;

import com.lembed.unit.test.ext.code.IUnitTestAdaptor;
import com.lembed.unit.test.ext.code.ICodeFormater;
import com.lembed.unit.test.ext.code.IStubber;

public interface ICppUTestFactory {

	IUnitTestAdaptor createPlatformAdaptor(IWorkbenchWindow window);

	ISourceCodeStubberForEditor createSourceCodeStubber();

	ICodeFormater createCodeFormater();

	IStubber createEmptyStubber();

	IStubCodeUI createStubCodeUI();

	IStubber createDefaultMockStubber();

}
