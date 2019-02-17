package com.lembed.unit.test.ext.parser;

import com.lembed.unit.test.ext.internal.parser.Token;

public class UTestPart {
	public enum Type {
		MAYBE_NEW_FUNCTION, END_OF_FUNCTION_SIGNATURE, PART_OF_LONG_FUNCTION_NAME
	, MAYBE_PART_OF_FUNCTION_NAME, PARAMETER, END_OF_FUNCTION, TOKEN, END_OF_GLOBAL_STATEMENT, PREPROCESSOR, TYPEDEF, ASSIGNMENT}
	private Type type;
	private final Token token;
	private UTestPart(Type type, Token token){
		this.type = type;
		this.token = token;
	}
	public Type getType(){
		return type;
	}
	public static UTestPart StartNewFunction(Token token2) {
		return new UTestPart(Type.MAYBE_NEW_FUNCTION, token2);
	}
	static public UTestPart EndOfFunctionSignature(com.lembed.unit.test.ext.internal.parser.Token token2){
		return new UTestPart(Type.END_OF_FUNCTION_SIGNATURE, token2);
	}
	public static UTestPart PartOfLongFunctionName(Token token) {
		return new UTestPart(Type.PART_OF_LONG_FUNCTION_NAME, token);
	}
	public static UTestPart AddToFunctionName(Token token2) {
		return new UTestPart(Type.MAYBE_PART_OF_FUNCTION_NAME, token2);
	}
	public static UTestPart Parameter(com.lembed.unit.test.ext.internal.parser.Token token2) {
		return new UTestPart(Type.PARAMETER, token2);
	}
	public static UTestPart EndOfFunction() {
		return new UTestPart(Type.END_OF_FUNCTION, null);
	}
	public static UTestPart Token(com.lembed.unit.test.ext.internal.parser.Token token2) {
		return new UTestPart(Type.TOKEN, token2);
	}
	public static UTestPart EndOfGlobalStatement() {
		return new UTestPart(Type.END_OF_GLOBAL_STATEMENT, null);
	}
	
	public static UTestPart Typedef() {
		return new UTestPart(Type.TYPEDEF, null);
	}
	public static UTestPart Assignment() {
		return new UTestPart(Type.ASSIGNMENT, null);
	}

	@Override
	public String toString() {
		return "CppPart [type=" + type + ", token=" + token + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UTestPart other = (UTestPart) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	public String codeString() {
		return token.toString();
	}
	public int getEndOffset() {
		return this.token.getEndOffset();
	}
	public int getBeginOffset() {
		return this.token.getBeginOffset();
	}
}
