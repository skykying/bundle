/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.elf.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import com.lembed.lite.studio.manager.analysis.editor.command.AbstractParserProcess;
import com.lembed.lite.studio.manager.analysis.editor.command.EditorProcessResultHandler;
import com.lembed.lite.studio.manager.analysis.editor.command.IEditorConsole;
import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;
import com.lembed.lite.studio.manager.analysis.editor.command.ProcessExecutionException;

public abstract class ElfParserBase extends AbstractParserProcess {

	protected  static String DELIMITER = ":";
	protected  static String BLANK = " ";
	protected  static String[] DEFAULT_ARGUMENTS = { "" };

	/**
	 * pattern recognizes "2/2 files checked 100% done"
	 */
	protected  static Pattern PROGRESS_PATTERN = Pattern
			.compile("^((\\d)*)/(\\d)* files checked (\\d)*% done");

	/**
	 * pattern recognizes "Checking src/test.1.cpp..."
	 */
	protected  static Pattern FILE_PATTERN = Pattern.compile("^Checking (.*)...");

	protected  Collection<String> arguments;
	protected String advancedArguments;
	
	protected  List<ParserResult> parserResults = new LinkedList<ParserResult>();
	
	/**
	 * For testing purposes either use interfaces or simple types as parameters.
	 * No dependency to Eclipse classes allowed.
	 * 
	 * @param editorConsole
	 * @param settingsStore
	 *            either workspace or project settings
	 * @param advancedSettingsStore
	 *            always project settings
	 * @param userIncludePaths
	 * @param systemIncludePaths
	 * @param symbols
	 */
	public ElfParserBase(IEditorConsole editorConsole, String binaryPath) {
		super(editorConsole, DEFAULT_ARGUMENTS, binaryPath);
		arguments = new LinkedList<String>();
	}
	
	public void run(IFile file, IProgressMonitor monitor)
			throws IOException, URISyntaxException, ProcessExecutionException, InterruptedException{
		
		IProject project = file.getProject();
		
		// convert list of files to filenames
		File projectFolder = project.getLocation().toFile();
		
		// make this also work with linked resources
		File absoluteFile = file.getLocation().toFile();

		// make file relative to project
		String path = relativizeFile(projectFolder, absoluteFile).toString();
		
		setProcessInputStream(new ByteArrayInputStream(path.getBytes(DEFAULT_CHARSET)));
		
		setWorkingDirectory(projectFolder);
		arguments.add(path);
		
		EditorProcessResultHandler resultHandler = runInternal(arguments.toArray(new String[0]), 
				advancedArguments,	null);
		
		try {
			while (resultHandler.isRunning()) {
				Thread.sleep(SLEEP_TIME_MS);
				if (monitor.isCanceled()) {
					watchdog.destroyProcess();
					throw new InterruptedException("Process manually killed");
				}

				if (!parserResults.isEmpty()) {			
					parserResults.clear();
				}

				// parse output
				parseResultLines(project, getOutputReader(), parserResults);

			}
			waitForExit(resultHandler, monitor);
		} finally {
			if (resultHandler.isRunning()) {
				// always destroy process if it is still running here
				watchdog.destroyProcess();
			}
		}
	}

	public void parseResultLines(IProject project, BufferedReader reader, List<ParserResult> parserResults) throws IOException {
		String line;
		
		while ((line = reader.readLine()) != null) {
			try {
				ParserResult r = parseResult(line, project);
				if(r!=null){
					parserResults.add(r);
				}
			} catch (IllegalArgumentException e) {
				log("Problems parsing a result line",e);
			}
		}
	}

	public abstract ParserResult parseResult(String line, IProject project);

	public static String parseFilename(String line) {
		Matcher fileMatcher = FILE_PATTERN.matcher(line);
		if (fileMatcher.matches()) {
			return fileMatcher.group(1);
		}
		return null;
	}

	static public File relativizeFile(File parent, File file) throws URISyntaxException {
		// use Uri.relativize
		// regard the bugs in http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6226081
		URI fileUri = file.toURI();
		URI parentUri = parent.toURI();
		// constructor of File only allows absolute uris, therefore convert from string
		return new File(parentUri.relativize(fileUri).getPath());
	}
	
	public void addArguments(String args) {
		arguments.add(args);
	}
	
	
	/**
	 * @return the parserResults
	 */
	public List<ParserResult> getParserResults() {
		return parserResults;
	}

	protected static void log(String string, IllegalArgumentException e) {
		System.out.println(string);
	}
	
	protected static void log(String string) {
		System.out.println(string);
	}
	
}
