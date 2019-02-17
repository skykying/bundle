package com.lembed.unit.test.runner.cpputest;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.lembed.unit.test.runner.cpputest.Messages"; //$NON-NLS-1$
	public static String CppUTestRunner_error_format;
	public static String CppUTestRunner_io_error_prefix;
	public static String Crashed_Unexpected_Termination;
	
	public static String UnitTestsRunner_error_format;
	public static String UnitTestsRunner_io_error_prefix;
	public static String OutputHandler_getparam_message;
	public static String OutputHandler_unexpected_case_end;
	public static String OutputHandler_unexpected_output;
	public static String OutputHandler_unexpected_suite_end;
	public static String OutputHandler_unknown_error_prefix;
	public static String OutputHandler_unknown_location_format;
	public static String OutputHandler_unknown_test_status;
	public static String OutputHandler_wrong_groups_count; 
	public static String OutputHandler_wrong_suite_name;
	
	
	public static String CatchTestsRunner_error_format;
	public static String CatchTestsRunner_io_error_prefix;
	public static String CatchTestsRunner_wrong_tests_paths_count;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
