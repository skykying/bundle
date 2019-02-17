package com.lembed.unit.test.runner.cpputest;

import java.io.IOException;
import java.io.InputStream;

import com.lembed.unit.test.runner.model.TestingException;



public interface IOutputHandler {
	
	public void run(InputStream inputStream) throws IOException, TestingException;
	
}
