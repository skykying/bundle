package com.lembed.unit.test.parser;

import java.util.Iterator;

import com.lembed.unit.test.parser.langunit.CppLangFunctionSignature;
import com.lembed.unit.test.parser.langunit.LanguageUnit;

public class CppSourceCodeReader implements SourceCodeReader {
	public class SignatureIterable implements Iterable<CppLangFunctionSignature> {

		private final Iterator<LanguageUnit> units;

		public SignatureIterable(Iterable<LanguageUnit> units) {
			this.units = units.iterator();
		}

		@Override
		public Iterator<CppLangFunctionSignature> iterator() {
			return new Iterator<CppLangFunctionSignature>() {

				@Override
				public boolean hasNext() {
					return units.hasNext();
				}

				@Override
				public CppLangFunctionSignature next() {
					return (CppLangFunctionSignature) units.next();
				}

				@Override
				public void remove() {
				}

			};
		}

	}

	@Override
	public Iterable<CppLangFunctionSignature> signatures(String sourceCode) {
		Iterable<Token> tokens = new CppLikeCodeTokenSplitter().tokens(sourceCode);
		Iterable<CppPart> parts = new CppTokensToPotentialLanguagePartsTranslator().getPotentialLanguageParts(tokens);
		Iterable<LanguageUnit> units = new CppPotentialLanguagePartsToMeaningfulLanguageUnitsTranslator()
				.languageUnits(parts);
		return new SignatureIterable(units);
	}

}
