package com.lembed.unit.test.ext.internal.parser;

import java.util.Iterator;

import com.lembed.unit.test.ext.parser.ILanguageUnit;
import com.lembed.unit.test.ext.parser.ISourceCodeReader;
import com.lembed.unit.test.ext.parser.UTestPart;

public class SourceCodeReader implements ISourceCodeReader {
	public class SignatureIterable implements Iterable<LangFunctionSignature> {

		private final Iterator<ILanguageUnit> units;

		public SignatureIterable(Iterable<ILanguageUnit> units) {
			this.units = units.iterator();
		}

		@Override
		public Iterator<LangFunctionSignature> iterator() {
			return new Iterator<LangFunctionSignature>() {

				@Override
				public boolean hasNext() {
					return units.hasNext();
				}

				@Override
				public LangFunctionSignature next() {
					return (LangFunctionSignature)units.next();
				}

				@Override
				public void remove() {
				}
				
			};
		}

	}
	@Override
	public Iterable<LangFunctionSignature> signatures(String sourceCode) {
		Iterable<Token> tokens = new LikeCodeTokenSplitter().tokens(sourceCode);
		Iterable<UTestPart> parts = new TokensToPotentialLanguagePartsTranslator().getPotentialLanguageParts(tokens);
		Iterable<ILanguageUnit> units = new PotentialLanguagePartsToMeaningfulLanguageUnitsTranslator().languageUnits(parts);
		return new SignatureIterable(units);
	}

}
