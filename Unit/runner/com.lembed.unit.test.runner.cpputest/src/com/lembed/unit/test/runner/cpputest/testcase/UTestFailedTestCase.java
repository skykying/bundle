package com.lembed.unit.test.runner.cpputest.testcase;

import com.lembed.unit.test.runner.cpputest.UTAbstractCase;
import com.lembed.unit.test.runner.model.ITestMessage;
import com.lembed.unit.test.runner.model.ITestModelUpdater;
import com.lembed.unit.test.runner.model.ITestItem.Status;


public class UTestFailedTestCase extends UTAbstractCase {

	private String errorMessage = "error";
	private String fileName = "";
	private int lineNumber = -1;

	@Override
	public void updateModel(ITestModelUpdater modelUpdater) {
		modelUpdater.enterTestCase(getCaseName());
		modelUpdater.setTestStatus(Status.Failed);
		modelUpdater.setTestingTime(getTestingTime());
		modelUpdater.addTestMessage(fileName, lineNumber, ITestMessage.Level.Error, errorMessage);
		modelUpdater.exitTestCase();
	}

	public void setErrorMessage(String errorMessage) {
		if(errorMessage != null) {
			this.errorMessage = errorMessage;
		}		
	}

	public void setFileName(String fileName) {
		if(fileName != null) {
			this.fileName = fileName;
		}		
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
