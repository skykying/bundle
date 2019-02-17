package com.lembed.lite.studio.manager.analysis.editor.map.antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link mapParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface mapVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link mapParser#symbols}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbols(mapParser.SymbolsContext ctx);
}