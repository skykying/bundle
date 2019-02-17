package org.eclipse.swifi.jython.helper;

public class JythonHelperAction  {

    private void generatePy(String outputDir, String[] libs, String... files) {
    	ClassGenerator ej = JythonHelper.getPyCls(outputDir);
//        ej.loadJarLibrarys(libs);
//        ej.generatePys(files);
    }
}
