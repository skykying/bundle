package com.lembed.lite.studio.manager.analysis.editor.map.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;


public class MapParser {

	public static final String ArchiveIndicator = "Archive member included to satisfy";
	public static final String AllocationIndicator = "Allocating common symbols";
	public static final String DiscardedIndicator = "Discarded input sections";
	public static final String MemoryIndicator = "Memory Configuration";
	public static final String LinkerIndicator = "Linker script and memory map";
	public static final String OutputIndicator = "OUTPUT(";
	
	
	public static final String ArchiveTag = "Archive symbols";
	public static final String AllocationTag = "Allocating symbols";
	public static final String DiscardedTag = "Discarded sections";
	public static final String MemoryTag = "Memory Configuration";
	public static final String LinkerTag = "Linker map";
	public static final String OutputTag = "Output format";
	
	private IDocument document = null;
	private FindReplaceDocumentAdapter finder =null;
	
	
	public MapParser(IDocument doc) {
		document = doc;
		finder = new FindReplaceDocumentAdapter(document);
	}
	
	
	class CompareObj implements Comparable<CompareObj> {
		public String tag = null;
		public String value = null;
		public int offset = 0;
		public int length = 0;
		
		public CompareObj(String tag, String value, int offset, int length) {
			super();
			this.tag = tag;
			this.value = value;
			this.offset = offset;
			this.length = length;
		}

		@Override
		public int compareTo(CompareObj o) {
			if (this.offset > o.offset) {
				return 1;
			}
			
			if (this.offset < o.offset) {
				return -1;
			} else {		
				return 0;
			}
		}
	}
	
	public CompareObj offsetFinder(String content,String tag) {
		
		try {
			IRegion region = finder.find(0, content, true, true, true, false);
			if(region != null) {
				CompareObj so = new CompareObj(tag, content, region.getOffset(), region.getLength());
				return so;
			}

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		return null;		
	}
	
	public List<CompareObj> listAll() {
		List<CompareObj> sortList = new LinkedList<CompareObj>();
		
		CompareObj er = offsetFinder(ArchiveIndicator,ArchiveTag);
		if(er != null) {
			sortList.add(er);
		}
		
		CompareObj el = offsetFinder(AllocationIndicator,AllocationTag);
		if(el != null) {
			sortList.add(el);
		}
		
		CompareObj ed = offsetFinder(DiscardedIndicator,DiscardedTag);
		if(ed != null) {
			sortList.add(ed);
		}
		
		CompareObj em = offsetFinder(MemoryIndicator,MemoryTag);
		if(em != null) {
			sortList.add(em);
		}
		
		CompareObj ek = offsetFinder(LinkerIndicator,LinkerTag);
		if(ek != null) {
			sortList.add(ek);
		}
		
		Collections.sort(sortList);
		
		return sortList;
	}

	public List<GccMapElement> composeAll() {
		
		List<GccMapElement> all = new LinkedList<GccMapElement>();
		
		List<CompareObj> sortList = listAll();
		
		for(CompareObj obj : sortList) {
			log(obj.tag);
			
			int index  = sortList.indexOf(obj);
			int offset = obj.offset;
			int nextOffset = 0;
			
			if(index+1 == sortList.size()) {
				nextOffset = document.getLength();
			}else {
				nextOffset = sortList.get(index+1).offset;
			}
			
			int length = nextOffset - offset;
			
			Position p = new Position(offset,length);
			GccMapElement ge = new GccMapElement(p, obj.tag, obj.value);
			if(ge != null) {
				all.add(ge);
			}
		}
		
		for(GccMapElement me: all) {
			if(me.getTag().equals(LinkerTag)) {
				GccMapElement gc = getElement(OutputIndicator,OutputTag);
				if(gc!=null) {
					me.addChild(gc);
				}
			}
		}
		
		return all;
	}

	public GccMapElement getElement(String seg,String tag) {
		Position p = null;
		
		try {
			IRegion region = finder.find(0, seg, true, true, true, false);
			if(region != null) {
				p= new Position(region.getOffset(),region.getLength());
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		if(p!=null) {
			return new GccMapElement(p, tag, seg);
		}else {
			return null;
		}
	}
	
	
	private void log(String msg) {
		System.out.println(msg);
	}
}
