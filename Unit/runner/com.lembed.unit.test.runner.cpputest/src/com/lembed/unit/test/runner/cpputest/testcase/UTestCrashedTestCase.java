package com.lembed.unit.test.runner.cpputest.testcase;

import com.lembed.unit.test.runner.cpputest.Messages;
import com.lembed.unit.test.runner.cpputest.UTAbstractCase;
import com.lembed.unit.test.runner.model.ITestMessage;
import com.lembed.unit.test.runner.model.ITestModelUpdater;
import com.lembed.unit.test.runner.model.ITestItem.Status;


public class UTestCrashedTestCase extends UTAbstractCase {

	@Override
	public void updateModel(ITestModelUpdater modelUpdater) {
		modelUpdater.enterTestCase(getCaseName());
		modelUpdater.setTestStatus(Status.Failed);
		modelUpdater.addTestMessage("", 0, ITestMessage.Level.Error, Messages.Crashed_Unexpected_Termination);
		modelUpdater.setTestingTime(0);
		modelUpdater.exitTestCase();
	}
}
