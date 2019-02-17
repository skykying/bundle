package com.lembed.unit.test.parser.langunit;

import com.lembed.unit.test.parser.codeGenerator.CppCode;

public class CppLangFunctionImplementation implements LanguageUnit {

	public CppLangFunctionImplementation(String code) {
	}

	@Override
	public CppCode getCode() {
		return null;
	}

	@Override
	public boolean isOffsetInclusive(int offset) {
		return false;
	}

}
