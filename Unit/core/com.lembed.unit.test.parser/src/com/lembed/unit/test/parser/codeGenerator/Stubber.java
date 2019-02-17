package com.lembed.unit.test.parser.codeGenerator;

import com.lembed.unit.test.parser.langunit.CppLangFunctionSignature;

public interface Stubber {

	CppCode getStubOfSignature(CppLangFunctionSignature signature);

}
