package com.lembed.unit.test.runner.cpputest.testcase;

import com.lembed.unit.test.runner.cpputest.UTAbstractCase;

public class UTestCaseFactory {

	private static final String TEST_START = "TEST";
	private static final String TEST_START_IGNORE = "IGNORE_TEST";
	private static final char TEST_GROUP_NAME_START = '(';
	private static final char GROUP_TEST_SEPARATOR = ',';
	private static final char TEST_GROUP_NAME_END = ')';
	private static final char SPACE = ' ';
	private static final char FILE_LINE_SEPARATOR = ':';
	private static final String NEW_LINE = "\n";
	private static final String END_OF_TEST = "ms"; // Each test ends with time of execution in ms

	private static final String EMPTY = "";

	/*
	 * Sample CppUTest output: TEST(TestSuite, TestName) - 0 ms
	 */

	public UTAbstractCase create(String testCaseOutput) {

		if (testCaseOutput == null) {
			return createFailedTestcase(null);
		}

		if (testCaseOutput.isEmpty()) {
			return createFailedTestcase(testCaseOutput);
		}

		if (isCrashed(testCaseOutput)) {
			return createCrashTestCase(testCaseOutput);
		}

		if (isPassed(testCaseOutput)) {
			return createPassedTestcase(testCaseOutput);
		}

		if (isIgnored(testCaseOutput)) {
			return createIgnoredTestCase(testCaseOutput);
		}

		return createFailedTestcase(testCaseOutput);
	}

	private boolean isCrashed(String testCaseOutput) {
		return !testCaseOutput.endsWith(END_OF_TEST);
	}

	private boolean isPassed(String testCaseOutput) {
		
		if(testCaseOutput.contains(TEST_START+"")) {
			return (testCaseOutput.startsWith(TEST_START) && !testCaseOutput.contains(NEW_LINE));
		}		
		
		return false;
	}

	private boolean isIgnored(String testCaseOutput) {
		if(testCaseOutput.contains(TEST_START_IGNORE+"")) {
			return testCaseOutput.startsWith(TEST_START_IGNORE);
		}
		return false;
	}

	private UTAbstractCase createCrashTestCase(String testCaseOutput) {
		UTestCrashedTestCase testCase = new UTestCrashedTestCase();

		if (testCaseOutput != null) {
			if (!testCaseOutput.isEmpty()) {
				String suitName = getTestSuiteName(testCaseOutput);
				if (suitName != null) {
					testCase.setSuiteName(suitName);
				}

				String caseName = getTestName(testCaseOutput);
				if (caseName != null) {
					testCase.setCaseName(caseName);
				}
			}
		}

		return testCase;
	}

	private UTAbstractCase createIgnoredTestCase(String testCaseOutput) {
		UTestIgnoredTestCase testCase = new UTestIgnoredTestCase();

		if (testCaseOutput != null) {
			if (!testCaseOutput.isEmpty()) {
				testCase.setSuiteName(getTestSuiteName(testCaseOutput));
				testCase.setCaseName(getTestName(testCaseOutput));
				testCase.setTestingTime(getTestingTime(testCaseOutput));
			}
		}
		return testCase;
	}

	private UTAbstractCase createPassedTestcase(String testCaseOutput) {
		UTestPassedTestCase testCase = new UTestPassedTestCase();

		if (testCaseOutput != null) {
			if (!testCaseOutput.isEmpty()) {
				testCase.setSuiteName(getTestSuiteName(testCaseOutput));
				testCase.setCaseName(getTestName(testCaseOutput));
				testCase.setTestingTime(getTestingTime(testCaseOutput));
			}
		}

		return testCase;
	}

	private UTAbstractCase createFailedTestcase(String testCaseOutput) {
		UTestFailedTestCase testCase = new UTestFailedTestCase();

		if (testCaseOutput != null) {
			if (!testCaseOutput.isEmpty()) {
				testCase.setSuiteName(getTestSuiteName(testCaseOutput));
				testCase.setCaseName(getTestName(testCaseOutput));
				testCase.setTestingTime(getTestingTime(testCaseOutput));

				String errorMessage = getErrorMessage(testCaseOutput);
				if (errorMessage != null) {
					testCase.setErrorMessage(errorMessage);
					testCase.setFileName(getFileName(errorMessage));
					testCase.setLineNumber(getLineNumber(errorMessage));
				}
			}
		}

		return testCase;
	}

	private String getTestSuiteName(String testCaseOutput) {

		if (testCaseOutput != null) {
			if (!testCaseOutput.isEmpty()) {

				if (testCaseOutput.contains(TEST_GROUP_NAME_START + "")) {

					if (testCaseOutput.contains(GROUP_TEST_SEPARATOR + "")) {
						int istart = testCaseOutput.indexOf(TEST_GROUP_NAME_START) + 1;
						int isep = testCaseOutput.indexOf(GROUP_TEST_SEPARATOR);

						if (istart > 0 && isep > 0) {
							return testCaseOutput.substring(istart, isep);
						}
					}
				}

			}
		}

		return EMPTY;
	}

	private String getTestName(String testCaseOutput) {
		if (testCaseOutput != null) {
			if (!testCaseOutput.isEmpty()) {

				if (testCaseOutput.contains(GROUP_TEST_SEPARATOR + "")) {
					if (testCaseOutput.contains(TEST_GROUP_NAME_END + "")) {
						int isep = testCaseOutput.indexOf(GROUP_TEST_SEPARATOR) + 2;
						if (isep < testCaseOutput.length()) {
							int iend = testCaseOutput.indexOf(TEST_GROUP_NAME_END);
							if (iend < testCaseOutput.length()) {
								return testCaseOutput.substring(isep, iend);
							}
						}
					}
				}
			}
		}
		return EMPTY;
	}

	private int getTestingTime(String testCaseOutput) {

		if (testCaseOutput.contains(SPACE + "")) {
			return Integer.parseInt(testCaseOutput.substring(
					testCaseOutput.lastIndexOf(SPACE, testCaseOutput.lastIndexOf(SPACE) - 1) + 1,
					testCaseOutput.lastIndexOf(SPACE)));
		}

		return 0;
	}

	private String getErrorMessage(String errorMessage) {
		if (errorMessage != null) {
			if (!errorMessage.isEmpty()) {
				if (errorMessage.contains(NEW_LINE + "")) {
					return errorMessage.substring(errorMessage.indexOf(NEW_LINE.charAt(0)) + 1,
							errorMessage.lastIndexOf(NEW_LINE + NEW_LINE) + 1);
				}				
			}
		}
		return EMPTY;
	}

	private int getLineNumber(String errorMessage) {
		if (errorMessage != null) {
			if (!errorMessage.isEmpty()) {
				if (errorMessage.contains(FILE_LINE_SEPARATOR + "")) {
					int positionAfterFirstColon = errorMessage.indexOf(FILE_LINE_SEPARATOR) + 1;
					int positionAfterSecondColon = errorMessage.indexOf(FILE_LINE_SEPARATOR, positionAfterFirstColon);
					return Integer.parseInt(errorMessage.substring(positionAfterFirstColon, positionAfterSecondColon));
				}
			}
		}

		return -1;
	}

	private String getFileName(String errorMessage) {
		if (errorMessage != null) {
			if (!errorMessage.isEmpty()) {
				if (errorMessage.contains(FILE_LINE_SEPARATOR + "")) {
					return errorMessage.substring(0, errorMessage.indexOf(FILE_LINE_SEPARATOR));
				}
			}
		}

		return EMPTY;
	}

}
