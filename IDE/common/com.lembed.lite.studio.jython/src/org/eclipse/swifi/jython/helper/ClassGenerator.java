package org.eclipse.swifi.jython.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ClassGenerator {
	
    public static final String INIT_PY = "__init__.py";
    public static final String DEF_TPL = "def %s(%s):";
    public static final String CLASS_TPL = "class %s:";
    public static final String PASS = "pass";
    private static final Object INDENT = "    ";
    public static final String STATIC_METHOD = "@staticmethod";

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(new String[]{
            "in"
    }));
    
    public static final Set<String> IGNORED_METHODS = new HashSet<String>(Arrays.asList(new String[]
            {"wait", "toString", "hashCode", "notify", "notifyAll", "getClass", "yield"}));

    private String outputDir;
    public static final String INIT_TEMPLATE = "# encoding: utf-8\n" +
            "# module %s\n" +
            "# from (built-in)\n" +
            "# by generator 999.999\n" +
            "# source:%s\n";

    protected ClassGenerator(String outputDir) {
        this.outputDir = outputDir;
    }

    String indents(String line, int i) {
        String[] split = line.split(LINE_SEPARATOR);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(indent(s, i));
        }
        return sb.toString();
    }


    String indent(String line) {
        return indent(line, 1);
    }

    String indent(String line, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(INDENT);
        }
        sb.append(line);
        return sb.toString();
    }

    String makeAField(Field f) {
        return f.getName() + " = None";
    }

    String indents(String line) {
        String[] split = line.split(LINE_SEPARATOR);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(indent(s, 1));
            sb.append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    void writePyHeader(FileWriter fw, String name, URL resourceURL) {
        String source = "";
        if (resourceURL != null) {
            source = resourceURL.toString();
        }
        try {
            fw.write(String.format(INIT_TEMPLATE, name, source));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void ensureInitPy(File dir) {
        File initFile = new File(dir, INIT_PY);
//        if (initFile.exists() && initFile.lastModified() < startTime) {
//            initFile.delete();
//        }
        if (!initFile.exists()) {
            try {
                boolean newFile = initFile.createNewFile();
                if (newFile) {
                    FileWriter fw = new FileWriter(initFile);
                    writePyHeader(fw, initFile.getName(), null);
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    File createDirectory(Class clazz) {
        String packageName = clazz.getPackage().getName();
        System.out.println("create package directory:" + packageName);

        String[] split = packageName.split("\\.");
        File currentDir = new File(outputDir);
        for (String dirName : split) {
            File tmpDir = new File(currentDir, dirName);
            if (!tmpDir.exists()) {
                tmpDir.mkdir();
            }
            ensureInitPy(tmpDir);
            currentDir = tmpDir;
        }
        return currentDir;
    }

    Method[] filterOverrideMethods(Method[] methods) {
        Map<String, Method> methodMap = new HashMap<String, Method>();

        for (Method m : methods) {
            String name = m.getName();
            Method existingMethod = methodMap.get(name);
            if (existingMethod == null) {
                methodMap.put(name, m);
            } else {
                Class<?>[] parameterTypes = existingMethod.getParameterTypes();
                Class<?>[] newParameterTypes = m.getParameterTypes();
                if (newParameterTypes.length > parameterTypes.length) {
                    methodMap.put(name, m);
                }
            }
        }
        return methodMap.values().toArray(new Method[methodMap.keySet().size()]);
    }

    public abstract void createPyForClass(Class<?> clazz) throws IOException;
}


