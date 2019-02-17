package com.lembed.lite.studio.manager.analysis.core.linker;

// Generated from LinkerScript.g4 by ANTLR 4.5
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LinkerScriptParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, LinkerDigit=6, MemoryAttributs=7, 
		Sections=8, Group=9, Memory=10, Origin=11, Length=12, Provide=13, Entry=14, 
		Align=15, Fill=16, Absolute=17, Keep=18, LinkLong=19, Address=20, ProvideHidden=21, 
		Sort=22, At=23, LoadAddress=24, Noload=25, Common=26, LeftParen=27, RightParen=28, 
		LeftBracket=29, RightBracket=30, LeftBrace=31, RightBrace=32, Less=33, 
		LessEqual=34, Greater=35, GreaterEqual=36, LeftShift=37, RightShift=38, 
		PlusPlus=39, Minus=40, MinusMinus=41, Star=42, Div=43, Mod=44, And=45, 
		Or=46, AndAnd=47, OrOr=48, Caret=49, Not=50, Tilde=51, Question=52, Colon=53, 
		Semi=54, Comma=55, Assign=56, StarAssign=57, DivAssign=58, ModAssign=59, 
		PlusAssign=60, MinusAssign=61, LeftShiftAssign=62, RightShiftAssign=63, 
		AndAssign=64, XorAssign=65, OrAssign=66, Equal=67, NotEqual=68, Arrow=69, 
		Dot=70, Ellipsis=71, Identifier=72, Constant=73, StringLiteral=74, GroupBlock=75, 
		Whitespace=76, Newline=77, BlockComment=78, LineComment=79;
	public static final int
		RULE_primaryExpression = 0, RULE_assignmentOperator = 1, RULE_linkerExpress = 2, 
		RULE_scriptExpress = 3, RULE_groupDefinition = 4, RULE_groupElementList = 5, 
		RULE_groupElement = 6, RULE_memoryArrayDefinition = 7, RULE_memoryDefinitionList = 8, 
		RULE_memoryDefinition = 9, RULE_memoryNameWithAttribute = 10, RULE_memoryOrigin = 11, 
		RULE_memoryLength = 12, RULE_sectionsDefinition = 13, RULE_sectionDefinitionList = 14, 
		RULE_sectionDefinition = 15, RULE_sectionExpressList = 16, RULE_sectionExpress = 17, 
		RULE_commonExpress = 18, RULE_keepDefinition = 19, RULE_genericExpressList = 20, 
		RULE_genericExpress = 21, RULE_genericStatement = 22, RULE_sectionHeader = 23, 
		RULE_sectionFoot = 24, RULE_entryDefinition = 25, RULE_entryName = 26, 
		RULE_linkerAssignmentDefinition = 27, RULE_linkerAssignmentExpression = 28, 
		RULE_rawExpress = 29, RULE_noloadExpress = 30, RULE_linkerStatement = 31, 
		RULE_linkerOp = 32, RULE_provideDefinition = 33, RULE_provideSpecifier = 34, 
		RULE_provideHiddenDefinition = 35, RULE_provideHiddenSpecifier = 36, RULE_fillDefinition = 37, 
		RULE_compilationUnit = 38, RULE_translationUnit = 39, RULE_externalDeclaration = 40;
	public static final String[] ruleNames = {
		"primaryExpression", "assignmentOperator", "linkerExpress", "scriptExpress", 
		"groupDefinition", "groupElementList", "groupElement", "memoryArrayDefinition", 
		"memoryDefinitionList", "memoryDefinition", "memoryNameWithAttribute", 
		"memoryOrigin", "memoryLength", "sectionsDefinition", "sectionDefinitionList", 
		"sectionDefinition", "sectionExpressList", "sectionExpress", "commonExpress", 
		"keepDefinition", "genericExpressList", "genericExpress", "genericStatement", 
		"sectionHeader", "sectionFoot", "entryDefinition", "entryName", "linkerAssignmentDefinition", 
		"linkerAssignmentExpression", "rawExpress", "noloadExpress", "linkerStatement", 
		"linkerOp", "provideDefinition", "provideSpecifier", "provideHiddenDefinition", 
		"provideHiddenSpecifier", "fillDefinition", "compilationUnit", "translationUnit", 
		"externalDeclaration"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'groupElement'", "'0'", "'*(COMMON)'", "'+'", "'SIZEOF'", null, 
		null, "'SECTIONS'", "'GROUP'", "'MEMORY'", "'ORIGIN'", "'LENGTH'", "'PROVIDE'", 
		"'ENTRY'", "'ALIGN'", "'FILL'", "'ABSOLUTE'", "'KEEP'", "'LONG'", "'ADDR'", 
		"'PROVIDE_HIDDEN'", "'SORT'", "'AT'", "'LOADADDR'", "'NOLOAD'", "'COMMON'", 
		"'('", "')'", "'['", "']'", "'{'", "'}'", "'<'", "'<='", "'>'", "'>='", 
		"'<<'", "'>>'", "'++'", "'-'", "'--'", "'*'", "'/'", "'%'", "'&'", "'|'", 
		"'&&'", "'||'", "'^'", "'!'", "'~'", "'?'", "':'", "';'", "','", "'='", 
		"'*='", "'/='", "'%='", "'+='", "'-='", "'<<='", "'>>='", "'&='", "'^='", 
		"'|='", "'=='", "'!='", "'->'", "'.'", "'...'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, "LinkerDigit", "MemoryAttributs", 
		"Sections", "Group", "Memory", "Origin", "Length", "Provide", "Entry", 
		"Align", "Fill", "Absolute", "Keep", "LinkLong", "Address", "ProvideHidden", 
		"Sort", "At", "LoadAddress", "Noload", "Common", "LeftParen", "RightParen", 
		"LeftBracket", "RightBracket", "LeftBrace", "RightBrace", "Less", "LessEqual", 
		"Greater", "GreaterEqual", "LeftShift", "RightShift", "PlusPlus", "Minus", 
		"MinusMinus", "Star", "Div", "Mod", "And", "Or", "AndAnd", "OrOr", "Caret", 
		"Not", "Tilde", "Question", "Colon", "Semi", "Comma", "Assign", "StarAssign", 
		"DivAssign", "ModAssign", "PlusAssign", "MinusAssign", "LeftShiftAssign", 
		"RightShiftAssign", "AndAssign", "XorAssign", "OrAssign", "Equal", "NotEqual", 
		"Arrow", "Dot", "Ellipsis", "Identifier", "Constant", "StringLiteral", 
		"GroupBlock", "Whitespace", "Newline", "BlockComment", "LineComment"
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
	public String getGrammarFileName() { return "LinkerScript.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LinkerScriptParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class PrimaryExpressionContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LinkerScriptParser.Identifier, 0); }
		public TerminalNode Constant() { return getToken(LinkerScriptParser.Constant, 0); }
		public List<TerminalNode> StringLiteral() { return getTokens(LinkerScriptParser.StringLiteral); }
		public TerminalNode StringLiteral(int i) {
			return getToken(LinkerScriptParser.StringLiteral, i);
		}
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_primaryExpression);
		try {
			int _alt;
			setState(89);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(82);
				match(Identifier);
				}
				break;
			case Constant:
				enterOuterAlt(_localctx, 2);
				{
				setState(83);
				match(Constant);
				}
				break;
			case StringLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(85); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(84);
						match(StringLiteral);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(87); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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

	public static class AssignmentOperatorContext extends ParserRuleContext {
		public AssignmentOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentOperator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitAssignmentOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentOperatorContext assignmentOperator() throws RecognitionException {
		AssignmentOperatorContext _localctx = new AssignmentOperatorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_assignmentOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			_la = _input.LA(1);
			if ( !(((((_la - 56)) & ~0x3f) == 0 && ((1L << (_la - 56)) & ((1L << (Assign - 56)) | (1L << (StarAssign - 56)) | (1L << (DivAssign - 56)) | (1L << (ModAssign - 56)) | (1L << (PlusAssign - 56)) | (1L << (MinusAssign - 56)) | (1L << (LeftShiftAssign - 56)) | (1L << (RightShiftAssign - 56)) | (1L << (AndAssign - 56)) | (1L << (XorAssign - 56)) | (1L << (OrAssign - 56)))) != 0)) ) {
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

	public static class LinkerExpressContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(LinkerScriptParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(LinkerScriptParser.Identifier, i);
		}
		public LinkerExpressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linkerExpress; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitLinkerExpress(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LinkerExpressContext linkerExpress() throws RecognitionException {
		LinkerExpressContext _localctx = new LinkerExpressContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_linkerExpress);
		int _la;
		try {
			int _alt;
			setState(116);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(93);
				match(Identifier);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Dot) {
					{
					{
					setState(94);
					match(Dot);
					}
					}
					setState(99);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(100);
				match(Identifier);
				setState(104);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(101);
						match(Dot);
						}
						} 
					}
					setState(106);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(108);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(107);
					match(Dot);
					}
					break;
				}
				setState(113);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(110);
						match(Identifier);
						}
						} 
					}
					setState(115);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				}
				}
				break;
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

	public static class ScriptExpressContext extends ParserRuleContext {
		public GenericExpressContext genericExpress() {
			return getRuleContext(GenericExpressContext.class,0);
		}
		public LinkerAssignmentDefinitionContext linkerAssignmentDefinition() {
			return getRuleContext(LinkerAssignmentDefinitionContext.class,0);
		}
		public RawExpressContext rawExpress() {
			return getRuleContext(RawExpressContext.class,0);
		}
		public ProvideDefinitionContext provideDefinition() {
			return getRuleContext(ProvideDefinitionContext.class,0);
		}
		public ScriptExpressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scriptExpress; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitScriptExpress(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScriptExpressContext scriptExpress() throws RecognitionException {
		ScriptExpressContext _localctx = new ScriptExpressContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_scriptExpress);
		try {
			setState(122);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(118);
				genericExpress();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(119);
				linkerAssignmentDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(120);
				rawExpress();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(121);
				provideDefinition();
				}
				break;
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

	public static class GroupDefinitionContext extends ParserRuleContext {
		public TerminalNode Group() { return getToken(LinkerScriptParser.Group, 0); }
		public List<GroupElementListContext> groupElementList() {
			return getRuleContexts(GroupElementListContext.class);
		}
		public GroupElementListContext groupElementList(int i) {
			return getRuleContext(GroupElementListContext.class,i);
		}
		public GroupDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitGroupDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupDefinitionContext groupDefinition() throws RecognitionException {
		GroupDefinitionContext _localctx = new GroupDefinitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_groupDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			match(Group);
			setState(125);
			match(LeftParen);
			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(126);
				groupElementList(0);
				}
				}
				setState(131);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(132);
			match(RightParen);
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

	public static class GroupElementListContext extends ParserRuleContext {
		public GroupElementContext groupElement() {
			return getRuleContext(GroupElementContext.class,0);
		}
		public GroupElementListContext groupElementList() {
			return getRuleContext(GroupElementListContext.class,0);
		}
		public GroupElementListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupElementList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitGroupElementList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupElementListContext groupElementList() throws RecognitionException {
		return groupElementList(0);
	}

	private GroupElementListContext groupElementList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		GroupElementListContext _localctx = new GroupElementListContext(_ctx, _parentState);
		GroupElementListContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_groupElementList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(135);
			groupElement();
			}
			_ctx.stop = _input.LT(-1);
			setState(141);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new GroupElementListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_groupElementList);
					setState(137);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(138);
					groupElement();
					}
					} 
				}
				setState(143);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class GroupElementContext extends ParserRuleContext {
		public GroupElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupElement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitGroupElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupElementContext groupElement() throws RecognitionException {
		GroupElementContext _localctx = new GroupElementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_groupElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(T__0);
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

	public static class MemoryArrayDefinitionContext extends ParserRuleContext {
		public TerminalNode Memory() { return getToken(LinkerScriptParser.Memory, 0); }
		public MemoryDefinitionListContext memoryDefinitionList() {
			return getRuleContext(MemoryDefinitionListContext.class,0);
		}
		public MemoryArrayDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryArrayDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitMemoryArrayDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryArrayDefinitionContext memoryArrayDefinition() throws RecognitionException {
		MemoryArrayDefinitionContext _localctx = new MemoryArrayDefinitionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_memoryArrayDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(Memory);
			setState(147);
			match(LeftBrace);
			setState(148);
			memoryDefinitionList(0);
			setState(149);
			match(RightBrace);
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

	public static class MemoryDefinitionListContext extends ParserRuleContext {
		public MemoryDefinitionContext memoryDefinition() {
			return getRuleContext(MemoryDefinitionContext.class,0);
		}
		public MemoryDefinitionListContext memoryDefinitionList() {
			return getRuleContext(MemoryDefinitionListContext.class,0);
		}
		public MemoryDefinitionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryDefinitionList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitMemoryDefinitionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryDefinitionListContext memoryDefinitionList() throws RecognitionException {
		return memoryDefinitionList(0);
	}

	private MemoryDefinitionListContext memoryDefinitionList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		MemoryDefinitionListContext _localctx = new MemoryDefinitionListContext(_ctx, _parentState);
		MemoryDefinitionListContext _prevctx = _localctx;
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_memoryDefinitionList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(152);
			memoryDefinition();
			}
			_ctx.stop = _input.LT(-1);
			setState(158);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new MemoryDefinitionListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_memoryDefinitionList);
					setState(154);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(155);
					memoryDefinition();
					}
					} 
				}
				setState(160);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class MemoryDefinitionContext extends ParserRuleContext {
		public MemoryNameWithAttributeContext memoryNameWithAttribute() {
			return getRuleContext(MemoryNameWithAttributeContext.class,0);
		}
		public MemoryOriginContext memoryOrigin() {
			return getRuleContext(MemoryOriginContext.class,0);
		}
		public MemoryLengthContext memoryLength() {
			return getRuleContext(MemoryLengthContext.class,0);
		}
		public MemoryDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitMemoryDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryDefinitionContext memoryDefinition() throws RecognitionException {
		MemoryDefinitionContext _localctx = new MemoryDefinitionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_memoryDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			memoryNameWithAttribute();
			setState(162);
			match(Colon);
			setState(163);
			memoryOrigin();
			setState(164);
			match(Comma);
			setState(165);
			memoryLength();
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

	public static class MemoryNameWithAttributeContext extends ParserRuleContext {
		public List<LinkerExpressContext> linkerExpress() {
			return getRuleContexts(LinkerExpressContext.class);
		}
		public LinkerExpressContext linkerExpress(int i) {
			return getRuleContext(LinkerExpressContext.class,i);
		}
		public MemoryNameWithAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryNameWithAttribute; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitMemoryNameWithAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryNameWithAttributeContext memoryNameWithAttribute() throws RecognitionException {
		MemoryNameWithAttributeContext _localctx = new MemoryNameWithAttributeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_memoryNameWithAttribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			linkerExpress();
			setState(168);
			match(LeftParen);
			setState(169);
			linkerExpress();
			setState(170);
			match(RightParen);
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

	public static class MemoryOriginContext extends ParserRuleContext {
		public TerminalNode Origin() { return getToken(LinkerScriptParser.Origin, 0); }
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public MemoryOriginContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryOrigin; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitMemoryOrigin(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryOriginContext memoryOrigin() throws RecognitionException {
		MemoryOriginContext _localctx = new MemoryOriginContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_memoryOrigin);
		try {
			setState(178);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(172);
				match(Origin);
				setState(173);
				match(Assign);
				setState(174);
				primaryExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(175);
				match(Origin);
				setState(176);
				match(Assign);
				setState(177);
				match(T__1);
				}
				break;
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

	public static class MemoryLengthContext extends ParserRuleContext {
		public TerminalNode Length() { return getToken(LinkerScriptParser.Length, 0); }
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public MemoryLengthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryLength; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitMemoryLength(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemoryLengthContext memoryLength() throws RecognitionException {
		MemoryLengthContext _localctx = new MemoryLengthContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_memoryLength);
		try {
			setState(186);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(180);
				match(Length);
				setState(181);
				match(Assign);
				setState(182);
				primaryExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(183);
				match(Length);
				setState(184);
				match(Assign);
				setState(185);
				match(T__1);
				}
				break;
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

	public static class SectionsDefinitionContext extends ParserRuleContext {
		public TerminalNode Sections() { return getToken(LinkerScriptParser.Sections, 0); }
		public SectionDefinitionListContext sectionDefinitionList() {
			return getRuleContext(SectionDefinitionListContext.class,0);
		}
		public SectionsDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sectionsDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitSectionsDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SectionsDefinitionContext sectionsDefinition() throws RecognitionException {
		SectionsDefinitionContext _localctx = new SectionsDefinitionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_sectionsDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			match(Sections);
			setState(189);
			match(LeftBrace);
			setState(190);
			sectionDefinitionList(0);
			setState(191);
			match(RightBrace);
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

	public static class SectionDefinitionListContext extends ParserRuleContext {
		public SectionDefinitionContext sectionDefinition() {
			return getRuleContext(SectionDefinitionContext.class,0);
		}
		public SectionDefinitionListContext sectionDefinitionList() {
			return getRuleContext(SectionDefinitionListContext.class,0);
		}
		public SectionDefinitionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sectionDefinitionList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitSectionDefinitionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SectionDefinitionListContext sectionDefinitionList() throws RecognitionException {
		return sectionDefinitionList(0);
	}

	private SectionDefinitionListContext sectionDefinitionList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		SectionDefinitionListContext _localctx = new SectionDefinitionListContext(_ctx, _parentState);
		SectionDefinitionListContext _prevctx = _localctx;
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_sectionDefinitionList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(194);
			sectionDefinition();
			}
			_ctx.stop = _input.LT(-1);
			setState(200);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new SectionDefinitionListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_sectionDefinitionList);
					setState(196);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(197);
					sectionDefinition();
					}
					} 
				}
				setState(202);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class SectionDefinitionContext extends ParserRuleContext {
		public SectionHeaderContext sectionHeader() {
			return getRuleContext(SectionHeaderContext.class,0);
		}
		public SectionExpressListContext sectionExpressList() {
			return getRuleContext(SectionExpressListContext.class,0);
		}
		public List<SectionFootContext> sectionFoot() {
			return getRuleContexts(SectionFootContext.class);
		}
		public SectionFootContext sectionFoot(int i) {
			return getRuleContext(SectionFootContext.class,i);
		}
		public ScriptExpressContext scriptExpress() {
			return getRuleContext(ScriptExpressContext.class,0);
		}
		public SectionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sectionDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitSectionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SectionDefinitionContext sectionDefinition() throws RecognitionException {
		SectionDefinitionContext _localctx = new SectionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_sectionDefinition);
		try {
			int _alt;
			setState(214);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(203);
				sectionHeader();
				setState(204);
				match(LeftBrace);
				setState(205);
				sectionExpressList(0);
				setState(206);
				match(RightBrace);
				setState(210);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(207);
						sectionFoot();
						}
						} 
					}
					setState(212);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(213);
				scriptExpress();
				}
				break;
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

	public static class SectionExpressListContext extends ParserRuleContext {
		public SectionExpressContext sectionExpress() {
			return getRuleContext(SectionExpressContext.class,0);
		}
		public SectionExpressListContext sectionExpressList() {
			return getRuleContext(SectionExpressListContext.class,0);
		}
		public SectionExpressListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sectionExpressList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitSectionExpressList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SectionExpressListContext sectionExpressList() throws RecognitionException {
		return sectionExpressList(0);
	}

	private SectionExpressListContext sectionExpressList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		SectionExpressListContext _localctx = new SectionExpressListContext(_ctx, _parentState);
		SectionExpressListContext _prevctx = _localctx;
		int _startState = 32;
		enterRecursionRule(_localctx, 32, RULE_sectionExpressList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(217);
			sectionExpress();
			}
			_ctx.stop = _input.LT(-1);
			setState(223);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new SectionExpressListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_sectionExpressList);
					setState(219);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(220);
					sectionExpress();
					}
					} 
				}
				setState(225);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class SectionExpressContext extends ParserRuleContext {
		public GenericExpressContext genericExpress() {
			return getRuleContext(GenericExpressContext.class,0);
		}
		public LinkerAssignmentDefinitionContext linkerAssignmentDefinition() {
			return getRuleContext(LinkerAssignmentDefinitionContext.class,0);
		}
		public RawExpressContext rawExpress() {
			return getRuleContext(RawExpressContext.class,0);
		}
		public KeepDefinitionContext keepDefinition() {
			return getRuleContext(KeepDefinitionContext.class,0);
		}
		public FillDefinitionContext fillDefinition() {
			return getRuleContext(FillDefinitionContext.class,0);
		}
		public ProvideHiddenDefinitionContext provideHiddenDefinition() {
			return getRuleContext(ProvideHiddenDefinitionContext.class,0);
		}
		public CommonExpressContext commonExpress() {
			return getRuleContext(CommonExpressContext.class,0);
		}
		public ProvideDefinitionContext provideDefinition() {
			return getRuleContext(ProvideDefinitionContext.class,0);
		}
		public SectionExpressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sectionExpress; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitSectionExpress(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SectionExpressContext sectionExpress() throws RecognitionException {
		SectionExpressContext _localctx = new SectionExpressContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_sectionExpress);
		try {
			setState(234);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(226);
				genericExpress();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(227);
				linkerAssignmentDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(228);
				rawExpress();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(229);
				keepDefinition();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(230);
				fillDefinition();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(231);
				provideHiddenDefinition();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(232);
				commonExpress();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(233);
				provideDefinition();
				}
				break;
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

	public static class CommonExpressContext extends ParserRuleContext {
		public CommonExpressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commonExpress; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitCommonExpress(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommonExpressContext commonExpress() throws RecognitionException {
		CommonExpressContext _localctx = new CommonExpressContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_commonExpress);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(T__2);
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

	public static class KeepDefinitionContext extends ParserRuleContext {
		public GenericExpressContext genericExpress() {
			return getRuleContext(GenericExpressContext.class,0);
		}
		public KeepDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keepDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitKeepDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeepDefinitionContext keepDefinition() throws RecognitionException {
		KeepDefinitionContext _localctx = new KeepDefinitionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_keepDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			match(Keep);
			setState(239);
			match(LeftParen);
			setState(240);
			genericExpress();
			setState(241);
			match(RightParen);
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

	public static class GenericExpressListContext extends ParserRuleContext {
		public GenericExpressContext genericExpress() {
			return getRuleContext(GenericExpressContext.class,0);
		}
		public GenericExpressListContext genericExpressList() {
			return getRuleContext(GenericExpressListContext.class,0);
		}
		public GenericExpressListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericExpressList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitGenericExpressList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GenericExpressListContext genericExpressList() throws RecognitionException {
		return genericExpressList(0);
	}

	private GenericExpressListContext genericExpressList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		GenericExpressListContext _localctx = new GenericExpressListContext(_ctx, _parentState);
		GenericExpressListContext _prevctx = _localctx;
		int _startState = 40;
		enterRecursionRule(_localctx, 40, RULE_genericExpressList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(244);
			genericExpress();
			}
			_ctx.stop = _input.LT(-1);
			setState(250);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new GenericExpressListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_genericExpressList);
					setState(246);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(247);
					genericExpress();
					}
					} 
				}
				setState(252);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class GenericExpressContext extends ParserRuleContext {
		public List<GenericStatementContext> genericStatement() {
			return getRuleContexts(GenericStatementContext.class);
		}
		public GenericStatementContext genericStatement(int i) {
			return getRuleContext(GenericStatementContext.class,i);
		}
		public GenericExpressContext genericExpress() {
			return getRuleContext(GenericExpressContext.class,0);
		}
		public GenericExpressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericExpress; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitGenericExpress(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GenericExpressContext genericExpress() throws RecognitionException {
		GenericExpressContext _localctx = new GenericExpressContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_genericExpress);
		int _la;
		try {
			setState(284);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(253);
				match(Star);
				setState(254);
				match(LeftParen);
				setState(255);
				genericStatement();
				setState(261);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 42)) & ~0x3f) == 0 && ((1L << (_la - 42)) & ((1L << (Star - 42)) | (1L << (Dot - 42)) | (1L << (Identifier - 42)))) != 0)) {
					{
					{
					setState(256);
					genericStatement();
					setState(257);
					match(Star);
					}
					}
					setState(263);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(264);
				match(RightParen);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(266);
				match(Star);
				setState(267);
				match(LeftParen);
				setState(268);
				genericExpress();
				setState(269);
				match(RightParen);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(271);
				match(Sort);
				setState(272);
				match(LeftParen);
				setState(273);
				genericStatement();
				setState(279);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 42)) & ~0x3f) == 0 && ((1L << (_la - 42)) & ((1L << (Star - 42)) | (1L << (Dot - 42)) | (1L << (Identifier - 42)))) != 0)) {
					{
					{
					setState(274);
					genericStatement();
					setState(275);
					match(Star);
					}
					}
					setState(281);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(282);
				match(RightParen);
				}
				break;
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

	public static class GenericStatementContext extends ParserRuleContext {
		public List<LinkerExpressContext> linkerExpress() {
			return getRuleContexts(LinkerExpressContext.class);
		}
		public LinkerExpressContext linkerExpress(int i) {
			return getRuleContext(LinkerExpressContext.class,i);
		}
		public GenericStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitGenericStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GenericStatementContext genericStatement() throws RecognitionException {
		GenericStatementContext _localctx = new GenericStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_genericStatement);
		try {
			int _alt;
			setState(313);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(289);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(286);
						match(Star);
						}
						} 
					}
					setState(291);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
				}
				setState(292);
				linkerExpress();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(293);
				linkerExpress();
				setState(297);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(294);
						match(Star);
						}
						} 
					}
					setState(299);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(301); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(300);
						match(Dot);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(303); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(305);
				linkerExpress();
				setState(310);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(306);
						match(Dot);
						setState(307);
						linkerExpress();
						}
						} 
					}
					setState(312);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				}
				break;
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

	public static class SectionHeaderContext extends ParserRuleContext {
		public GenericStatementContext genericStatement() {
			return getRuleContext(GenericStatementContext.class,0);
		}
		public LinkerStatementContext linkerStatement() {
			return getRuleContext(LinkerStatementContext.class,0);
		}
		public NoloadExpressContext noloadExpress() {
			return getRuleContext(NoloadExpressContext.class,0);
		}
		public SectionHeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sectionHeader; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitSectionHeader(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SectionHeaderContext sectionHeader() throws RecognitionException {
		SectionHeaderContext _localctx = new SectionHeaderContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_sectionHeader);
		int _la;
		try {
			setState(326);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(315);
				genericStatement();
				setState(317);
				_la = _input.LA(1);
				if (_la==LeftParen) {
					{
					setState(316);
					noloadExpress();
					}
				}

				setState(319);
				match(Colon);
				setState(320);
				linkerStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(322);
				genericStatement();
				setState(323);
				match(T__1);
				setState(324);
				match(Colon);
				}
				break;
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

	public static class SectionFootContext extends ParserRuleContext {
		public List<LinkerExpressContext> linkerExpress() {
			return getRuleContexts(LinkerExpressContext.class);
		}
		public LinkerExpressContext linkerExpress(int i) {
			return getRuleContext(LinkerExpressContext.class,i);
		}
		public SectionFootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sectionFoot; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitSectionFoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SectionFootContext sectionFoot() throws RecognitionException {
		SectionFootContext _localctx = new SectionFootContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_sectionFoot);
		int _la;
		try {
			setState(337);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(328);
				match(Greater);
				setState(329);
				linkerExpress();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(332);
				_la = _input.LA(1);
				if (_la==Greater) {
					{
					setState(330);
					match(Greater);
					setState(331);
					linkerExpress();
					}
				}

				setState(334);
				match(At);
				setState(335);
				match(Greater);
				setState(336);
				linkerExpress();
				}
				break;
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

	public static class EntryDefinitionContext extends ParserRuleContext {
		public TerminalNode Entry() { return getToken(LinkerScriptParser.Entry, 0); }
		public EntryNameContext entryName() {
			return getRuleContext(EntryNameContext.class,0);
		}
		public EntryDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entryDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitEntryDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntryDefinitionContext entryDefinition() throws RecognitionException {
		EntryDefinitionContext _localctx = new EntryDefinitionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_entryDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
			match(Entry);
			setState(340);
			match(LeftParen);
			setState(341);
			entryName();
			setState(342);
			match(RightParen);
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

	public static class EntryNameContext extends ParserRuleContext {
		public LinkerExpressContext linkerExpress() {
			return getRuleContext(LinkerExpressContext.class,0);
		}
		public EntryNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entryName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitEntryName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntryNameContext entryName() throws RecognitionException {
		EntryNameContext _localctx = new EntryNameContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_entryName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			linkerExpress();
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

	public static class LinkerAssignmentDefinitionContext extends ParserRuleContext {
		public LinkerAssignmentExpressionContext linkerAssignmentExpression() {
			return getRuleContext(LinkerAssignmentExpressionContext.class,0);
		}
		public LinkerAssignmentDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linkerAssignmentDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitLinkerAssignmentDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LinkerAssignmentDefinitionContext linkerAssignmentDefinition() throws RecognitionException {
		LinkerAssignmentDefinitionContext _localctx = new LinkerAssignmentDefinitionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_linkerAssignmentDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(346);
			linkerAssignmentExpression();
			setState(347);
			match(Semi);
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

	public static class LinkerAssignmentExpressionContext extends ParserRuleContext {
		public List<LinkerExpressContext> linkerExpress() {
			return getRuleContexts(LinkerExpressContext.class);
		}
		public LinkerExpressContext linkerExpress(int i) {
			return getRuleContext(LinkerExpressContext.class,i);
		}
		public List<AssignmentOperatorContext> assignmentOperator() {
			return getRuleContexts(AssignmentOperatorContext.class);
		}
		public AssignmentOperatorContext assignmentOperator(int i) {
			return getRuleContext(AssignmentOperatorContext.class,i);
		}
		public List<LinkerStatementContext> linkerStatement() {
			return getRuleContexts(LinkerStatementContext.class);
		}
		public LinkerStatementContext linkerStatement(int i) {
			return getRuleContext(LinkerStatementContext.class,i);
		}
		public LinkerAssignmentExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linkerAssignmentExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitLinkerAssignmentExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LinkerAssignmentExpressionContext linkerAssignmentExpression() throws RecognitionException {
		LinkerAssignmentExpressionContext _localctx = new LinkerAssignmentExpressionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_linkerAssignmentExpression);
		int _la;
		try {
			int _alt;
			setState(381);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(349);
				linkerExpress();
				setState(350);
				assignmentOperator();
				setState(351);
				linkerExpress();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(353);
				linkerExpress();
				setState(354);
				assignmentOperator();
				setState(367); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(355);
					linkerStatement();
					setState(364);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(359);
							switch (_input.LA(1)) {
							case Assign:
							case StarAssign:
							case DivAssign:
							case ModAssign:
							case PlusAssign:
							case MinusAssign:
							case LeftShiftAssign:
							case RightShiftAssign:
							case AndAssign:
							case XorAssign:
							case OrAssign:
								{
								setState(356);
								assignmentOperator();
								}
								break;
							case T__3:
								{
								setState(357);
								match(T__3);
								}
								break;
							case Minus:
								{
								setState(358);
								match(Minus);
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(361);
							linkerStatement();
							}
							} 
						}
						setState(366);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
					}
					}
					}
					setState(369); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << Origin) | (1L << Length) | (1L << Align) | (1L << Fill) | (1L << Absolute) | (1L << LinkLong) | (1L << Address) | (1L << Sort) | (1L << LoadAddress) | (1L << Star))) != 0) );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(371);
				linkerExpress();
				setState(372);
				assignmentOperator();
				setState(373);
				linkerExpress();
				setState(378);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3 || _la==Minus) {
					{
					{
					setState(374);
					_la = _input.LA(1);
					if ( !(_la==T__3 || _la==Minus) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(375);
					linkerExpress();
					}
					}
					setState(380);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
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

	public static class RawExpressContext extends ParserRuleContext {
		public LinkerAssignmentExpressionContext linkerAssignmentExpression() {
			return getRuleContext(LinkerAssignmentExpressionContext.class,0);
		}
		public List<PrimaryExpressionContext> primaryExpression() {
			return getRuleContexts(PrimaryExpressionContext.class);
		}
		public PrimaryExpressionContext primaryExpression(int i) {
			return getRuleContext(PrimaryExpressionContext.class,i);
		}
		public AssignmentOperatorContext assignmentOperator() {
			return getRuleContext(AssignmentOperatorContext.class,0);
		}
		public LinkerStatementContext linkerStatement() {
			return getRuleContext(LinkerStatementContext.class,0);
		}
		public RawExpressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rawExpress; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitRawExpress(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RawExpressContext rawExpress() throws RecognitionException {
		RawExpressContext _localctx = new RawExpressContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_rawExpress);
		try {
			setState(394);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(383);
				linkerAssignmentExpression();
				setState(384);
				match(Semi);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(386);
				primaryExpression();
				setState(387);
				assignmentOperator();
				setState(388);
				primaryExpression();
				setState(389);
				match(Semi);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(391);
				linkerStatement();
				setState(392);
				match(Semi);
				}
				break;
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

	public static class NoloadExpressContext extends ParserRuleContext {
		public NoloadExpressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noloadExpress; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitNoloadExpress(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoloadExpressContext noloadExpress() throws RecognitionException {
		NoloadExpressContext _localctx = new NoloadExpressContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_noloadExpress);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
			match(LeftParen);
			setState(397);
			match(Noload);
			setState(398);
			match(RightParen);
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

	public static class LinkerStatementContext extends ParserRuleContext {
		public List<LinkerOpContext> linkerOp() {
			return getRuleContexts(LinkerOpContext.class);
		}
		public LinkerOpContext linkerOp(int i) {
			return getRuleContext(LinkerOpContext.class,i);
		}
		public LinkerExpressContext linkerExpress() {
			return getRuleContext(LinkerExpressContext.class,0);
		}
		public List<TerminalNode> LinkerDigit() { return getTokens(LinkerScriptParser.LinkerDigit); }
		public TerminalNode LinkerDigit(int i) {
			return getToken(LinkerScriptParser.LinkerDigit, i);
		}
		public List<LinkerStatementContext> linkerStatement() {
			return getRuleContexts(LinkerStatementContext.class);
		}
		public LinkerStatementContext linkerStatement(int i) {
			return getRuleContext(LinkerStatementContext.class,i);
		}
		public LinkerStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linkerStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitLinkerStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LinkerStatementContext linkerStatement() throws RecognitionException {
		LinkerStatementContext _localctx = new LinkerStatementContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_linkerStatement);
		int _la;
		try {
			setState(427);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(400);
				linkerOp();
				setState(401);
				match(LeftParen);
				setState(402);
				linkerExpress();
				setState(403);
				match(RightParen);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(405);
				linkerOp();
				setState(406);
				match(LeftParen);
				setState(408); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(407);
					match(LinkerDigit);
					}
					}
					setState(410); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==LinkerDigit );
				setState(412);
				match(RightParen);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(414);
				linkerOp();
				setState(415);
				match(LeftParen);
				setState(416);
				linkerStatement();
				setState(422);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << Origin) | (1L << Length) | (1L << Align) | (1L << Fill) | (1L << Absolute) | (1L << LinkLong) | (1L << Address) | (1L << Sort) | (1L << LoadAddress) | (1L << Star))) != 0)) {
					{
					{
					setState(417);
					linkerOp();
					setState(418);
					linkerStatement();
					}
					}
					setState(424);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(425);
				match(RightParen);
				}
				break;
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

	public static class LinkerOpContext extends ParserRuleContext {
		public LinkerOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linkerOp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitLinkerOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LinkerOpContext linkerOp() throws RecognitionException {
		LinkerOpContext _localctx = new LinkerOpContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_linkerOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << Origin) | (1L << Length) | (1L << Align) | (1L << Fill) | (1L << Absolute) | (1L << LinkLong) | (1L << Address) | (1L << Sort) | (1L << LoadAddress) | (1L << Star))) != 0)) ) {
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

	public static class ProvideDefinitionContext extends ParserRuleContext {
		public ProvideSpecifierContext provideSpecifier() {
			return getRuleContext(ProvideSpecifierContext.class,0);
		}
		public LinkerAssignmentExpressionContext linkerAssignmentExpression() {
			return getRuleContext(LinkerAssignmentExpressionContext.class,0);
		}
		public ProvideDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_provideDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitProvideDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProvideDefinitionContext provideDefinition() throws RecognitionException {
		ProvideDefinitionContext _localctx = new ProvideDefinitionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_provideDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			provideSpecifier();
			setState(432);
			match(LeftParen);
			setState(433);
			linkerAssignmentExpression();
			setState(434);
			match(RightParen);
			setState(435);
			match(Semi);
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

	public static class ProvideSpecifierContext extends ParserRuleContext {
		public TerminalNode Provide() { return getToken(LinkerScriptParser.Provide, 0); }
		public ProvideSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_provideSpecifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitProvideSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProvideSpecifierContext provideSpecifier() throws RecognitionException {
		ProvideSpecifierContext _localctx = new ProvideSpecifierContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_provideSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(437);
			match(Provide);
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

	public static class ProvideHiddenDefinitionContext extends ParserRuleContext {
		public ProvideHiddenSpecifierContext provideHiddenSpecifier() {
			return getRuleContext(ProvideHiddenSpecifierContext.class,0);
		}
		public LinkerAssignmentExpressionContext linkerAssignmentExpression() {
			return getRuleContext(LinkerAssignmentExpressionContext.class,0);
		}
		public ProvideHiddenDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_provideHiddenDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitProvideHiddenDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProvideHiddenDefinitionContext provideHiddenDefinition() throws RecognitionException {
		ProvideHiddenDefinitionContext _localctx = new ProvideHiddenDefinitionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_provideHiddenDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			provideHiddenSpecifier();
			setState(440);
			match(LeftParen);
			setState(441);
			linkerAssignmentExpression();
			setState(442);
			match(RightParen);
			setState(443);
			match(Semi);
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

	public static class ProvideHiddenSpecifierContext extends ParserRuleContext {
		public TerminalNode ProvideHidden() { return getToken(LinkerScriptParser.ProvideHidden, 0); }
		public ProvideHiddenSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_provideHiddenSpecifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitProvideHiddenSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProvideHiddenSpecifierContext provideHiddenSpecifier() throws RecognitionException {
		ProvideHiddenSpecifierContext _localctx = new ProvideHiddenSpecifierContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_provideHiddenSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(445);
			match(ProvideHidden);
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

	public static class FillDefinitionContext extends ParserRuleContext {
		public TerminalNode Fill() { return getToken(LinkerScriptParser.Fill, 0); }
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public FillDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fillDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitFillDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FillDefinitionContext fillDefinition() throws RecognitionException {
		FillDefinitionContext _localctx = new FillDefinitionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_fillDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			match(Fill);
			setState(448);
			match(LeftParen);
			setState(449);
			primaryExpression();
			setState(450);
			match(RightParen);
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

	public static class CompilationUnitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(LinkerScriptParser.EOF, 0); }
		public TranslationUnitContext translationUnit() {
			return getRuleContext(TranslationUnitContext.class,0);
		}
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitCompilationUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_compilationUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(453);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << Sections) | (1L << Group) | (1L << Memory) | (1L << Origin) | (1L << Length) | (1L << Provide) | (1L << Entry) | (1L << Align) | (1L << Fill) | (1L << Absolute) | (1L << LinkLong) | (1L << Address) | (1L << Sort) | (1L << LoadAddress) | (1L << Star) | (1L << Semi) | (1L << Assign) | (1L << StarAssign) | (1L << DivAssign) | (1L << ModAssign) | (1L << PlusAssign) | (1L << MinusAssign) | (1L << LeftShiftAssign) | (1L << RightShiftAssign))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (AndAssign - 64)) | (1L << (XorAssign - 64)) | (1L << (OrAssign - 64)) | (1L << (Dot - 64)) | (1L << (Identifier - 64)) | (1L << (Constant - 64)) | (1L << (StringLiteral - 64)))) != 0)) {
				{
				setState(452);
				translationUnit(0);
				}
			}

			setState(455);
			match(EOF);
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

	public static class TranslationUnitContext extends ParserRuleContext {
		public ExternalDeclarationContext externalDeclaration() {
			return getRuleContext(ExternalDeclarationContext.class,0);
		}
		public TranslationUnitContext translationUnit() {
			return getRuleContext(TranslationUnitContext.class,0);
		}
		public TranslationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_translationUnit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitTranslationUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TranslationUnitContext translationUnit() throws RecognitionException {
		return translationUnit(0);
	}

	private TranslationUnitContext translationUnit(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TranslationUnitContext _localctx = new TranslationUnitContext(_ctx, _parentState);
		TranslationUnitContext _prevctx = _localctx;
		int _startState = 78;
		enterRecursionRule(_localctx, 78, RULE_translationUnit, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(458);
			externalDeclaration();
			}
			_ctx.stop = _input.LT(-1);
			setState(464);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TranslationUnitContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_translationUnit);
					setState(460);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(461);
					externalDeclaration();
					}
					} 
				}
				setState(466);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExternalDeclarationContext extends ParserRuleContext {
		public MemoryArrayDefinitionContext memoryArrayDefinition() {
			return getRuleContext(MemoryArrayDefinitionContext.class,0);
		}
		public GroupDefinitionContext groupDefinition() {
			return getRuleContext(GroupDefinitionContext.class,0);
		}
		public SectionsDefinitionContext sectionsDefinition() {
			return getRuleContext(SectionsDefinitionContext.class,0);
		}
		public EntryDefinitionContext entryDefinition() {
			return getRuleContext(EntryDefinitionContext.class,0);
		}
		public ScriptExpressContext scriptExpress() {
			return getRuleContext(ScriptExpressContext.class,0);
		}
		public ExternalDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_externalDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LinkerScriptVisitor ) return ((LinkerScriptVisitor<? extends T>)visitor).visitExternalDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExternalDeclarationContext externalDeclaration() throws RecognitionException {
		ExternalDeclarationContext _localctx = new ExternalDeclarationContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_externalDeclaration);
		try {
			setState(474);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(467);
				memoryArrayDefinition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(468);
				match(Semi);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(469);
				groupDefinition();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(470);
				memoryArrayDefinition();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(471);
				sectionsDefinition();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(472);
				entryDefinition();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(473);
				scriptExpress();
				}
				break;
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 5:
			return groupElementList_sempred((GroupElementListContext)_localctx, predIndex);
		case 8:
			return memoryDefinitionList_sempred((MemoryDefinitionListContext)_localctx, predIndex);
		case 14:
			return sectionDefinitionList_sempred((SectionDefinitionListContext)_localctx, predIndex);
		case 16:
			return sectionExpressList_sempred((SectionExpressListContext)_localctx, predIndex);
		case 20:
			return genericExpressList_sempred((GenericExpressListContext)_localctx, predIndex);
		case 39:
			return translationUnit_sempred((TranslationUnitContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean groupElementList_sempred(GroupElementListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean memoryDefinitionList_sempred(MemoryDefinitionListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean sectionDefinitionList_sempred(SectionDefinitionListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean sectionExpressList_sempred(SectionExpressListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean genericExpressList_sempred(GenericExpressListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean translationUnit_sempred(TranslationUnitContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3Q\u01df\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\3\2\3\2"+
		"\3\2\6\2X\n\2\r\2\16\2Y\5\2\\\n\2\3\3\3\3\3\4\3\4\7\4b\n\4\f\4\16\4e\13"+
		"\4\3\4\3\4\7\4i\n\4\f\4\16\4l\13\4\3\4\5\4o\n\4\3\4\7\4r\n\4\f\4\16\4"+
		"u\13\4\5\4w\n\4\3\5\3\5\3\5\3\5\5\5}\n\5\3\6\3\6\3\6\7\6\u0082\n\6\f\6"+
		"\16\6\u0085\13\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\7\7\u008e\n\7\f\7\16\7\u0091"+
		"\13\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\7\n\u009f\n\n\f"+
		"\n\16\n\u00a2\13\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\5\r\u00b5\n\r\3\16\3\16\3\16\3\16\3\16\3\16\5\16"+
		"\u00bd\n\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\7\20\u00c9"+
		"\n\20\f\20\16\20\u00cc\13\20\3\21\3\21\3\21\3\21\3\21\7\21\u00d3\n\21"+
		"\f\21\16\21\u00d6\13\21\3\21\5\21\u00d9\n\21\3\22\3\22\3\22\3\22\3\22"+
		"\7\22\u00e0\n\22\f\22\16\22\u00e3\13\22\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\5\23\u00ed\n\23\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\26\3\26\7\26\u00fb\n\26\f\26\16\26\u00fe\13\26\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\7\27\u0106\n\27\f\27\16\27\u0109\13\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\7\27\u0118\n\27\f\27"+
		"\16\27\u011b\13\27\3\27\3\27\5\27\u011f\n\27\3\30\7\30\u0122\n\30\f\30"+
		"\16\30\u0125\13\30\3\30\3\30\3\30\7\30\u012a\n\30\f\30\16\30\u012d\13"+
		"\30\3\30\6\30\u0130\n\30\r\30\16\30\u0131\3\30\3\30\3\30\7\30\u0137\n"+
		"\30\f\30\16\30\u013a\13\30\5\30\u013c\n\30\3\31\3\31\5\31\u0140\n\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0149\n\31\3\32\3\32\3\32\3\32"+
		"\5\32\u014f\n\32\3\32\3\32\3\32\5\32\u0154\n\32\3\33\3\33\3\33\3\33\3"+
		"\33\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\5\36\u016a\n\36\3\36\7\36\u016d\n\36\f\36\16\36\u0170\13\36\6"+
		"\36\u0172\n\36\r\36\16\36\u0173\3\36\3\36\3\36\3\36\3\36\7\36\u017b\n"+
		"\36\f\36\16\36\u017e\13\36\5\36\u0180\n\36\3\37\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3\37\3\37\3\37\3\37\5\37\u018d\n\37\3 \3 \3 \3 \3!\3!\3!\3!\3"+
		"!\3!\3!\3!\6!\u019b\n!\r!\16!\u019c\3!\3!\3!\3!\3!\3!\3!\3!\7!\u01a7\n"+
		"!\f!\16!\u01aa\13!\3!\3!\5!\u01ae\n!\3\"\3\"\3#\3#\3#\3#\3#\3#\3$\3$\3"+
		"%\3%\3%\3%\3%\3%\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\5(\u01c8\n(\3(\3(\3)\3)"+
		"\3)\3)\3)\7)\u01d1\n)\f)\16)\u01d4\13)\3*\3*\3*\3*\3*\3*\3*\5*\u01dd\n"+
		"*\3*\2\b\f\22\36\"*P+\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,."+
		"\60\62\64\668:<>@BDFHJLNPR\2\5\3\2:D\4\2\6\6**\t\2\6\7\r\16\21\23\25\26"+
		"\30\30\32\32,,\u01f5\2[\3\2\2\2\4]\3\2\2\2\6v\3\2\2\2\b|\3\2\2\2\n~\3"+
		"\2\2\2\f\u0088\3\2\2\2\16\u0092\3\2\2\2\20\u0094\3\2\2\2\22\u0099\3\2"+
		"\2\2\24\u00a3\3\2\2\2\26\u00a9\3\2\2\2\30\u00b4\3\2\2\2\32\u00bc\3\2\2"+
		"\2\34\u00be\3\2\2\2\36\u00c3\3\2\2\2 \u00d8\3\2\2\2\"\u00da\3\2\2\2$\u00ec"+
		"\3\2\2\2&\u00ee\3\2\2\2(\u00f0\3\2\2\2*\u00f5\3\2\2\2,\u011e\3\2\2\2."+
		"\u013b\3\2\2\2\60\u0148\3\2\2\2\62\u0153\3\2\2\2\64\u0155\3\2\2\2\66\u015a"+
		"\3\2\2\28\u015c\3\2\2\2:\u017f\3\2\2\2<\u018c\3\2\2\2>\u018e\3\2\2\2@"+
		"\u01ad\3\2\2\2B\u01af\3\2\2\2D\u01b1\3\2\2\2F\u01b7\3\2\2\2H\u01b9\3\2"+
		"\2\2J\u01bf\3\2\2\2L\u01c1\3\2\2\2N\u01c7\3\2\2\2P\u01cb\3\2\2\2R\u01dc"+
		"\3\2\2\2T\\\7J\2\2U\\\7K\2\2VX\7L\2\2WV\3\2\2\2XY\3\2\2\2YW\3\2\2\2YZ"+
		"\3\2\2\2Z\\\3\2\2\2[T\3\2\2\2[U\3\2\2\2[W\3\2\2\2\\\3\3\2\2\2]^\t\2\2"+
		"\2^\5\3\2\2\2_w\7J\2\2`b\7H\2\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2"+
		"\2df\3\2\2\2ec\3\2\2\2fj\7J\2\2gi\7H\2\2hg\3\2\2\2il\3\2\2\2jh\3\2\2\2"+
		"jk\3\2\2\2kw\3\2\2\2lj\3\2\2\2mo\7H\2\2nm\3\2\2\2no\3\2\2\2os\3\2\2\2"+
		"pr\7J\2\2qp\3\2\2\2ru\3\2\2\2sq\3\2\2\2st\3\2\2\2tw\3\2\2\2us\3\2\2\2"+
		"v_\3\2\2\2vc\3\2\2\2vn\3\2\2\2w\7\3\2\2\2x}\5,\27\2y}\58\35\2z}\5<\37"+
		"\2{}\5D#\2|x\3\2\2\2|y\3\2\2\2|z\3\2\2\2|{\3\2\2\2}\t\3\2\2\2~\177\7\13"+
		"\2\2\177\u0083\7\35\2\2\u0080\u0082\5\f\7\2\u0081\u0080\3\2\2\2\u0082"+
		"\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0086\3\2"+
		"\2\2\u0085\u0083\3\2\2\2\u0086\u0087\7\36\2\2\u0087\13\3\2\2\2\u0088\u0089"+
		"\b\7\1\2\u0089\u008a\5\16\b\2\u008a\u008f\3\2\2\2\u008b\u008c\f\3\2\2"+
		"\u008c\u008e\5\16\b\2\u008d\u008b\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d"+
		"\3\2\2\2\u008f\u0090\3\2\2\2\u0090\r\3\2\2\2\u0091\u008f\3\2\2\2\u0092"+
		"\u0093\7\3\2\2\u0093\17\3\2\2\2\u0094\u0095\7\f\2\2\u0095\u0096\7!\2\2"+
		"\u0096\u0097\5\22\n\2\u0097\u0098\7\"\2\2\u0098\21\3\2\2\2\u0099\u009a"+
		"\b\n\1\2\u009a\u009b\5\24\13\2\u009b\u00a0\3\2\2\2\u009c\u009d\f\3\2\2"+
		"\u009d\u009f\5\24\13\2\u009e\u009c\3\2\2\2\u009f\u00a2\3\2\2\2\u00a0\u009e"+
		"\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\23\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3"+
		"\u00a4\5\26\f\2\u00a4\u00a5\7\67\2\2\u00a5\u00a6\5\30\r\2\u00a6\u00a7"+
		"\79\2\2\u00a7\u00a8\5\32\16\2\u00a8\25\3\2\2\2\u00a9\u00aa\5\6\4\2\u00aa"+
		"\u00ab\7\35\2\2\u00ab\u00ac\5\6\4\2\u00ac\u00ad\7\36\2\2\u00ad\27\3\2"+
		"\2\2\u00ae\u00af\7\r\2\2\u00af\u00b0\7:\2\2\u00b0\u00b5\5\2\2\2\u00b1"+
		"\u00b2\7\r\2\2\u00b2\u00b3\7:\2\2\u00b3\u00b5\7\4\2\2\u00b4\u00ae\3\2"+
		"\2\2\u00b4\u00b1\3\2\2\2\u00b5\31\3\2\2\2\u00b6\u00b7\7\16\2\2\u00b7\u00b8"+
		"\7:\2\2\u00b8\u00bd\5\2\2\2\u00b9\u00ba\7\16\2\2\u00ba\u00bb\7:\2\2\u00bb"+
		"\u00bd\7\4\2\2\u00bc\u00b6\3\2\2\2\u00bc\u00b9\3\2\2\2\u00bd\33\3\2\2"+
		"\2\u00be\u00bf\7\n\2\2\u00bf\u00c0\7!\2\2\u00c0\u00c1\5\36\20\2\u00c1"+
		"\u00c2\7\"\2\2\u00c2\35\3\2\2\2\u00c3\u00c4\b\20\1\2\u00c4\u00c5\5 \21"+
		"\2\u00c5\u00ca\3\2\2\2\u00c6\u00c7\f\3\2\2\u00c7\u00c9\5 \21\2\u00c8\u00c6"+
		"\3\2\2\2\u00c9\u00cc\3\2\2\2\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb"+
		"\37\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cd\u00ce\5\60\31\2\u00ce\u00cf\7!\2"+
		"\2\u00cf\u00d0\5\"\22\2\u00d0\u00d4\7\"\2\2\u00d1\u00d3\5\62\32\2\u00d2"+
		"\u00d1\3\2\2\2\u00d3\u00d6\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2"+
		"\2\2\u00d5\u00d9\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7\u00d9\5\b\5\2\u00d8"+
		"\u00cd\3\2\2\2\u00d8\u00d7\3\2\2\2\u00d9!\3\2\2\2\u00da\u00db\b\22\1\2"+
		"\u00db\u00dc\5$\23\2\u00dc\u00e1\3\2\2\2\u00dd\u00de\f\3\2\2\u00de\u00e0"+
		"\5$\23\2\u00df\u00dd\3\2\2\2\u00e0\u00e3\3\2\2\2\u00e1\u00df\3\2\2\2\u00e1"+
		"\u00e2\3\2\2\2\u00e2#\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e4\u00ed\5,\27\2"+
		"\u00e5\u00ed\58\35\2\u00e6\u00ed\5<\37\2\u00e7\u00ed\5(\25\2\u00e8\u00ed"+
		"\5L\'\2\u00e9\u00ed\5H%\2\u00ea\u00ed\5&\24\2\u00eb\u00ed\5D#\2\u00ec"+
		"\u00e4\3\2\2\2\u00ec\u00e5\3\2\2\2\u00ec\u00e6\3\2\2\2\u00ec\u00e7\3\2"+
		"\2\2\u00ec\u00e8\3\2\2\2\u00ec\u00e9\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec"+
		"\u00eb\3\2\2\2\u00ed%\3\2\2\2\u00ee\u00ef\7\5\2\2\u00ef\'\3\2\2\2\u00f0"+
		"\u00f1\7\24\2\2\u00f1\u00f2\7\35\2\2\u00f2\u00f3\5,\27\2\u00f3\u00f4\7"+
		"\36\2\2\u00f4)\3\2\2\2\u00f5\u00f6\b\26\1\2\u00f6\u00f7\5,\27\2\u00f7"+
		"\u00fc\3\2\2\2\u00f8\u00f9\f\3\2\2\u00f9\u00fb\5,\27\2\u00fa\u00f8\3\2"+
		"\2\2\u00fb\u00fe\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd"+
		"+\3\2\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0100\7,\2\2\u0100\u0101\7\35\2\2"+
		"\u0101\u0107\5.\30\2\u0102\u0103\5.\30\2\u0103\u0104\7,\2\2\u0104\u0106"+
		"\3\2\2\2\u0105\u0102\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u0105\3\2\2\2\u0107"+
		"\u0108\3\2\2\2\u0108\u010a\3\2\2\2\u0109\u0107\3\2\2\2\u010a\u010b\7\36"+
		"\2\2\u010b\u011f\3\2\2\2\u010c\u010d\7,\2\2\u010d\u010e\7\35\2\2\u010e"+
		"\u010f\5,\27\2\u010f\u0110\7\36\2\2\u0110\u011f\3\2\2\2\u0111\u0112\7"+
		"\30\2\2\u0112\u0113\7\35\2\2\u0113\u0119\5.\30\2\u0114\u0115\5.\30\2\u0115"+
		"\u0116\7,\2\2\u0116\u0118\3\2\2\2\u0117\u0114\3\2\2\2\u0118\u011b\3\2"+
		"\2\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011c\3\2\2\2\u011b"+
		"\u0119\3\2\2\2\u011c\u011d\7\36\2\2\u011d\u011f\3\2\2\2\u011e\u00ff\3"+
		"\2\2\2\u011e\u010c\3\2\2\2\u011e\u0111\3\2\2\2\u011f-\3\2\2\2\u0120\u0122"+
		"\7,\2\2\u0121\u0120\3\2\2\2\u0122\u0125\3\2\2\2\u0123\u0121\3\2\2\2\u0123"+
		"\u0124\3\2\2\2\u0124\u0126\3\2\2\2\u0125\u0123\3\2\2\2\u0126\u013c\5\6"+
		"\4\2\u0127\u012b\5\6\4\2\u0128\u012a\7,\2\2\u0129\u0128\3\2\2\2\u012a"+
		"\u012d\3\2\2\2\u012b\u0129\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u013c\3\2"+
		"\2\2\u012d\u012b\3\2\2\2\u012e\u0130\7H\2\2\u012f\u012e\3\2\2\2\u0130"+
		"\u0131\3\2\2\2\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0133\3\2"+
		"\2\2\u0133\u0138\5\6\4\2\u0134\u0135\7H\2\2\u0135\u0137\5\6\4\2\u0136"+
		"\u0134\3\2\2\2\u0137\u013a\3\2\2\2\u0138\u0136\3\2\2\2\u0138\u0139\3\2"+
		"\2\2\u0139\u013c\3\2\2\2\u013a\u0138\3\2\2\2\u013b\u0123\3\2\2\2\u013b"+
		"\u0127\3\2\2\2\u013b\u012f\3\2\2\2\u013c/\3\2\2\2\u013d\u013f\5.\30\2"+
		"\u013e\u0140\5> \2\u013f\u013e\3\2\2\2\u013f\u0140\3\2\2\2\u0140\u0141"+
		"\3\2\2\2\u0141\u0142\7\67\2\2\u0142\u0143\5@!\2\u0143\u0149\3\2\2\2\u0144"+
		"\u0145\5.\30\2\u0145\u0146\7\4\2\2\u0146\u0147\7\67\2\2\u0147\u0149\3"+
		"\2\2\2\u0148\u013d\3\2\2\2\u0148\u0144\3\2\2\2\u0149\61\3\2\2\2\u014a"+
		"\u014b\7%\2\2\u014b\u0154\5\6\4\2\u014c\u014d\7%\2\2\u014d\u014f\5\6\4"+
		"\2\u014e\u014c\3\2\2\2\u014e\u014f\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u0151"+
		"\7\31\2\2\u0151\u0152\7%\2\2\u0152\u0154\5\6\4\2\u0153\u014a\3\2\2\2\u0153"+
		"\u014e\3\2\2\2\u0154\63\3\2\2\2\u0155\u0156\7\20\2\2\u0156\u0157\7\35"+
		"\2\2\u0157\u0158\5\66\34\2\u0158\u0159\7\36\2\2\u0159\65\3\2\2\2\u015a"+
		"\u015b\5\6\4\2\u015b\67\3\2\2\2\u015c\u015d\5:\36\2\u015d\u015e\78\2\2"+
		"\u015e9\3\2\2\2\u015f\u0160\5\6\4\2\u0160\u0161\5\4\3\2\u0161\u0162\5"+
		"\6\4\2\u0162\u0180\3\2\2\2\u0163\u0164\5\6\4\2\u0164\u0171\5\4\3\2\u0165"+
		"\u016e\5@!\2\u0166\u016a\5\4\3\2\u0167\u016a\7\6\2\2\u0168\u016a\7*\2"+
		"\2\u0169\u0166\3\2\2\2\u0169\u0167\3\2\2\2\u0169\u0168\3\2\2\2\u016a\u016b"+
		"\3\2\2\2\u016b\u016d\5@!\2\u016c\u0169\3\2\2\2\u016d\u0170\3\2\2\2\u016e"+
		"\u016c\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u0172\3\2\2\2\u0170\u016e\3\2"+
		"\2\2\u0171\u0165\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0171\3\2\2\2\u0173"+
		"\u0174\3\2\2\2\u0174\u0180\3\2\2\2\u0175\u0176\5\6\4\2\u0176\u0177\5\4"+
		"\3\2\u0177\u017c\5\6\4\2\u0178\u0179\t\3\2\2\u0179\u017b\5\6\4\2\u017a"+
		"\u0178\3\2\2\2\u017b\u017e\3\2\2\2\u017c\u017a\3\2\2\2\u017c\u017d\3\2"+
		"\2\2\u017d\u0180\3\2\2\2\u017e\u017c\3\2\2\2\u017f\u015f\3\2\2\2\u017f"+
		"\u0163\3\2\2\2\u017f\u0175\3\2\2\2\u0180;\3\2\2\2\u0181\u0182\5:\36\2"+
		"\u0182\u0183\78\2\2\u0183\u018d\3\2\2\2\u0184\u0185\5\2\2\2\u0185\u0186"+
		"\5\4\3\2\u0186\u0187\5\2\2\2\u0187\u0188\78\2\2\u0188\u018d\3\2\2\2\u0189"+
		"\u018a\5@!\2\u018a\u018b\78\2\2\u018b\u018d\3\2\2\2\u018c\u0181\3\2\2"+
		"\2\u018c\u0184\3\2\2\2\u018c\u0189\3\2\2\2\u018d=\3\2\2\2\u018e\u018f"+
		"\7\35\2\2\u018f\u0190\7\33\2\2\u0190\u0191\7\36\2\2\u0191?\3\2\2\2\u0192"+
		"\u0193\5B\"\2\u0193\u0194\7\35\2\2\u0194\u0195\5\6\4\2\u0195\u0196\7\36"+
		"\2\2\u0196\u01ae\3\2\2\2\u0197\u0198\5B\"\2\u0198\u019a\7\35\2\2\u0199"+
		"\u019b\7\b\2\2\u019a\u0199\3\2\2\2\u019b\u019c\3\2\2\2\u019c\u019a\3\2"+
		"\2\2\u019c\u019d\3\2\2\2\u019d\u019e\3\2\2\2\u019e\u019f\7\36\2\2\u019f"+
		"\u01ae\3\2\2\2\u01a0\u01a1\5B\"\2\u01a1\u01a2\7\35\2\2\u01a2\u01a8\5@"+
		"!\2\u01a3\u01a4\5B\"\2\u01a4\u01a5\5@!\2\u01a5\u01a7\3\2\2\2\u01a6\u01a3"+
		"\3\2\2\2\u01a7\u01aa\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9"+
		"\u01ab\3\2\2\2\u01aa\u01a8\3\2\2\2\u01ab\u01ac\7\36\2\2\u01ac\u01ae\3"+
		"\2\2\2\u01ad\u0192\3\2\2\2\u01ad\u0197\3\2\2\2\u01ad\u01a0\3\2\2\2\u01ae"+
		"A\3\2\2\2\u01af\u01b0\t\4\2\2\u01b0C\3\2\2\2\u01b1\u01b2\5F$\2\u01b2\u01b3"+
		"\7\35\2\2\u01b3\u01b4\5:\36\2\u01b4\u01b5\7\36\2\2\u01b5\u01b6\78\2\2"+
		"\u01b6E\3\2\2\2\u01b7\u01b8\7\17\2\2\u01b8G\3\2\2\2\u01b9\u01ba\5J&\2"+
		"\u01ba\u01bb\7\35\2\2\u01bb\u01bc\5:\36\2\u01bc\u01bd\7\36\2\2\u01bd\u01be"+
		"\78\2\2\u01beI\3\2\2\2\u01bf\u01c0\7\27\2\2\u01c0K\3\2\2\2\u01c1\u01c2"+
		"\7\22\2\2\u01c2\u01c3\7\35\2\2\u01c3\u01c4\5\2\2\2\u01c4\u01c5\7\36\2"+
		"\2\u01c5M\3\2\2\2\u01c6\u01c8\5P)\2\u01c7\u01c6\3\2\2\2\u01c7\u01c8\3"+
		"\2\2\2\u01c8\u01c9\3\2\2\2\u01c9\u01ca\7\2\2\3\u01caO\3\2\2\2\u01cb\u01cc"+
		"\b)\1\2\u01cc\u01cd\5R*\2\u01cd\u01d2\3\2\2\2\u01ce\u01cf\f\3\2\2\u01cf"+
		"\u01d1\5R*\2\u01d0\u01ce\3\2\2\2\u01d1\u01d4\3\2\2\2\u01d2\u01d0\3\2\2"+
		"\2\u01d2\u01d3\3\2\2\2\u01d3Q\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d5\u01dd"+
		"\5\20\t\2\u01d6\u01dd\78\2\2\u01d7\u01dd\5\n\6\2\u01d8\u01dd\5\20\t\2"+
		"\u01d9\u01dd\5\34\17\2\u01da\u01dd\5\64\33\2\u01db\u01dd\5\b\5\2\u01dc"+
		"\u01d5\3\2\2\2\u01dc\u01d6\3\2\2\2\u01dc\u01d7\3\2\2\2\u01dc\u01d8\3\2"+
		"\2\2\u01dc\u01d9\3\2\2\2\u01dc\u01da\3\2\2\2\u01dc\u01db\3\2\2\2\u01dd"+
		"S\3\2\2\2-Y[cjnsv|\u0083\u008f\u00a0\u00b4\u00bc\u00ca\u00d4\u00d8\u00e1"+
		"\u00ec\u00fc\u0107\u0119\u011e\u0123\u012b\u0131\u0138\u013b\u013f\u0148"+
		"\u014e\u0153\u0169\u016e\u0173\u017c\u017f\u018c\u019c\u01a8\u01ad\u01c7"+
		"\u01d2\u01dc";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}