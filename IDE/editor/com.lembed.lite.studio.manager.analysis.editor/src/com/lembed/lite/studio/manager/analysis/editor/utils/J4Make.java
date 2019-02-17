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

Copyright (c) 2015 Pierre Lindenbaum

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
* 2015 creation

*/
package com.lembed.lite.studio.manager.analysis.editor.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lembed.lite.studio.manager.analysis.editor.BaseEditorPlugin;



/** main application */
public class J4Make {
	
	/** current graph */
	private Graph graph = null;

	/** output format */
	private enum FORMAT {
		XML, DOT, GEXF, RDF
	};

	private J4Make() {
	}

	/** current version */
	public String getVersion() {
		return "1.0";
	}

	public int run(FORMAT fmt, BufferedReader in, FileOutputStream fos) {
		try {
			this.graph = Graph.parse(in);
			in.close();

			BaseEditorPlugin.showMessage("parsing done");

			IGraphWriter gw = new DotWriter();
			switch (fmt) {
			case XML:
				gw = new XmlWriter();
				break;
			case GEXF:
				gw = new GexfWriter();
				break;
			case DOT:
				gw = new DotWriter();
				break;
			case RDF:
				gw = new RdfWriter();
				break;
			default:
				throw new IllegalArgumentException("" + fmt);
			}
			final OutputStream out;

			if (fos == null) {
				out = System.out;
			} else {
				out = fos;
			}

			gw.write(this.graph, out);

			out.flush();

			if (fos != null) {
				fos.flush();
				fos.close();
				fos = null;
			}

			return 0;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

}
