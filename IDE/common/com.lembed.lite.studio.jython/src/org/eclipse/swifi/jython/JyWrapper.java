package org.eclipse.swifi.jython;

import java.util.Properties;


import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JyWrapper {

	
	 @SuppressWarnings("resource")
	public static void main() throws PyException {
		 
	        Properties props = new Properties();
//	      props.put("python.home","path to the Lib folder");
	        props.put("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
	        props.put("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
	        props.put("python.import.site","false");

	       
	        Properties preprops = System.getProperties();
	        PythonInterpreter.initialize(preprops, props, new String[0]);

	        PythonInterpreter interp = new PythonInterpreter();
	        interp.exec("import java");
	        interp.exec("import org.eclipse.swt");
//	        interp.exec("import org.eclipse.swt.SWT");
//	        interp.exec("import org.eclipse.swt.awt.SWT_AWT");
//	        interp.exec("import org.eclipse.swt.widgets.Composite");

//	        interp.exec("composite = Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND)");
//	        interp.exec("xframe = SWT_AWT.new_Frame(composite)");
//	        interp.exec("button = awt.Button('Close Me!', actionPerformed=exit)");
//	        interp.exec("xframe.add(button, 'Center')");
//	        interp.exec("xframe.pack()");
	        interp.set("a", new PyInteger(42));
	        interp.exec("print a");
	        interp.exec("x = 2+2");
	        PyObject x = interp.get("x");
	        System.out.println("x: " + x);
	    }
	 
}
