package com.lembed.unit.test.ext.internal.parser;

import java.util.Iterator;

import com.lembed.unit.test.ext.internal.parser.LangFunctionSignature.SignatureBuilder;
import com.lembed.unit.test.ext.parser.ILanguageUnit;
import com.lembed.unit.test.ext.parser.UTestPart;

public class PotentialLanguagePartsToMeaningfulLanguageUnitsTranslator{
	public PotentialLanguagePartsToMeaningfulLanguageUnitsTranslator() {
	}

	public class LanguageUnitIterator implements Iterator<ILanguageUnit> {
		private SignatureBuilder signatureBuilder = LangFunctionSignature.builderStartWith("");
		private ILanguageUnit nextValue = null;
		private final Iterator<UTestPart> parts;

		public LanguageUnitIterator(Iterable<UTestPart> parts) {
			this.parts = parts.iterator();
		}

		@Override
		public boolean hasNext() {
			if (nextValue == null)
				nextValue = getNextLanguageUnit();
			return nextValue != null;
		}

		@Override
		public ILanguageUnit next() {
			ILanguageUnit ret = nextValue;
			nextValue = null;
			return ret;
		}

		@Override
		public void remove() {

		}

		private ILanguageUnit getNextLanguageUnit() {
			while (parts.hasNext()) {
				UTestPart part = parts.next();
				switch (part.getType()) {
				case END_OF_FUNCTION_SIGNATURE:
					signatureBuilder.withEndOffset(part.getEndOffset());
					if (signatureBuilder.isComplete())
						return signatureBuilder.build();
				case MAYBE_NEW_FUNCTION:
					signatureBuilder = LangFunctionSignature.builderStartWith(part.codeString());
					signatureBuilder.withBeginOffset(part.getBeginOffset());
					break;
				case MAYBE_PART_OF_FUNCTION_NAME:
					signatureBuilder
							.addToFunctionDeclaration(part.codeString());
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

	public Iterable<ILanguageUnit> languageUnits(final Iterable<UTestPart> parts) {
		return new Iterable<ILanguageUnit>() {

			@Override
			public Iterator<ILanguageUnit> iterator() {
				return new LanguageUnitIterator(parts);
			}

		};
	}

}
