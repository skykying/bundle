package com.lembed.lite.studio.manager.analysis.core.datetime;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class datetimeParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, ALPHA=34, DIGIT=35, WS=36;
	public static final int
		RULE_date_time = 0, RULE_day = 1, RULE_date = 2, RULE_month = 3, RULE_time = 4, 
		RULE_hour = 5, RULE_zone = 6, RULE_two_digit = 7, RULE_four_digit = 8, 
		RULE_alphanumeric = 9;
	public static final String[] ruleNames = {
		"date_time", "day", "date", "month", "time", "hour", "zone", "two_digit", 
		"four_digit", "alphanumeric"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'Mon'", "'Tue'", "'Wed'", "'Thu'", "'Fri'", "'Sat'", "'Sun'", 
		"'Jan'", "'Feb'", "'Mar'", "'Apr'", "'May'", "'Jun'", "'Jul'", "'Aug'", 
		"'Sep'", "'Oct'", "'Nov'", "'Dec'", "':'", "'UT'", "'GMT'", "'EST'", "'EDT'", 
		"'CST'", "'CDT'", "'MST'", "'MDT'", "'PST'", "'PDT'", "'+'", "'-'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, "ALPHA", "DIGIT", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "datetime.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public datetimeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class Date_timeContext extends ParserRuleContext {
		public DateContext date() {
			return getRuleContext(DateContext.class,0);
		}
		public TimeContext time() {
			return getRuleContext(TimeContext.class,0);
		}
		public DayContext day() {
			return getRuleContext(DayContext.class,0);
		}
		public Date_timeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_date_time; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitDate_time(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Date_timeContext date_time() throws RecognitionException {
		Date_timeContext _localctx = new Date_timeContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_date_time);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7))) != 0)) {
				{
				setState(20);
				day();
				setState(21);
				match(T__0);
				}
			}

			setState(25);
			date();
			setState(26);
			time();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DayContext extends ParserRuleContext {
		public DayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_day; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitDay(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DayContext day() throws RecognitionException {
		DayContext _localctx = new DayContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_day);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateContext extends ParserRuleContext {
		public MonthContext month() {
			return getRuleContext(MonthContext.class,0);
		}
		public List<Two_digitContext> two_digit() {
			return getRuleContexts(Two_digitContext.class);
		}
		public Two_digitContext two_digit(int i) {
			return getRuleContext(Two_digitContext.class,i);
		}
		public DateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_date; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitDate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateContext date() throws RecognitionException {
		DateContext _localctx = new DateContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_date);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(30);
				two_digit();
				}
				}
				setState(33); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ALPHA || _la==DIGIT );
			setState(35);
			month();
			setState(36);
			two_digit();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MonthContext extends ParserRuleContext {
		public MonthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_month; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitMonth(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MonthContext month() throws RecognitionException {
		MonthContext _localctx = new MonthContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_month);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TimeContext extends ParserRuleContext {
		public HourContext hour() {
			return getRuleContext(HourContext.class,0);
		}
		public ZoneContext zone() {
			return getRuleContext(ZoneContext.class,0);
		}
		public TimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_time; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimeContext time() throws RecognitionException {
		TimeContext _localctx = new TimeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_time);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			hour();
			setState(41);
			zone();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HourContext extends ParserRuleContext {
		public List<Two_digitContext> two_digit() {
			return getRuleContexts(Two_digitContext.class);
		}
		public Two_digitContext two_digit(int i) {
			return getRuleContext(Two_digitContext.class,i);
		}
		public HourContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hour; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitHour(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HourContext hour() throws RecognitionException {
		HourContext _localctx = new HourContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_hour);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			two_digit();
			setState(44);
			match(T__20);
			setState(45);
			two_digit();
			setState(48);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(46);
				match(T__20);
				setState(47);
				two_digit();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ZoneContext extends ParserRuleContext {
		public TerminalNode ALPHA() { return getToken(datetimeParser.ALPHA, 0); }
		public Four_digitContext four_digit() {
			return getRuleContext(Four_digitContext.class,0);
		}
		public ZoneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_zone; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitZone(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ZoneContext zone() throws RecognitionException {
		ZoneContext _localctx = new ZoneContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_zone);
		int _la;
		try {
			setState(63);
			switch (_input.LA(1)) {
			case T__21:
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				match(T__21);
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				match(T__22);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				match(T__23);
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 4);
				{
				setState(53);
				match(T__24);
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 5);
				{
				setState(54);
				match(T__25);
				}
				break;
			case T__26:
				enterOuterAlt(_localctx, 6);
				{
				setState(55);
				match(T__26);
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 7);
				{
				setState(56);
				match(T__27);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 8);
				{
				setState(57);
				match(T__28);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 9);
				{
				setState(58);
				match(T__29);
				}
				break;
			case T__30:
				enterOuterAlt(_localctx, 10);
				{
				setState(59);
				match(T__30);
				}
				break;
			case ALPHA:
				enterOuterAlt(_localctx, 11);
				{
				setState(60);
				match(ALPHA);
				}
				break;
			case T__31:
			case T__32:
				enterOuterAlt(_localctx, 12);
				{
				{
				setState(61);
				_la = _input.LA(1);
				if ( !(_la==T__31 || _la==T__32) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(62);
				four_digit();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Two_digitContext extends ParserRuleContext {
		public List<AlphanumericContext> alphanumeric() {
			return getRuleContexts(AlphanumericContext.class);
		}
		public AlphanumericContext alphanumeric(int i) {
			return getRuleContext(AlphanumericContext.class,i);
		}
		public Two_digitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_two_digit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitTwo_digit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Two_digitContext two_digit() throws RecognitionException {
		Two_digitContext _localctx = new Two_digitContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_two_digit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			alphanumeric();
			setState(66);
			alphanumeric();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Four_digitContext extends ParserRuleContext {
		public List<AlphanumericContext> alphanumeric() {
			return getRuleContexts(AlphanumericContext.class);
		}
		public AlphanumericContext alphanumeric(int i) {
			return getRuleContext(AlphanumericContext.class,i);
		}
		public Four_digitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_four_digit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitFour_digit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Four_digitContext four_digit() throws RecognitionException {
		Four_digitContext _localctx = new Four_digitContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_four_digit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			alphanumeric();
			setState(69);
			alphanumeric();
			setState(70);
			alphanumeric();
			setState(71);
			alphanumeric();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AlphanumericContext extends ParserRuleContext {
		public TerminalNode ALPHA() { return getToken(datetimeParser.ALPHA, 0); }
		public TerminalNode DIGIT() { return getToken(datetimeParser.DIGIT, 0); }
		public AlphanumericContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alphanumeric; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof datetimeVisitor ) return ((datetimeVisitor<? extends T>)visitor).visitAlphanumeric(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlphanumericContext alphanumeric() throws RecognitionException {
		AlphanumericContext _localctx = new AlphanumericContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_alphanumeric);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			_la = _input.LA(1);
			if ( !(_la==ALPHA || _la==DIGIT) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3&N\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3\2"+
		"\3\2\3\2\5\2\32\n\2\3\2\3\2\3\2\3\3\3\3\3\4\6\4\"\n\4\r\4\16\4#\3\4\3"+
		"\4\3\4\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7\63\n\7\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bB\n\b\3\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\n\3\n\3\13\3\13\3\13\2\2\f\2\4\6\b\n\f\16\20\22\24\2\6\3\2\4\n"+
		"\3\2\13\26\3\2\"#\3\2$%Q\2\31\3\2\2\2\4\36\3\2\2\2\6!\3\2\2\2\b(\3\2\2"+
		"\2\n*\3\2\2\2\f-\3\2\2\2\16A\3\2\2\2\20C\3\2\2\2\22F\3\2\2\2\24K\3\2\2"+
		"\2\26\27\5\4\3\2\27\30\7\3\2\2\30\32\3\2\2\2\31\26\3\2\2\2\31\32\3\2\2"+
		"\2\32\33\3\2\2\2\33\34\5\6\4\2\34\35\5\n\6\2\35\3\3\2\2\2\36\37\t\2\2"+
		"\2\37\5\3\2\2\2 \"\5\20\t\2! \3\2\2\2\"#\3\2\2\2#!\3\2\2\2#$\3\2\2\2$"+
		"%\3\2\2\2%&\5\b\5\2&\'\5\20\t\2\'\7\3\2\2\2()\t\3\2\2)\t\3\2\2\2*+\5\f"+
		"\7\2+,\5\16\b\2,\13\3\2\2\2-.\5\20\t\2./\7\27\2\2/\62\5\20\t\2\60\61\7"+
		"\27\2\2\61\63\5\20\t\2\62\60\3\2\2\2\62\63\3\2\2\2\63\r\3\2\2\2\64B\7"+
		"\30\2\2\65B\7\31\2\2\66B\7\32\2\2\67B\7\33\2\28B\7\34\2\29B\7\35\2\2:"+
		"B\7\36\2\2;B\7\37\2\2<B\7 \2\2=B\7!\2\2>B\7$\2\2?@\t\4\2\2@B\5\22\n\2"+
		"A\64\3\2\2\2A\65\3\2\2\2A\66\3\2\2\2A\67\3\2\2\2A8\3\2\2\2A9\3\2\2\2A"+
		":\3\2\2\2A;\3\2\2\2A<\3\2\2\2A=\3\2\2\2A>\3\2\2\2A?\3\2\2\2B\17\3\2\2"+
		"\2CD\5\24\13\2DE\5\24\13\2E\21\3\2\2\2FG\5\24\13\2GH\5\24\13\2HI\5\24"+
		"\13\2IJ\5\24\13\2J\23\3\2\2\2KL\t\5\2\2L\25\3\2\2\2\6\31#\62A";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}