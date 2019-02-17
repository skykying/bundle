package com.lembed.unit.test.ext.internal.parser;

import com.lembed.unit.test.ext.code.UTestCode;
import com.lembed.unit.test.ext.parser.ILanguageUnit;

public class LangFunction implements ILanguageUnit {

	public LangFunction(String code) {
	}

	@Override
	public UTestCode getCode() {
		return null;
	}

	@Override
	public boolean isOffsetInclusive(int offset) {
		return false;
	}

	@Override
	public boolean isVoid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getFunctionName() {
		// TODO Auto-generated method stub
		return null;
	}

}
