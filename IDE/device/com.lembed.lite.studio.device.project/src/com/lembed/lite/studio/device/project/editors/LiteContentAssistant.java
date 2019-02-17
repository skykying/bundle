package com.lembed.lite.studio.device.project.editors;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.widgets.Shell;

public class LiteContentAssistant extends ContentAssistant {
    
    public LiteContentAssistant() {
        super();
        
        LiteContentAssistProcessor processor= new LiteContentAssistProcessor(); 
        setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
    
        enableAutoActivation(true);
        enableAutoInsert(true);
        
        setInformationControlCreator(getInformationControlCreator());   
    }

    private IInformationControlCreator getInformationControlCreator() {
        return new IInformationControlCreator() {
            public IInformationControl createInformationControl(Shell parent) {
                return new DefaultInformationControl(parent);
            }
        };
    }
}