package com.lembed.unit.test.ext.code;

import com.lembed.unit.test.ext.parser.ILanguageUnit;

public class UTestEmptyStubber implements IStubber {

	@Override
	public UTestCode getStubOfSignature(ILanguageUnit signature) {
		UTestCode stub = signature.getCode();
		if (signature.isVoid()) {
			stub.append("{}\n");
		}else {
			stub.append("{return 0;}\n");
		}
		return stub;
	}

}
