package com.lembed.unit.test.ext.code;

public interface ISourceCodeProvider {

	public abstract String getFullText();

	public abstract int getCursorPosition();

	public abstract String getSelectedText();

}