package com.lembed.unit.test.ext;

import org.eclipse.ui.IWorkbenchWindow;

import com.lembed.unit.test.ext.code.IUnitTestAdaptor;
import com.lembed.unit.test.ext.code.ICodeFormater;
import com.lembed.unit.test.ext.code.UTestDefaultMockStubber;
import com.lembed.unit.test.ext.code.UTestEmptyStubber;
import com.lembed.unit.test.ext.code.IStubber;
import com.lembed.unit.test.ext.internal.code.UTestCodeFormater;
import com.lembed.unit.test.ext.internal.code.UTestStubCreator;
import com.lembed.unit.test.ext.internal.parser.SourceCodeReader;

public class UTestFactory implements ICppUTestFactory {
	private UTestEclipsePlatform platform = null;
//	static public CppUTestActionRunnerForEclipseActions createCppUTestCodeGeneratorActions(IWorkbenchWindow window) {
//		CppSourceCodeReader reader = new CppSourceCodeReader();
//		CppStubber stubber = new CppStubber();
//		CppUTestEclipsePlatform eclipse = new CppUTestEclipsePlatform(window);
//		CppUTestStubCreator codeGenerator = new CppUTestStubCreator(reader, stubber);
//		SourceCodeStubber sourceCodeStubber = new CppUTestSourceCodeStubber(eclipse, codeGenerator);
//		CompactCppCodeFormater formater = new CompactCppCodeFormater();
//		return new CppUTestActionRunnerForEclipseActions(new CppUTestStubbingActions(eclipse, sourceCodeStubber, formater));
//	}

	@Override
	public IUnitTestAdaptor createPlatformAdaptor(IWorkbenchWindow window) {
		platform = new UTestEclipsePlatform(window);
		return platform;
	}

	@Override
	public ISourceCodeStubberForEditor createSourceCodeStubber() {
		SourceCodeReader reader = new SourceCodeReader();
		UTestStubCreator codeGenerator = new UTestStubCreator(reader);
		return new UTestSourceCodeStubberForEditor(platform, codeGenerator);
	}

	@Override
	public ICodeFormater createCodeFormater() {
		return new UTestCodeFormater();
	}

	@Override
	public IStubber createEmptyStubber() {
		return new UTestEmptyStubber();
	}

	@Override
	public IStubCodeUI createStubCodeUI() {
		return new UTestStubCodeUI(platform, createSourceCodeStubber(), createCodeFormater());
	}

	@Override
	public IStubber createDefaultMockStubber() {
		return new UTestDefaultMockStubber();
	}
}
