package com.lembed.lite.studio.manager.analysis.core.linker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.EntryDefinitionContext;
import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.MemoryArrayDefinitionContext;
import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.MemoryDefinitionContext;
import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.MemoryLengthContext;
import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.ScriptExpressContext;
import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.SectionDefinitionContext;
import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.SectionsDefinitionContext;
import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.TranslationUnitContext;

public class TranslationUnitVisitor extends LinkerScriptBaseVisitor<String> {

	private LinkedList<LinkerScriptElement> elist = new LinkedList<LinkerScriptElement>();
	
	@Override 
	public String visitTranslationUnit(TranslationUnitContext ctx) {	
		return visitChildren(ctx);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptBaseVisitor#visitMemoryArrayDefinition(com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.MemoryArrayDefinitionContext)
	 */
	@Override
	public String visitMemoryArrayDefinition(MemoryArrayDefinitionContext ctx) {
		String name = ctx.Memory().toString();
		String value = ctx.getPayload().getText();
		int startLine = ctx.getStart().getLine();
		int stopLine = ctx.getStop().getLine();
		
		LinkerScriptElement e = new LinkerScriptElement(name,value,LinkerScriptElement.MEMORY_TYPE,startLine, stopLine);
		e.parent=null;
		addElement(e);
		
		return super.visitMemoryArrayDefinition(ctx);
	}



	/* (non-Javadoc)
	 * @see com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptBaseVisitor#visitMemoryDefinition(com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.MemoryDefinitionContext)
	 */
	@Override
	public String visitMemoryDefinition(MemoryDefinitionContext ctx) {
		String name = ctx.memoryNameWithAttribute().getText();		
		String length = "";
		MemoryLengthContext lengthObj = ctx.memoryLength();
		if(lengthObj != null) {
			length = lengthObj.getText();
		}
		int startLine = ctx.getStart().getLine();
		int stopLine = ctx.getStop().getLine();
		String parentText = ctx.getParent().getText();
		
		LinkerScriptElement e = new LinkerScriptElement(name,length,LinkerScriptElement.NORMAL_TYPE,startLine, stopLine);
		
		for(LinkerScriptElement el: getElements()) {
			String cc = el.getValue();
			if(cc.contains(parentText)) {
				el.addChild(e); 
			}
		}

		return super.visitMemoryDefinition(ctx);
	}



	/* (non-Javadoc)
	 * @see com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptBaseVisitor#visitSectionsDefinition(com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.SectionsDefinitionContext)
	 */
	@Override
	public String visitSectionsDefinition(SectionsDefinitionContext ctx) {
		String name = ctx.Sections().toString();
		String value = ctx.getPayload().getText();
		int startLine = ctx.getStart().getLine();
		int stopLine = ctx.getStop().getLine();
		
		LinkerScriptElement e = new LinkerScriptElement(name,value,LinkerScriptElement.SECTION_TYPE,startLine, stopLine);
		e.parent=null;
		addElement(e);
		
		return super.visitSectionsDefinition(ctx);
	}


	

	/* (non-Javadoc)
	 * @see com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptBaseVisitor#visitScriptExpress(com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.ScriptExpressContext)
	 */
	@Override
	public String visitScriptExpress(ScriptExpressContext ctx) {
		
		LinkedList<String> nameList = new LinkedList<>();
		
		String ctxName = ctx.getText();
		if(ctxName == null) {
			nameList.add("");
		}
		if(ctxName.contains("=")) {
			String[] nameArray = ctxName.split("=");
			if(nameArray != null) {
				for(String name : nameArray) {
					nameList.add(name);
				}
			}
		}else {
			nameList.add(ctxName);
		}
		
		String name = nameList.getFirst();
		if(name.contains("\\(")) {
			int index = name.indexOf("\\(");
			name = name.substring(index+1);
		}
		
		String value = ctx.getPayload().getText();
		int startLine = ctx.getStart().getLine();
		int stopLine = ctx.getStop().getLine();
		String parentText = ctx.getParent().getText();
		
		LinkerScriptElement e = new LinkerScriptElement(name,value,LinkerScriptElement.NORMAL_TYPE,startLine, stopLine);
		
//		for(LinkerScriptElement el: getElements()) {
//			String cc = el.getValue();
//			if(cc.contains(parentText)) {
//			}else {
//				addElement(e);
//			}
//		}
		
		return super.visitScriptExpress(ctx);
	}



	/* (non-Javadoc)
	 * @see com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptBaseVisitor#visitSectionDefinition(com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.SectionDefinitionContext)
	 */
	@Override
	public String visitSectionDefinition(SectionDefinitionContext ctx) {
		String name = ctx.getText().split(":")[0];
		String value = ctx.getPayload().getText();
		int startLine = ctx.getStart().getLine();
		int stopLine = ctx.getStop().getLine();
		String parentText = ctx.getParent().getText();
		
		LinkerScriptElement e = new LinkerScriptElement(name,value,LinkerScriptElement.NORMAL_TYPE,startLine, stopLine);
		e.parent=null;
		
		for(LinkerScriptElement el: getElements()) {
			String cc = el.getValue();
			if(cc.contains(parentText)) {
				el.addChild(e); 
			}
		}
		return super.visitSectionDefinition(ctx);
	}



	/* (non-Javadoc)
	 * @see com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptBaseVisitor#visitEntryDefinition(com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptParser.EntryDefinitionContext)
	 */
	@Override
	public String visitEntryDefinition(EntryDefinitionContext ctx) {
		String name = ctx.Entry().toString();
		String value = ctx.entryName().getText();
		int startLine = ctx.getStart().getLine();
		int stopLine = ctx.getStop().getLine();
		ctx.getParent().getText();
		
		LinkerScriptElement e = new LinkerScriptElement(name,value,LinkerScriptElement.ENTRY_TYPE,startLine, stopLine);
		e.parent=null;
		addElement(e);
		return super.visitEntryDefinition(ctx);
	}


	public synchronized LinkedList<LinkerScriptElement> getElements(){
		return elist;
	}
	
	private synchronized void addElement(LinkerScriptElement e) {
		elist.add(e);
	}

	public static List<LinkerScriptElement> parser(String expr) throws Exception {
		
		ANTLRInputStream in = new ANTLRInputStream(expr);
		LinkerScriptLexer lexer = new LinkerScriptLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		LinkerScriptParser parser = new LinkerScriptParser(tokens);
		ParseTree tree = parser.translationUnit();
		
		TranslationUnitVisitor vis = new TranslationUnitVisitor();
		
		vis.visit(tree);
		LinkedList<LinkerScriptElement> elist = vis.getElements();
		
		return elist;
	}
	
	private void log(String msg){
		System.out.println(msg);
	}
	
}
