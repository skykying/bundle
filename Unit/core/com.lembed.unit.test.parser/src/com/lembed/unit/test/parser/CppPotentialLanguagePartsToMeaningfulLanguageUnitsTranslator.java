package com.lembed.unit.test.parser;

import java.util.Iterator;

import com.lembed.unit.test.parser.langunit.CppLangFunctionSignature;
import com.lembed.unit.test.parser.langunit.CppLangFunctionSignature.SignatureBuilder;
import com.lembed.unit.test.parser.langunit.LanguageUnit;

public class CppPotentialLanguagePartsToMeaningfulLanguageUnitsTranslator {
	public CppPotentialLanguagePartsToMeaningfulLanguageUnitsTranslator() {
	}

	public class LanguageUnitIterator implements Iterator<LanguageUnit> {
		private SignatureBuilder signatureBuilder = CppLangFunctionSignature.builderStartWith("");
		private LanguageUnit nextValue = null;
		private final Iterator<CppPart> parts;

		public LanguageUnitIterator(Iterable<CppPart> parts) {
			this.parts = parts.iterator();
		}

		@Override
		public boolean hasNext() {
			if (nextValue == null)
				nextValue = getNextLanguageUnit();
			return nextValue != null;
		}

		@Override
		public LanguageUnit next() {
			LanguageUnit ret = nextValue;
			nextValue = null;
			return ret;
		}

		@Override
		public void remove() {

		}

		private LanguageUnit getNextLanguageUnit() {
			while (parts.hasNext()) {
				CppPart part = parts.next();
				switch (part.getType()) {
				case END_OF_FUNCTION_SIGNATURE:
					signatureBuilder.withEndOffset(part.getEndOffset());
					if (signatureBuilder.isComplete())
						return signatureBuilder.build();
				case MAYBE_NEW_FUNCTION:
					signatureBuilder = CppLangFunctionSignature.builderStartWith(part.codeString());
					signatureBuilder.withBeginOffset(part.getBeginOffset());
					break;
				case MAYBE_PART_OF_FUNCTION_NAME:
					signatureBuilder.addToFunctionDeclaration(part.codeString());
					break;
				case PARAMETER:
					signatureBuilder.addToParameter(part.codeString());
					break;
				case END_OF_FUNCTION:
				case PART_OF_LONG_FUNCTION_NAME:
				case TOKEN:
					break;
				}
			}
			return null;
		}
	}

	public Iterable<LanguageUnit> languageUnits(final Iterable<CppPart> parts) {
		return new Iterable<LanguageUnit>() {

			@Override
			public Iterator<LanguageUnit> iterator() {
				return new LanguageUnitIterator(parts);
			}

		};
	}

}
