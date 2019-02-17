package com.lembed.unit.test.ext.code;

public interface IStubCreator {
	UTestCode getStubOfCode(String string, IStubber stubber);

	UTestCode getStubOfCodeAtPosition(String aLL_CODE, int oFFSET, IStubber stubber);
}
