package com.lembed.unit.test.ext.parser;

import com.lembed.unit.test.ext.internal.parser.LangFunctionSignature;

public interface ISourceCodeReader {

	Iterable<LangFunctionSignature> signatures(String sourceCode);

}
