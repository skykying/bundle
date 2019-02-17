package com.lembed.unit.test.parser.langunit;

import com.lembed.unit.test.parser.codeGenerator.CppCode;

public interface LanguageUnit {

	CppCode getCode();

	boolean isOffsetInclusive(int offset);

}
