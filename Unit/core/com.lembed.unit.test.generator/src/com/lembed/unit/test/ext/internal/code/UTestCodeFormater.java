package com.lembed.unit.test.ext.internal.code;

import com.lembed.unit.test.ext.code.UTestCode;
import com.lembed.unit.test.ext.code.ICodeFormater;

public class UTestCodeFormater implements ICodeFormater {

	public String format(UTestCode code) {
		return code.toString();
	}
}
