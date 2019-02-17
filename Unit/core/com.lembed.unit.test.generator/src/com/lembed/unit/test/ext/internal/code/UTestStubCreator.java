package com.lembed.unit.test.ext.internal.code;

import com.lembed.unit.test.ext.code.UTestCode;
import com.lembed.unit.test.ext.internal.parser.LangFunctionSignature;
import com.lembed.unit.test.ext.code.IStubber;
import com.lembed.unit.test.ext.code.IStubCreator;
import com.lembed.unit.test.ext.parser.ISourceCodeReader;

public class UTestStubCreator implements IStubCreator {
	private final ISourceCodeReader signatures;

	public UTestStubCreator(ISourceCodeReader reader) {
		this.signatures = reader;
	}

	@Override
	public UTestCode getStubOfCode(String sourceCode, IStubber stubber) {
		UTestCode stubCode = new UTestCode();
		for(LangFunctionSignature sig: signatures.signatures(sourceCode))
			stubCode.append(stubber.getStubOfSignature(sig));
		
		return stubCode;
	}

	@Override
	public UTestCode getStubOfCodeAtPosition(String allCode, int offset,
			IStubber stubber) {
		for(LangFunctionSignature sig: signatures.signatures(allCode))
			if (sig.isOffsetInclusive(offset))
				return stubber.getStubOfSignature(sig);
		
		return new UTestCode();
	}
}
