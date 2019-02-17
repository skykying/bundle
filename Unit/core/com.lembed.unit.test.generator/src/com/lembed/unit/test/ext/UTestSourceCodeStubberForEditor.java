package com.lembed.unit.test.ext;

import com.lembed.unit.test.ext.code.UTestCode;
import com.lembed.unit.test.ext.code.ISourceCodeProvider;
import com.lembed.unit.test.ext.code.IStubber;
import com.lembed.unit.test.ext.code.IStubCreator;

public class UTestSourceCodeStubberForEditor implements ISourceCodeStubberForEditor {
	private final ISourceCodeProvider resource;
	private final IStubCreator codeGenerator;

	public UTestSourceCodeStubberForEditor(ISourceCodeProvider resource,
			IStubCreator codeGenerator) {
				this.resource = resource;
				this.codeGenerator = codeGenerator;
	}
	
	protected UTestCode getStubOfCodeAtPosition(IStubber stubber) {
		UTestCode stubCode;
		String allCode = resource.getFullText();
		int pos = resource.getCursorPosition();
		stubCode = codeGenerator.getStubOfCodeAtPosition(allCode, pos, stubber);
		return stubCode;
	}
	
	protected UTestCode getStubOfSelectedCode(IStubber stubber) {
		String code = resource.getSelectedText();
		UTestCode stubCode = codeGenerator.getStubOfCode(code, stubber);
		return stubCode;
	}
	
	@Override
	public UTestCode getStubOfCodeInEditor(IStubber stubber) {
		UTestCode stubCode = getStubOfSelectedCode(stubber);
		if (stubCode.isEmpty()) { 
			stubCode = getStubOfCodeAtPosition(stubber);
		}
		return stubCode;
	}
}