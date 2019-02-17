package com.lembed.unit.test.runner.cpputest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.lembed.unit.test.runner.cpputest.IUTParser;
import com.lembed.unit.test.runner.cpputest.Messages;
import com.lembed.unit.test.runner.cpputest.parser.UTestStdOutputParser;
import com.lembed.unit.test.runner.launcher.ITestsRunnerProvider;
import com.lembed.unit.test.runner.model.ITestModelUpdater;
import com.lembed.unit.test.runner.model.TestingException;

public class UTRunnerProvider implements ITestsRunnerProvider{

	@Override
	public String[] getAdditionalLaunchParameters(String[][] testAndGroupNames) {
		if (testAndGroupNames == null || testAndGroupNames.length==0 || testAndGroupNames[0] == null )
			return getDefaultLaunchParameters().toArray(new String[0]);

		return getComplexLaunchParameters(testAndGroupNames).toArray(new String[0]);
	}

	private List<String> getComplexLaunchParameters(String[][] testGroupAndTestNames) {
		List<String> launchParameters = getDefaultLaunchParameters();
		for (String[] testGroupAndTestName : testGroupAndTestNames) {
			addGroupNameToLaunchParameters(getGroupName(testGroupAndTestName), launchParameters);
			addTestNameToLaunchParameters(getTestName(testGroupAndTestName), launchParameters);
		}
		return launchParameters;
	}

	private List<String> getDefaultLaunchParameters() {
		List<String> launchParameters = new ArrayList<String>();
		launchParameters.add("-r1");
		launchParameters.add("-v");
		return launchParameters;
	}
	
	private void addTestNameToLaunchParameters(String testName, List<String> launchParameters) {
		if (!testName.equals("")) {
			launchParameters.add("-sn" + testName);
		}			
	}

	private void addGroupNameToLaunchParameters(String groupName, List<String> launchParameters) {
		if (!groupName.equals(""))
			launchParameters.add("-sg" + groupName);
	}

	private String getGroupName(String[] testGroupAndTestName) {
		if (testGroupAndTestName.length == 0 || testGroupAndTestName[0] == null)
			return "";
		
		return testGroupAndTestName[0];
	}
	
	private String getTestName(String[] testGroupAndTestName) {
		if (testGroupAndTestName.length < 2 || testGroupAndTestName[1] == null)
			return "";
		
		return testGroupAndTestName[1];
	}
	
	
	
	
	/**
	 *  InputStream inputStream : std out
	 */
	@Override
	public void run(ITestModelUpdater modelUpdater, InputStream inputStream) throws TestingException {
		try {
			IUTParser parser = new UTestStdOutputParser(modelUpdater);
			if(parser != null) {
				parser.parse(inputStream);
			}
		} catch (IOException e) {
			String error = MessageFormat.format(Messages.CppUTestRunner_error_format, Messages.CppUTestRunner_io_error_prefix, e.getLocalizedMessage());
			throw new TestingException(error);
		}
	}
	
}
