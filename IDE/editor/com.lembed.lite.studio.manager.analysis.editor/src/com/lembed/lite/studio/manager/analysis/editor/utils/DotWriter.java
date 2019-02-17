/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
/*
The MIT License (MIT)

Copyright (c) 2016 Pierre Lindenbaum

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


History:
* 2016 creation

*/
package com.lembed.lite.studio.manager.analysis.editor.utils;

import java.io.OutputStream;
import java.io.PrintStream;

public class DotWriter implements IGraphWriter {
	public DotWriter() {
	}

	@Override
	public void write(final Graph graph, final OutputStream w) {
		final PrintStream out;
		if (w instanceof PrintStream) {
			out = PrintStream.class.cast(w);
		} else {
			out = new PrintStream(w);
		}

		out.println("digraph G {");
		for (final Target t : graph.getTargets()) {
			out.println("n" + t.getNodeId() + "[label=\"" + t.getName() + "\"];");
		}
		for (final Target t : graph.getTargets()) {
			for (final Target c : t.getPrerequisites()) {
				out.println("n" + c.getNodeId() + " -> n" + t.getNodeId() + ";");
			}
		}
		out.println("}");
		out.flush();
	}
}
