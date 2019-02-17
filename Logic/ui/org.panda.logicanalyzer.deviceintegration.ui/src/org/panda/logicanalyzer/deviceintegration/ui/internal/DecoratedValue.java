package org.panda.logicanalyzer.deviceintegration.ui.internal;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * A decorated value is some sort of object decorated with another string. This
 * class can be used to effortlessly use JFace viewers, since we may simply use
 * the {@link LabelProvider} which uses {@link #toString()}.
 */
public class DecoratedValue<V> {

	/**
	 * The alignment of the value
	 */
	public static enum Alignment {
		Left, Right, Hide;
	}

	/**
	 * The value we're decorating
	 */
	private final V value;

	/**
	 * The decoration
	 */
	private final String decoration;

	/**
	 * The alignment of the decoration
	 */
	private final Alignment alignment;

	public DecoratedValue(V value, String decoration) {
		this(value, decoration, Alignment.Right);
	}

	public DecoratedValue(V value, String decoration, Alignment alignment) {
		this.value = value;
		this.decoration = decoration;
		this.alignment = alignment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result;

		if (alignment == Alignment.Left) {
			result = decoration + getValue().toString();
		} else if (alignment == Alignment.Right) {
			result = getValue().toString() + decoration;
		} else {
			result = decoration;
		}

		return result;
	}

	public V getValue() {
		return value;
	}

}
