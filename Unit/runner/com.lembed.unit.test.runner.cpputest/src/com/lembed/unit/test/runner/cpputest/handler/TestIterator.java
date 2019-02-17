package com.lembed.unit.test.runner.cpputest.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.lembed.unit.test.runner.cpputest.UTAbstractCase;
import com.lembed.unit.test.runner.cpputest.testcase.UTestCaseFactory;

public class TestIterator {

	private static final String NEW_LINE = "\n";
	private static final String END_OF_TEST = "ms"; // Each test ends with time of execution in ms
	private static final String END_OF_RUN_OK = "OK (";
	private static final String END_OF_RUN_ERR = "Errors (";

	private final BufferedReader bufferedReader;
	
	public TestIterator(InputStream inputStream) {
		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	}

	public UTAbstractCase fetchTest() throws IOException {
		String output = "";
		String thisLine = null; 
		
		for (; (thisLine = bufferedReader.readLine()) != null; output+= NEW_LINE){
			output += thisLine;
			if (output.endsWith(END_OF_TEST)) {
				break;
			}
		}
		
		System.out.println(output);
		
		if (output.length() == 0 || output.startsWith(NEW_LINE)) {
			return null;
		}
			
		if (output.startsWith(END_OF_RUN_OK) || output.startsWith(END_OF_RUN_ERR)) {
			return null;
		}			
		
		return new UTestCaseFactory().create(output);
	}

}
