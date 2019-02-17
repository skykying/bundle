package com.lembed.lite.studio.manager.analysis.editor.elf;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.lembed.lite.studio.manager.analysis.editor.color.ColorManager;
import com.lembed.lite.studio.manager.analysis.editor.elf.ui.ElfContentOutlinePage;

public class ElfTextEditor extends TextEditor {
	
	private ColorManager colorManager;
	private ElfContentOutlinePage contentOutline;

	public ElfTextEditor() {
		super();
		colorManager = new ColorManager();
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(@SuppressWarnings("rawtypes") Class required) {

		if (IContentOutlinePage.class.equals(required)) {
			if (contentOutline == null) {
				contentOutline = new ElfContentOutlinePage(getDocumentProvider(), this );
				if (getEditorInput() != null){
					contentOutline.setInput(getEditorInput());
				}
					
			}
			return contentOutline;
		}

		return super.getAdapter(required);
	}

}
