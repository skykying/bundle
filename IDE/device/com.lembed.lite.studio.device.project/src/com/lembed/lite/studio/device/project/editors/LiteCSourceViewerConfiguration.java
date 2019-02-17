package com.lembed.lite.studio.device.project.editors;

import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class LiteCSourceViewerConfiguration extends SourceViewerConfiguration {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		
		System.out.println("LiteCSourceViewerConfiguration \n");
		
		// TODO Auto-generated method stub
//		return super.getContentAssistant(sourceViewer);
		
		return new LiteContentAssistant();
	}
	
	

}
