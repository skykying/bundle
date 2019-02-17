package com.lembed.unit.test.ext.parser;

import com.lembed.unit.test.ext.code.UTestCode;

public interface ILanguageUnit {

	UTestCode getCode();

	boolean isOffsetInclusive(int offset);

	boolean isVoid();

	String getFunctionName();

}
