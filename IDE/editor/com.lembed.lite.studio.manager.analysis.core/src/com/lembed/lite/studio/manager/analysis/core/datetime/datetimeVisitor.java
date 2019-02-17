package com.lembed.lite.studio.manager.analysis.core.datetime;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link datetimeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface datetimeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link datetimeParser#date_time}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate_time(datetimeParser.Date_timeContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#day}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDay(datetimeParser.DayContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#date}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate(datetimeParser.DateContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#month}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonth(datetimeParser.MonthContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#time}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTime(datetimeParser.TimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#hour}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHour(datetimeParser.HourContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#zone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZone(datetimeParser.ZoneContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#two_digit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTwo_digit(datetimeParser.Two_digitContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#four_digit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFour_digit(datetimeParser.Four_digitContext ctx);
	/**
	 * Visit a parse tree produced by {@link datetimeParser#alphanumeric}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlphanumeric(datetimeParser.AlphanumericContext ctx);
}