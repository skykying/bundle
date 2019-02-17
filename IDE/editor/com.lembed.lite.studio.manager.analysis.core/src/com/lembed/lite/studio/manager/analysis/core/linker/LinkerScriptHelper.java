package com.lembed.lite.studio.manager.analysis.core.linker;

import java.util.List;

public class LinkerScriptHelper {

	public List<LinkerScriptElement> composeAll(String content) {

		List<LinkerScriptElement> slist = null;
		try {
			slist = TranslationUnitVisitor.parser(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return slist; 
	}
	
}
