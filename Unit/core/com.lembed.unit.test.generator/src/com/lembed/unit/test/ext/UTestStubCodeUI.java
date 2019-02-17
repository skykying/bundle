package com.lembed.unit.test.ext;

import com.lembed.unit.test.ext.code.IUnitTestAdaptor;
import com.lembed.unit.test.ext.code.UTestCode;
import com.lembed.unit.test.ext.code.ICodeFormater;
import com.lembed.unit.test.ext.code.IStubber;

public class UTestStubCodeUI implements IStubCodeUI {
	public final IUnitTestAdaptor platform;
	public ISourceCodeStubberForEditor sourceCodeStubber;
	public final ICodeFormater formater;

	public UTestStubCodeUI(IUnitTestAdaptor platform, ISourceCodeStubberForEditor sourceCodeStubber, ICodeFormater formater) {
		this.platform = platform;
		this.sourceCodeStubber = sourceCodeStubber;
		this.formater = formater;
	}
	protected void copyFormatedCodeToClipboard(UTestCode stubCode) {
		if (!stubCode.isEmpty()) { 
			String stub = formater.format(stubCode);
			platform.copyToClipboard(stub);
		}
		else
			platform.messageBox("No function is selected.");
	}
	@Override
	public void copyStubCodeInEditorToClipboard(IStubber stubber) {
		UTestCode stubCode = sourceCodeStubber.getStubOfCodeInEditor(stubber);
		copyFormatedCodeToClipboard(stubCode);
	}
}