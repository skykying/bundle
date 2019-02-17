package com.lembed.lite.studio.manager.analysis.core.linker;

// Generated from LinkerScript.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LinkerScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LinkerScriptVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(LinkerScriptParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(LinkerScriptParser.AssignmentOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#linkerExpress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkerExpress(LinkerScriptParser.LinkerExpressContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#scriptExpress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScriptExpress(LinkerScriptParser.ScriptExpressContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#groupDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupDefinition(LinkerScriptParser.GroupDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#groupElementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupElementList(LinkerScriptParser.GroupElementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#groupElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupElement(LinkerScriptParser.GroupElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#memoryArrayDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoryArrayDefinition(LinkerScriptParser.MemoryArrayDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#memoryDefinitionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoryDefinitionList(LinkerScriptParser.MemoryDefinitionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#memoryDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoryDefinition(LinkerScriptParser.MemoryDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#memoryNameWithAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoryNameWithAttribute(LinkerScriptParser.MemoryNameWithAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#memoryOrigin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoryOrigin(LinkerScriptParser.MemoryOriginContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#memoryLength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemoryLength(LinkerScriptParser.MemoryLengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#sectionsDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSectionsDefinition(LinkerScriptParser.SectionsDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#sectionDefinitionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSectionDefinitionList(LinkerScriptParser.SectionDefinitionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#sectionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSectionDefinition(LinkerScriptParser.SectionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#sectionExpressList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSectionExpressList(LinkerScriptParser.SectionExpressListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#sectionExpress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSectionExpress(LinkerScriptParser.SectionExpressContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#commonExpress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonExpress(LinkerScriptParser.CommonExpressContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#keepDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeepDefinition(LinkerScriptParser.KeepDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#genericExpressList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericExpressList(LinkerScriptParser.GenericExpressListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#genericExpress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericExpress(LinkerScriptParser.GenericExpressContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#genericStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericStatement(LinkerScriptParser.GenericStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#sectionHeader}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSectionHeader(LinkerScriptParser.SectionHeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#sectionFoot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSectionFoot(LinkerScriptParser.SectionFootContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#entryDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryDefinition(LinkerScriptParser.EntryDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#entryName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryName(LinkerScriptParser.EntryNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#linkerAssignmentDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkerAssignmentDefinition(LinkerScriptParser.LinkerAssignmentDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#linkerAssignmentExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkerAssignmentExpression(LinkerScriptParser.LinkerAssignmentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#rawExpress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRawExpress(LinkerScriptParser.RawExpressContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#noloadExpress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoloadExpress(LinkerScriptParser.NoloadExpressContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#linkerStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkerStatement(LinkerScriptParser.LinkerStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#linkerOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkerOp(LinkerScriptParser.LinkerOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#provideDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProvideDefinition(LinkerScriptParser.ProvideDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#provideSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProvideSpecifier(LinkerScriptParser.ProvideSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#provideHiddenDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProvideHiddenDefinition(LinkerScriptParser.ProvideHiddenDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#provideHiddenSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProvideHiddenSpecifier(LinkerScriptParser.ProvideHiddenSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#fillDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFillDefinition(LinkerScriptParser.FillDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(LinkerScriptParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#translationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTranslationUnit(LinkerScriptParser.TranslationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link LinkerScriptParser#externalDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternalDeclaration(LinkerScriptParser.ExternalDeclarationContext ctx);
}