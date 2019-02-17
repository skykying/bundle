package com.lembed.unit.test.ext.internal.parser;

import java.util.Iterator;

import com.lembed.unit.test.ext.parser.UTestPart;

public class TokensToPotentialLanguagePartsTranslator{
	private enum State {
		GLOBAL, DEC, NAMESPACE, DEC_TO_IMP, IMP, FUNCTION_NAME, TYPEDEF, GLOBAL_ASSIGNMENT
	}
	
    public TokensToPotentialLanguagePartsTranslator() {
	}
    
	public class PotentialLanguagePartsIterator implements Iterator<UTestPart> {
		private State _state=State.GLOBAL;

		private int pa_count = 0;
		private int br_count = 0;

		private UTestPart nextValue = null;
		private final Iterator<Token> tokens;

		public PotentialLanguagePartsIterator(Iterable<Token> tokens) {
			this.tokens = tokens.iterator();
		}

		@Override
		public boolean hasNext() {
			if (nextValue  == null)
				nextValue = getNextPotentialLanguagePart();
			return nextValue != null;
		}

		@Override
		public UTestPart next() {
			UTestPart ret = nextValue;
			nextValue = null;
			return ret;
		}

		@Override
		public void remove() {
		}

		private UTestPart getNextPotentialLanguagePart() {
			while (tokens.hasNext()){
				Token token = tokens.next();
				UTestPart fun = read_token(token);
				if (fun != null)
					return fun;
			}
			return null;
		}
		private UTestPart read_token(Token token){
	        if (token.isWhiteSpace())
	            return null;//sUniversalCodeCounter.NEW_LINE, None
	        else
	            return input(token);
	    }
		protected UTestPart input(Token token) {
	    	if (token.isPreprocessor() || 
	    			token.isComment())
	    		return null;
	   
			switch(get_state()){
			case GLOBAL:
				return _GLOBAL(token);
			case FUNCTION_NAME:
				return _GLOBAL(token);
			case GLOBAL_ASSIGNMENT:
				return _GLOBAL_ASSIGNMENT(token);
			case DEC:
				return _DEC(token);
			case NAMESPACE:
				return _NAMESPACE(token);
			case DEC_TO_IMP:
				return _DEC_TO_IMP(token);
			case IMP:
				return _IMP(token);
			case TYPEDEF:
				return _TYPEDEF(token);
			}
			return null;
		}
	    
		private UTestPart _GLOBAL(Token token){
	        if (token.isAssignment()) {
	        	set_state(State.GLOBAL_ASSIGNMENT);
	            return UTestPart.Assignment();
	        }
	        else if (token.isTypeDef()) {
	        	set_state(State.TYPEDEF);
	            return UTestPart.Typedef();
	        }
	        else if (token.isLeftParentheses()) {
	            pa_count += 1;
	            set_state(State.DEC);
	            return UTestPart.PartOfLongFunctionName(token);
	        }
	        else if (token.equals("::")) 
	        	set_state(State.NAMESPACE);
	        else if (token.isSemicolon()) {
	        	set_state(State.GLOBAL);
	        	return UTestPart.EndOfGlobalStatement();
	        }
	        else {
	        	if (get_state() == State.FUNCTION_NAME)
	                return UTestPart.AddToFunctionName(token);
	        	set_state(State.FUNCTION_NAME);
	            return UTestPart.StartNewFunction(token);
	        }
	        return null;
	    }
		private UTestPart _GLOBAL_ASSIGNMENT(Token token) {
	        if (token.isSemicolon()) {
	        	set_state(State.GLOBAL);
	        	return UTestPart.EndOfGlobalStatement();
	        }
			return null;
		}
		private UTestPart _TYPEDEF(Token token) {
			if (token.isSemicolon())
	        	set_state(State.GLOBAL);
			return null;
		}
	    private UTestPart _NAMESPACE(Token token){
	    	set_state(State.GLOBAL);
	        return UTestPart.AddToFunctionName(new Token("::"+token.toString(), 0));
	    }
	    private UTestPart _DEC(Token token){
	        if (token.isLeftBracket())
	            pa_count += 1;
	        else if(token.isRightBracket()){
	            pa_count -= 1;
	            if (pa_count == 0) {
	            	set_state(State.DEC_TO_IMP);
	                return UTestPart.EndOfFunctionSignature(token);
	            }
	        }
	        else if (pa_count == 1)
	            return UTestPart.Parameter(token);
	        return UTestPart.PartOfLongFunctionName(new Token(" " + token.toString(), 0));
	    }
	    private UTestPart _DEC_TO_IMP(Token token){
	        if (token.equals("const"))
	            return UTestPart.PartOfLongFunctionName(new Token(" " + token.toString(), 0));
	        else if (token.isLeftBrace()){
	            br_count += 1;
	            set_state(State.IMP);
	        }
	        else
	        	set_state(State.GLOBAL);
	        return null;
	    }
	    private UTestPart  _IMP(Token token){
	        if (token.isLeftBrace())
	            br_count += 1;
	        else if(token.isRightBrace()){
	            br_count -= 1;
	            if (br_count == 0){
	            	set_state(State.GLOBAL);
	                return UTestPart.EndOfFunction();
	            }
	        }
	    	return UTestPart.Token(token);
	    }
	    protected State get_state() {
			return _state;
		}
		protected void set_state(State _state) {
			this._state = _state;
		}
	}
	public Iterable<UTestPart> getPotentialLanguageParts(final Iterable<Token> tokens) {
		return new Iterable<UTestPart> (){

			@Override
			public Iterator<UTestPart> iterator() {
				return new PotentialLanguagePartsIterator(tokens);
			}
			
		};
	}
    
}
