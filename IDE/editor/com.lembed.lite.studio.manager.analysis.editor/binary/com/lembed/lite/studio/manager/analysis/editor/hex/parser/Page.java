package com.lembed.lite.studio.manager.analysis.editor.hex.parser;

import java.util.LinkedList;
import java.util.List;

public class Page {

	public FlashMark getStartMark() {		
		return new FlashMark();
	}

	public FlashMark getEndMark() {		
		return new FlashMark();
	}

	public List<Page> getValue() {

		List<Page> pages = new LinkedList<>();
		return pages;
	}

}
