package com.lembed.unit.test.runner.cpputest.testcase;

import com.lembed.unit.test.runner.cpputest.UTAbstractCase;
import com.lembed.unit.test.runner.model.ITestModelUpdater;
import com.lembed.unit.test.runner.model.ITestItem.Status;

public class UTestPassedTestCase extends UTAbstractCase {

	@Override
	public void updateModel(ITestModelUpdater modelUpdater) {
		modelUpdater.enterTestCase(getCaseName());
		modelUpdater.setTestStatus(Status.Passed);
		modelUpdater.setTestingTime(getTestingTime());
		modelUpdater.exitTestCase();
	}

}
