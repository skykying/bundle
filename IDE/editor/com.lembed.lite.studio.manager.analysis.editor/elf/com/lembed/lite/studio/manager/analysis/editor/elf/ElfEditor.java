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
package com.lembed.lite.studio.manager.analysis.editor.elf;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;


//https://www.embeddedrelated.com/showarticle/900.php
	
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.MultiPageEditorPart;
import com.lembed.lite.studio.manager.analysis.editor.color.ColorManager;
import com.lembed.lite.studio.manager.analysis.editor.command.DefaultToolchain;
import com.lembed.lite.studio.manager.analysis.editor.command.EditorConsole;
import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;
import com.lembed.lite.studio.manager.analysis.editor.command.ProcessExecutionException;
import com.lembed.lite.studio.manager.analysis.editor.elf.Messages;
import com.lembed.lite.studio.manager.analysis.editor.elf.parser.ElfArchParser;
import com.lembed.lite.studio.manager.analysis.editor.elf.parser.ElfFileHeaderParser;
import com.lembed.lite.studio.manager.analysis.editor.elf.parser.ElfParserBase;
import com.lembed.lite.studio.manager.analysis.editor.elf.parser.ElfSectionParser;
import com.lembed.lite.studio.manager.analysis.editor.elf.parser.ElfSymbolParser;
import com.lembed.lite.studio.manager.analysis.editor.elf.ui.ElfItemView;


public class ElfEditor extends MultiPageEditorPart {

	private ColorManager colorManager;
	ElfTextEditor editor;
	private IFile iFile;
	EditorConsole console = new EditorConsole();
	IProgressMonitor monitor = new NullProgressMonitor();
	IProject project;
	String nativeElfParser;
	
	private IDocument fDocument;	
	
	
	public ElfEditor() {
		super();
		colorManager = new ColorManager();
		log("create default page 00");
	}
	
	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	void createOverViewPage() {
		
		IEditorInput editorInput = getEditorInput();			
		iFile = ResourceUtil.getFile(editorInput);
		if(iFile == null){
			return;
		}
		
		editor = new ElfTextEditor();
		
		project = iFile.getProject();
		nativeElfParser = DefaultToolchain.getElfParserPath(project);
		if(nativeElfParser == null){
			return;
		}
		
		ElfParserBase parser = new ElfFileHeaderParser(console, nativeElfParser);		
		try {
			parser.run(iFile, monitor);
		} catch (IOException | URISyntaxException | ProcessExecutionException | InterruptedException e1) {
			e1.printStackTrace();
		}
		
        List<ParserResult> contentHeader = parser.getParserResults();

        parser = new ElfArchParser(console, nativeElfParser);
        try {
			parser.run(iFile, monitor);
		} catch (IOException | URISyntaxException | ProcessExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
        
        List<ParserResult> contentArch = parser.getParserResults();
        if(contentArch!=null){
        	contentHeader.addAll(contentArch);
        }
        int size = contentHeader.size();
        if(size>0){
        	Composite composite = new Composite(getContainer(), SWT.NONE);
    		composite.setLayout(new GridLayout(3, false));	
    	
    		int index = addPage(composite);
    		setPageText(index, Messages.ElfEditor_OverviewPageText);
    		
            ElfItemView view = new ElfItemView(contentHeader.get(size-1));
    		view.createPartControl(composite);
    		view.setInput(contentHeader);
        }

        monitor.done();
	}

	
	@Override
	protected void createPages() {
		createOverViewPage();
		createSymbolPage();
		createSectionPage();
		//createRawPage();
		
		setActivePage(0);
	}
	
	private void createRawPage() {
		if(nativeElfParser==null){
			return;
		}
		
		try {
			IEditorInput editorInput = getEditorInput();
			ElfTextEditor editor = new ElfTextEditor();
			int index = addPage(editor, editorInput);
			setPageText(index, Messages.ElfEditor_SymbolPageText);
			setPartName(editor.getTitle());
			
			fDocument = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	private void createSymbolPage() {
		if(nativeElfParser==null){
			return;
		}
		
		ElfParserBase parser = new ElfSymbolParser(console, nativeElfParser);		
		try {
			parser.run(iFile, monitor);
		} catch (IOException | URISyntaxException | ProcessExecutionException | InterruptedException e1) {
			e1.printStackTrace();
		}
		
        List<ParserResult> content = parser.getParserResults();
   
        int size = content.size();
        if(size>0){
    		Composite composite = new Composite(getContainer(), SWT.NONE);
    		composite.setLayout(new GridLayout(3, false));
    		
    		int index = addPage(composite);
    		setPageText(index, Messages.ElfEditor_SymbolPageText);
    		
            ElfItemView view = new ElfItemView(content.get(size-1));
    		view.createPartControl(composite);
    		view.setInput(content);
        }
	}

	private void createSectionPage() {		
		if(nativeElfParser==null){
			return;
		}
		
		ElfParserBase parser = new ElfSectionParser(console, nativeElfParser);		
		try {
			parser.run(iFile, monitor);
		} catch (IOException | URISyntaxException | ProcessExecutionException | InterruptedException e1) {
			e1.printStackTrace();
		}
		
        List<ParserResult> content = parser.getParserResults();

        int size = content.size();
        if(size>0){
        	Composite composite = new Composite(getContainer(), SWT.NONE);
    		composite.setLayout(new GridLayout(3, false));
    		
    		int index = addPage(composite);
    		setPageText(index, Messages.ElfEditor_SectionPageText);
    		
            ElfItemView view = new ElfItemView(content.get(size-1));
    		view.createPartControl(composite);
    		view.setInput(content);
        }
        
	}
	
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		iFile = ResourceUtil.getFile(input);
		String title = input.getName();
		setPartName(title);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);		
	}
	
	@Override
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPartName(editor.getTitle());
		setInput(editor.getEditorInput());
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	
	@Override
	public boolean isDirty() {
		
		return false;
	}


	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput)) {
			throw new PartInitException(Messages.ConfigEditor_InvalidEditorInput);
		}
		super.init(site, editorInput);
	}
	
	private void log(String msg) {
		System.out.println(msg);
	}

}
