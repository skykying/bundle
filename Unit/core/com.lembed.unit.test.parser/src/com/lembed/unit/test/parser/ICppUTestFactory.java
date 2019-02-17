package com.lembed.unit.test.parser;

import org.eclipse.ui.IWorkbenchWindow;

import com.lembed.unit.test.parser.codeGenerator.CUTPlatformAdaptor;
import com.lembed.unit.test.parser.codeGenerator.CppCodeFormater;
import com.lembed.unit.test.parser.codeGenerator.Stubber;

public interface ICppUTestFactory {

	CUTPlatformAdaptor createPlatformAdaptor(IWorkbenchWindow window);

	SourceCodeStubberForEditor createSourceCodeStubber();

	CppCodeFormater createCodeFormater();

	Stubber createEmptyStubber();

	StubCodeUI createStubCodeUI();

	Stubber createDefaultMockStubber();

}
