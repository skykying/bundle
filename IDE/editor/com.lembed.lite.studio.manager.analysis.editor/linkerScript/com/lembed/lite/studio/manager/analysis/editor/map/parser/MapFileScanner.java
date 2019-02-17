/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.map.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * Used as the lexer for config files
 */
public class MapFileScanner extends RuleBasedScanner {
	public final static String CONFIG_BLOCK_COMMENT_START = "__config_block_comment_start"; //$NON-NLS-1$
	public final static String CONFIG_BLOCK_COMMENT_END = "__config_block_comment_end"; //$NON-NLS-1$
	public final static String MAP_ARCHIVE_FILE = "__map_archive"; //$NON-NLS-1$
	public final static String MAP_MEMORY_SECTION = "__map_section"; //$NON-NLS-1$
	public final static String MAP_DISCARD_INPUT = "__map_discard"; //$NON-NLS-1$
	public final static String MAP_LINKER_SYMBOL = "__map_linker_symbol"; //$NON-NLS-1$
	public final static String MAP_ALLOCATION_SYMBOL = "__map_allocation_symbol"; //$NON-NLS-1$
	public final static String CONFIG_DEFAULT = "__config_default"; //$NON-NLS-1$

	enum ETokenType {
		COMMENT,
		BLOCK_COMMENT,
		START,
		HEADING,
		HEADING_END,
		HEADING_ENABLE,
		HEADING_ENABLE_END,
		CODE_ENABLE,
		CODE_DISABLE,
		CODE_END,
		OPTION,
		OPTION_CHECK,
		OPTION_STRING,
		TOOLTIP,
		EOC,	// End of Config
		VALUE,
		NUMBER,
		STRING,
		DEFAULT,
		UNKNOWN,
	};

	// this value maintains the previous token's line number
	private int prevLine;

	private boolean startConfig;
	private boolean inBlockComment;

