package org.panda.logicanalyzer.ui.internal.editor;

import org.panda.logicanalyzer.core.util.TimeConverter;
import org.panda.logicanalyzer.core.util.TimeConverter.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;




/**
 * The time ruler provides a time scale display.
 */
public class TimeRuler extends Canvas {
	
	
	GLCanvas gl;

	/**
	 * The scale of this time ruler in nanoseconds per pixel
	 */
	private long scale;

	/**
	 * The length of this time ruler in nanoseconds
	 */
	private long length;

	/**
	 * The background color
	 */
	private Color bg;

	/**
	 * The grid color
	 */
	private Color grid;

	public TimeRuler(Composite parent) {
		super(parent, SWT.NONE);

		bg = new Color(getDisplay(), 45, 36, 34);
		grid = new Color(getDisplay(), 117, 101, 96);
		


		addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				updateImage(e.gc, new Rectangle(e.x, e.x, e.width, e.height));
			}

		});
	}

	/**
	 * Sets a new scale. If the new value is less than equal to zero, it is discarded.
	 *
	 * @param newScale the new value
	 */
	public void setScale(long newScale) {
		if (newScale <= 0 || newScale == scale) return;
		this.scale = newScale;
	}

	/**
	 * Sets a new length. If the new value is less than equal to zero, it is discarded.
	 *
	 * @param newLength the new value
	 */
	public void setLength(long newLength) {
		if (newLength <= 0 || newLength == length) return;
		this.length = newLength;
	}

	/**
	 * Updates the {@link #diagramImage} thus regenerating the content of this widget. This
	 * method should rarely be needed to be called from outside this class, since e.g. {@link #setTimeResolution(long)}
	 * will call this method.
	 */
	protected void updateImage(GC gc, Rectangle bounds) {
		// Warning: we do not check for overflows, this might cause some nasty bugs
		final int height = 25;
		int width = bounds.width;

		gc.setBackground(bg);
		gc.fillRectangle(bounds.x, 0, width, height);

		final TimeUnit unit = TimeConverter.chooseAppropriateUnit(length);
		if (bounds.x < 50) {
			gc.setForeground(grid);
			gc.drawString(String.valueOf("[" + unit.getShortname() + "]"), 2, (int) (height * 0.1));
		}
		
		/*
		 * This still is a very uncool way of handling the asynchronity since if we have to draw a lot
		 * of channels, we'll create a lot of threads.
		 */
		gc.setForeground(grid);
		for (int i = bounds.x - (bounds.x % 100); i < bounds.x + width; i += 100) {
			if (i > 5) {
				double t = unit.convert(i * scale);
				gc.drawLine(i, (int) (height * 0.6), i, height);
				gc.drawText(String.format("%03.3f", t), i, (int) (height * 0.1));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		bg.dispose();
		grid.dispose();

		super.dispose();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point((int) (length / scale), 25);
	}

}
