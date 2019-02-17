package com.lembed.unit.test.parser;

import com.lembed.unit.test.parser.codeGenerator.CppCode;
import com.lembed.unit.test.parser.codeGenerator.Stubber;

public interface SourceCodeStubberForEditor {
	CppCode getStubOfCodeInEditor(Stubber stubber);
}