	private final int mapSize = 7;
	private Map<Integer, Boolean> commentStarted = new LinkedHashMap<Integer, Boolean>(mapSize*10/7, 0.7f, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<Integer, Boolean> eldest) {
			return size() > mapSize;
		}
	};

	public MapFileScanner() {
		Collection<IRule> rules = new LinkedList<>();

		// Comment rules
		rules.add(new CommentRule("//", new Token(MAP_ARCHIVE_FILE))); //$NON-NLS-1$
		
		rules.add(new CommentRule("/*", new Token(CONFIG_BLOCK_COMMENT_START))); //$NON-NLS-1$
		rules.add(new CommentRule("*/", new Token(CONFIG_BLOCK_COMMENT_END))); //$NON-NLS-1$
		// Tag rules
		rules.add(new SingleLineRule("<<<", ">>>", new Token(MAP_DISCARD_INPUT))); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new TagRule(new Token(MAP_MEMORY_SECTION)));
		// Value rules
		rules.add(new SingleLineRule("\"", "\"", new Token(MAP_ALLOCATION_SYMBOL), '\\')); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new NumberRule(new Token(MAP_LINKER_SYMBOL)));	// Set this to last !!!

		setRules(rules.toArray(new IRule[rules.size()]));

		setDefaultReturnToken(new Token(CONFIG_DEFAULT));
	}

	public void clear() {
		prevLine = 0;
		startConfig = false;
		inBlockComment = false;
		commentStarted.clear();
	}

	@Override
	public IToken nextToken() {
		IToken token = super.nextToken();
		// Find the starting mark
		while (!startConfig && !token.isEOF()) {
			int currLine = getCurrentLineNumber();
			if (getTokenType(token) == ETokenType.COMMENT) {
				commentStarted.put(currLine, true);
			}
			if (getTokenType(token) == ETokenType.START &&
					commentStarted.containsKey(currLine) && commentStarted.get(currLine)) {
				startConfig = true;
				storeCurrentToken(token);
				return token;
			}
			token = super.nextToken();
		}
		if (token.isEOF()) {
			return token;
		}
		while (getTokenType(token) == ETokenType.DEFAULT) {
			storeCurrentToken(token);
			token = super.nextToken();
		}
		int currLine = getCurrentLineNumber();
		if (!commentStarted.containsKey(currLine)) {
			commentStarted.put(currLine, false);
		}
		if (getTokenType(token) == ETokenType.COMMENT) {
			commentStarted.put(currLine, true);
		}

		if (!commentStarted.get(currLine) && !inBlockComment &&
				(getTokenType(token) == ETokenType.NUMBER || getTokenType(token) == ETokenType.STRING)) {
			storeCurrentToken(token);
			return token;
		}

		while (continueLoop(currLine, token)) {
			if (token.isEOF() || getTokenType(token) == ETokenType.EOC) {
				return token;
			}
			if (getTokenTag(token).equals(MAP_MEMORY_SECTION)) {
				if (commentStarted.get(currLine)) {
					storeCurrentToken(token);
					return token;
				}
			}
			if (prevLine != currLine) {
				commentStarted.put(currLine, false);
				// Return the string and number token
				if (!inBlockComment &&
						(getTokenType(token) == ETokenType.STRING ||
						getTokenType(token) == ETokenType.NUMBER ||
						getTokenType(token) == ETokenType.START)) {
					storeCurrentToken(token);
					return token;
				}
			}
			if (getTokenType(token) == ETokenType.COMMENT) {
				commentStarted.put(currLine, true);
			}
			if (getTokenType(token) != ETokenType.DEFAULT) {
				storeCurrentToken(token);
			}
			token = super.nextToken();
			currLine = getCurrentLineNumber();
		}

		return token;
	}

	public String getTokenTag(IToken token) {
		Object obj = token.getData();
		if (obj != null && obj instanceof String) {
			return (String) obj;
		}
		return null;
	}

	public String getTokenContent(IToken token) {
		if (token.isEOF()) {
			return ""; //$NON-NLS-1$
		}
		int offset = getTokenOffset();
		int length = getTokenLength();
		String text;
		try {
			text = fDocument.get(offset, length);
			if (MAP_ALLOCATION_SYMBOL.equals(getTokenTag(token))) {
				return text;
			} else if (MAP_DISCARD_INPUT.equals(getTokenTag(token))) {
				return text.trim().replaceAll("\\s+"," "); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				// Get the pure text in the <>
				return text.replaceAll("[\\s<>]",""); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (BadLocationException e) {
		}

		return null;
	}

	public ETokenType getTokenType(IToken token) {
		if (token.isEOF()) {
			return ETokenType.EOC;
		}
		String tokenContent = getTokenContent(token);
		String tag = getTokenTag(token);
		if (tag.equals(CONFIG_DEFAULT)) {
			return ETokenType.DEFAULT;
		} else if (tag.equals(MAP_DISCARD_INPUT)) {
			if (tokenContent.equalsIgnoreCase("<<< Use Configuration Wizard In Context Menu >>>")) { //$NON-NLS-1$
				return ETokenType.START;
			} else if (tokenContent.equalsIgnoreCase("<<< End Of Configuration Section >>>")) { //$NON-NLS-1$
				return ETokenType.EOC;
			} else {
				return ETokenType.DEFAULT;
			}
		} else if (tag.equals(MAP_MEMORY_SECTION)) {
			Assert.isTrue(tokenContent.length() > 0);
			char type = Character.toLowerCase(tokenContent.charAt(0));
			if (Character.isDigit(type)) {	// For Selection Token: <0=>
				type = tokenContent.charAt(tokenContent.length()-1);
			}
			switch (type) {
			case 'h':
				return ETokenType.HEADING;
			case 'e':
				return ETokenType.HEADING_ENABLE;
			case 'c':
				return ETokenType.CODE_ENABLE;
			case 'o':
				return ETokenType.OPTION;
			case 'q':
				return ETokenType.OPTION_CHECK;
			case 's':
				return ETokenType.OPTION_STRING;
			case 'i':
				return ETokenType.TOOLTIP;
			case '/':
				return getEndTokenType(tokenContent.charAt(1));
			case '!':
				return getNextTokenChar(tokenContent.charAt(1));
			case '=':
				return ETokenType.VALUE;
			default:
				return ETokenType.UNKNOWN;
			}
		} else if (tag.equals(CONFIG_BLOCK_COMMENT_START)) {
			inBlockComment = true;
			return ETokenType.BLOCK_COMMENT;
		} else if (tag.equals(CONFIG_BLOCK_COMMENT_END)) {
			inBlockComment = false;
			return ETokenType.BLOCK_COMMENT;
		} else if (tag.equals(MAP_ARCHIVE_FILE)) {
			return ETokenType.COMMENT;
		} else if (tag.equals(MAP_LINKER_SYMBOL)) {
			return ETokenType.NUMBER;
		} else if (tag.equals(MAP_ALLOCATION_SYMBOL)) {
			return ETokenType.STRING;
		}

		return ETokenType.UNKNOWN;
	}

	private ETokenType getEndTokenType(char token) {
		switch (token) {
		case 'h':
			return ETokenType.HEADING_END;
		case 'e':
			return ETokenType.HEADING_ENABLE_END;
		case 'c':
			return ETokenType.CODE_END;
		default:
			return ETokenType.UNKNOWN;
		}
	}

	private ETokenType getNextTokenChar(char token) {
		switch (token) {
		case 'c':
			return ETokenType.CODE_DISABLE;
		default:
			return ETokenType.UNKNOWN;
		}
	}

	public String readString() {
		StringBuilder sb = new StringBuilder();
		int c;
		do {
			c = read();
			sb.append((char) c);
		} while (c != '<' && c != '\n');
		if (c == '<') {
			unread();
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString().trim();
	}

	public String readLine() {
		StringBuilder sb = new StringBuilder();
		int c;
		do {
			c = read();
			sb.append((char) c);
		} while (c != '\n');
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString().trim();
	}

	public int getCurrentLineNumber() {
		try {
			return fDocument.getLineOfOffset(getTokenOffset());
		} catch (BadLocationException e) {
			return -1;
		}
	}

	private void storeCurrentToken(IToken token) {
		prevLine = getCurrentLineNumber();
	}

	private boolean continueLoop(int currLine, IToken token) {
		if (token.isEOF()) {
			return false;
		}
		// newline started, or it is not a tag token
		if(prevLine != currLine ||
				getTokenType(token) == ETokenType.DEFAULT ||
				getTokenType(token) == ETokenType.COMMENT ||
				getTokenType(token) == ETokenType.BLOCK_COMMENT) {
			return true;
		}
		// it is a tag/mark token but the line does not start with //
		if ((getTokenTag(token).equals(MAP_MEMORY_SECTION) || getTokenTag(token).equals(MAP_DISCARD_INPUT))
				&& !commentStarted.get(currLine)) {
			return true;
		}
		// it is a number/string but it is behind a //
		if (prevLine == currLine && commentStarted.get(currLine) &&
				(getTokenType(token) == ETokenType.NUMBER || getTokenType(token) == ETokenType.STRING)) {
			return true;
		}
		// it is a number/string but it is in a block comment
		if (inBlockComment &&
				(getTokenType(token) == ETokenType.NUMBER || getTokenType(token) == ETokenType.STRING)) {
			return true;
		}
		return false;
	}

}
