package com.lembed.unit.test.parser;

import com.lembed.unit.test.parser.codeGenerator.CppCode;
import com.lembed.unit.test.parser.codeGenerator.SourceCodeProvider;
import com.lembed.unit.test.parser.codeGenerator.Stubber;
import com.lembed.unit.test.parser.codeGenerator.WhoCreateStubFromSourceCode;

public class CppUTestSourceCodeStubberForEditor implements SourceCodeStubberForEditor {
	private final SourceCodeProvider resource;
	private final WhoCreateStubFromSourceCode codeGenerator;

	public CppUTestSourceCodeStubberForEditor(SourceCodeProvider resource, WhoCreateStubFromSourceCode codeGenerator) {
		this.resource = resource;
		this.codeGenerator = codeGenerator;
	}

	protected CppCode getStubOfCodeAtPosition(Stubber stubber) {
		CppCode stubCode;
		String allCode = resource.getFullText();
		int pos = resource.getCursorPosition();
		stubCode = codeGenerator.getStubOfCodeAtPosition(allCode, pos, stubber);
		return stubCode;
	}

	protected CppCode getStubOfSelectedCode(Stubber stubber) {
		String code = resource.getSelectedText();
		CppCode stubCode = codeGenerator.getStubOfCode(code, stubber);
		return stubCode;
	}

	@Override
	public CppCode getStubOfCodeInEditor(Stubber stubber) {
		CppCode stubCode = getStubOfSelectedCode(stubber);
		if (stubCode.isEmpty()) {
			stubCode = getStubOfCodeAtPosition(stubber);
		}
		return stubCode;
	}
}