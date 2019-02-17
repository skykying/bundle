package com.lembed.unit.test.parser.codeGenerator;

public class CompactCppCodeFormater implements CppCodeFormater {

	public String format(CppCode code) {
		return code.toString();
	}
}
