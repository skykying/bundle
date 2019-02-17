package com.lembed.unit.test.parser;

import com.lembed.unit.test.parser.langunit.CppLangFunctionSignature;

public interface SourceCodeReader {

	Iterable<CppLangFunctionSignature> signatures(String sourceCode);

}
