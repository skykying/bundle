package com.lembed.lite.studio.device.project.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import com.lembed.lite.studio.device.parser.ConfigWizardParser;
import com.lembed.lite.studio.device.parser.ConfigWizardScanner;

public class LiteConfigMatchingStrategy implements IEditorMatchingStrategy {

	private ConfigWizardScanner scanner = null;
	private ConfigWizardParser fParser;
	
	@Override
	public boolean matches(IEditorReference editorRef, IEditorInput input) {

//		try {
//			return scan(input);
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
		return false;
	}

	
	public boolean scan(IEditorInput input) throws CoreException {
		scanner = new ConfigWizardScanner(false);
		if(scanner == null){
			return false;
		}
		
		FileDocumentProvider documentProvider = new FileDocumentProvider();
		documentProvider.connect(input);
		IDocument document = documentProvider.getDocument(input);
		
		fParser = new ConfigWizardParser(scanner, document);
		if (fParser == null) {
			return false;
		}

		if (fParser.containWizard()) {
			return true;
		}

		return false;
	}
}
