package com.lembed.unit.test.parser;

import org.eclipse.ui.IWorkbenchWindow;

import com.lembed.unit.test.parser.codeGenerator.CUTPlatformAdaptor;
import com.lembed.unit.test.parser.codeGenerator.CompactCppCodeFormater;
import com.lembed.unit.test.parser.codeGenerator.CppCodeFormater;
import com.lembed.unit.test.parser.codeGenerator.CppDefaultMockStubber;
import com.lembed.unit.test.parser.codeGenerator.CppEmptyStubber;
import com.lembed.unit.test.parser.codeGenerator.CppUTestStubCreator;
import com.lembed.unit.test.parser.codeGenerator.Stubber;

public class CppUTestFactory implements ICppUTestFactory {
	private CppUTestEclipsePlatform platform = null;

	@Override
	public CUTPlatformAdaptor createPlatformAdaptor(IWorkbenchWindow window) {
		platform = new CppUTestEclipsePlatform(window);
		return platform;
	}

	@Override
	public SourceCodeStubberForEditor createSourceCodeStubber() {
		CppSourceCodeReader reader = new CppSourceCodeReader();
		CppUTestStubCreator codeGenerator = new CppUTestStubCreator(reader);
		return new CppUTestSourceCodeStubberForEditor(platform, codeGenerator);
	}

	@Override
	public CppCodeFormater createCodeFormater() {
		return new CompactCppCodeFormater();
	}

	@Override
	public Stubber createEmptyStubber() {
		return new CppEmptyStubber();
	}

	@Override
	public StubCodeUI createStubCodeUI() {
		return new CppUTestStubCodeUI(platform, createSourceCodeStubber(), createCodeFormater());
	}

	@Override
	public Stubber createDefaultMockStubber() {
		return new CppDefaultMockStubber();
	}
}
