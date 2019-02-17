package com.lembed.unit.test.ext;

import com.lembed.unit.test.ext.code.UTestCode;
import com.lembed.unit.test.ext.code.IStubber;

public interface ISourceCodeStubberForEditor {
	
	UTestCode getStubOfCodeInEditor(IStubber stubber);
	
}