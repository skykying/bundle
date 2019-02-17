package com.lembed.unit.test.runner.cpputest.parser;

import java.io.IOException;
import java.io.InputStream;

import com.lembed.unit.test.runner.cpputest.IUTParser;
import com.lembed.unit.test.runner.cpputest.UTAbstractCase;
import com.lembed.unit.test.runner.cpputest.handler.TestIterator;
import com.lembed.unit.test.runner.model.ITestModelUpdater;

public class UTestStdOutputParser implements IUTParser{

	ITestModelUpdater modelUpdater;
	
	/**
	 * default constructor
	 * @param modelUpdater
	 */
	public UTestStdOutputParser(ITestModelUpdater modelUpdater) {
		this.modelUpdater = modelUpdater;
	}

	
	/**
	 * parser the test messages 
	 */
	@Override
	public void parse(InputStream inputStream) throws IOException {
		TestIterator testIterator = new TestIterator(inputStream);
		UTAbstractCase testCase = null;
		String lastTestSuiteName = null;
		
		while(true) {
			testCase = testIterator.fetchTest();
			
			if(testCase != null) {
				lastTestSuiteName = getNewTestSuiteName(lastTestSuiteName, testCase.getSuiteName());
				testCase.updateModel(modelUpdater);
			}else {
				modelUpdater.exitTestSuite();
				return;
			}
		}
	}
	
	
	/**
	 * get the test suite name
	 * @param lastTestSuiteName
	 * @param newTestSuiteName
	 * @return
	 */
	private String getNewTestSuiteName(String lastTestSuiteName, String newTestSuiteName) {
		if (newTestSuiteName.equals(lastTestSuiteName)) {
			return newTestSuiteName;
		}
		
		if (lastTestSuiteName != null) {
			modelUpdater.exitTestSuite();
		}
		
		modelUpdater.enterTestSuite(newTestSuiteName);
		return newTestSuiteName;
	}


}
