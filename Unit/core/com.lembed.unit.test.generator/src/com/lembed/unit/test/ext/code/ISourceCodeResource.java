package com.lembed.unit.test.ext.code;

public interface ISourceCodeResource {

	public abstract String getFullText();

	public abstract int getCursorPosition();

	public abstract String getSelectedText();

}