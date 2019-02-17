package com.lembed.lite.studio.manager.analysis.editor.map.antlr;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.lembed.lite.studio.manager.analysis.editor.map.antlr.mapParser.SymbolsContext;

/**
 * This class provides an empty implementation of {@link mapVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class symbolVisitor extends mapBaseVisitor<String>  {

	@Override
	public String visitSymbols(SymbolsContext ctx) {
		// TODO Auto-generated method stub
		return super.visitSymbols(ctx);
	}
	
}