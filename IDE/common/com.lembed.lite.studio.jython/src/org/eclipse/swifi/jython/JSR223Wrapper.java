package org.eclipse.swifi.jython;


import java.util.List;
import java.util.Properties;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSR223Wrapper {

	
	public static void main() throws ScriptException {
		Properties props = System.getProperties();
		props.setProperty("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
		props.setProperty("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
		props.setProperty("python.import.site","false");
        
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");
        engine.getContext();
        engine.eval("import sys");
        engine.eval("print sys");
        engine.put("a", 42);
        engine.eval("print a");
        engine.eval("x = 2 + 2");
        Object x = engine.get("x");
        System.out.println("x: " + x);
    } 
	
	
	public static ScriptEngine getRunner() {
		Properties props = System.getProperties();
		props.setProperty("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
		props.setProperty("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
		props.setProperty("python.import.site","false");
        
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");
        return engine;
	}
	
	public static void listEngines(){ // Note 1
		
		Properties props = System.getProperties();
		props.setProperty("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
		props.setProperty("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
		props.setProperty("python.import.site","false");
        
        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories =
                mgr.getEngineFactories();
        for (ScriptEngineFactory factory: factories) {
            System.out.println("ScriptEngineFactory Info");
            String engName = factory.getEngineName();
            String engVersion = factory.getEngineVersion();
            String langName = factory.getLanguageName();
            String langVersion = factory.getLanguageVersion();
            System.out.println("\tScript Engine: " + engName + ":" +
                               engVersion);
            List<String> engNames = factory.getNames();
            for(String name: engNames) {
                System.out.println("\tEngine Alias: " + name);
            }
            System.out.println("\tLanguage: " + langName + ":" +  langVersion);
        }
    }
	
}
