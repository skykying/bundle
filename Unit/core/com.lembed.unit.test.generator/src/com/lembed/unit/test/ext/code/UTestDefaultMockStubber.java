package com.lembed.unit.test.ext.code;

import com.lembed.unit.test.ext.parser.ILanguageUnit;

public class UTestDefaultMockStubber implements IStubber {

	@Override
	public UTestCode getStubOfSignature(ILanguageUnit signature) {
		UTestCode stub = signature.getCode();
		stub.append("{");
		stub.append("mock().actualCall(\"");
		stub.append(signature.getFunctionName());
		stub.append("\");");
		stub.append("}\n");
		return stub;
	}

}
