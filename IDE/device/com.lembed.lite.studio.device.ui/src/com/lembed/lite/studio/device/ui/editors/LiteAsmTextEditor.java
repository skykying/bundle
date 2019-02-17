package com.lembed.lite.studio.device.ui.editors;

import org.eclipse.cdt.internal.ui.editor.asm.AsmTextEditor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

@SuppressWarnings("restriction")
public class LiteAsmTextEditor extends AsmTextEditor {

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.internal.ui.editor.asm.AsmTextEditor#initializeEditor()
	 */
	@Override
	protected void initializeEditor() {
		// TODO Auto-generated method stub
		super.initializeEditor();
		
		SourceViewerConfiguration asc = createSourceViewConfiguration();
		if(asc != null){
			setSourceViewerConfiguration(asc);
		}		
	}
	
	
	private SourceViewerConfiguration createSourceViewConfiguration(){
		SourceViewerConfiguration svc = getSourceViewerConfiguration();
		
		ISourceViewer sourceViewer = getSourceViewer();
		if(sourceViewer != null){
			svc.getContentAssistant(sourceViewer);
		}
		
		return svc;
	}

}
