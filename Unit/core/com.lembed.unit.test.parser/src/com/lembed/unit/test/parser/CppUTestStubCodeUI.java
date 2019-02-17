package com.lembed.unit.test.parser;

import com.lembed.unit.test.parser.codeGenerator.CUTPlatformAdaptor;
import com.lembed.unit.test.parser.codeGenerator.CppCode;
import com.lembed.unit.test.parser.codeGenerator.CppCodeFormater;
import com.lembed.unit.test.parser.codeGenerator.Stubber;

public class CppUTestStubCodeUI implements StubCodeUI {
	public final CUTPlatformAdaptor platform;
	public SourceCodeStubberForEditor sourceCodeStubber;
	public final CppCodeFormater formater;

	public CppUTestStubCodeUI(CUTPlatformAdaptor platform, SourceCodeStubberForEditor sourceCodeStubber,
			CppCodeFormater formater) {
		this.platform = platform;
		this.sourceCodeStubber = sourceCodeStubber;
		this.formater = formater;
	}

	protected void copyFormatedCodeToClipboard(CppCode stubCode) {
		if (!stubCode.isEmpty()) {
			String stub = formater.format(stubCode);
			platform.copyToClipboard(stub);
		} else
			platform.messageBox("No function is selected.");
	}

	@Override
	public void copyStubCodeInEditorToClipboard(Stubber stubber) {
		CppCode stubCode = sourceCodeStubber.getStubOfCodeInEditor(stubber);
		copyFormatedCodeToClipboard(stubCode);
	}
}