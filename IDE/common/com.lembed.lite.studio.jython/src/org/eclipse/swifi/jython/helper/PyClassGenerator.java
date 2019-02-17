package org.eclipse.swifi.jython.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PyClassGenerator extends ClassGenerator {

    Pattern CLASS_NAME_PATTERN = Pattern.compile("class\\s+(\\w+).*:");

    protected PyClassGenerator(String outputDir) {
        super(outputDir);
    }

    public void createPyForClass(Class clazz) throws IOException {
        File directory = createDirectory(clazz);
        String classContent = generateClassAsPyClass(clazz);
        File initPy = new File(directory, INIT_PY);
        Map<String, StringBuilder> classStringMap = readClassesFromInitPy(initPy);
        classStringMap.put(clazz.getSimpleName(), new StringBuilder(classContent));
        String name = clazz.getName().replace('.', '/') + ".class";
        ClassLoader classLoader = clazz.getClassLoader();
        URL resourceURL = null;
        if (classLoader != null) {
            resourceURL = classLoader.getResource(name);
        }
        writeClassesFromInitPy(classStringMap, initPy, resourceURL);
    }

    public void createPyForClass0(Class clazz) throws IOException {
        File directory = createDirectory(clazz);
        String classContent = generateClassAsPyClass(clazz);
        File classFile = new File(directory, INIT_PY);
        FileWriter fileWriter = new FileWriter(classFile, true);
        fileWriter.write(classContent);
        fileWriter.close();
    }

    private Map<String, StringBuilder> readClassesFromInitPy(File initFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(initFile));
        String line;
        boolean inClass = false;
        Map<String, StringBuilder> maps = new HashMap<String, StringBuilder>();
        StringBuilder sb = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("class")) {
                inClass = true;
                Matcher matcher = CLASS_NAME_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String className = matcher.group(1);
                    sb = new StringBuilder();
                    maps.put(className, sb);
                }
            }
            if (inClass && sb != null) {
                sb.append(line).append(LINE_SEPARATOR);
            }
        }
        br.close();
        return maps;
    }

    private void writeClassesFromInitPy(Map<String, StringBuilder> classStringMap, File initPy, URL resourceURL) throws IOException {
        FileWriter fileWriter = new FileWriter(initPy);
        writePyHeader(fileWriter, initPy.getName(), resourceURL);
        for (StringBuilder classContent : classStringMap.values()) {
            fileWriter.write(classContent.toString());
        }
        fileWriter.close();
    }

    private String generateClassAsPyClass(Class clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(CLASS_TPL, clazz.getSimpleName()));
        sb.append(LINE_SEPARATOR);
        Field[] fields = new Field[0];
        try {
            fields = clazz.getFields();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        for (Field f : fields) {
            int modifiers = f.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                if (!IGNORED_FIELDS.contains(f.getName())) {
                    sb.append(indent(makeAField(f)));
                    sb.append(LINE_SEPARATOR);
                }
            }
        }
        sb.append(LINE_SEPARATOR);
        Method[] methods = new Method[0];
        try {
            methods = clazz.getMethods();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        methods = filterOverrideMethods(methods);
        for (Method m : methods) {
            int modifiers = m.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                String name = m.getName();
                if (!IGNORED_METHODS.contains(name)) {
                    sb.append(indents(generateMethodForPyClass(m)));
                    sb.append(indents(PASS, 2));
                    sb.append(LINE_SEPARATOR);
                    sb.append(LINE_SEPARATOR);
                }
            }
        }
        return sb.toString();
    }

    private String generateMethodForPyClass(Method m) {
        Class<?>[] parameterTypes = m.getParameterTypes();
        StringBuilder sb = new StringBuilder();
        int modifiers = m.getModifiers();
        String prefix = "";
        
        if (Modifier.isStatic(modifiers)) {
            prefix = STATIC_METHOD + LINE_SEPARATOR;
        } else {
            sb.append("self, ");
        }
        
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            String typeName;
            if (type.isArray()) {
                typeName = type.getComponentType().getSimpleName();
            } else {
                typeName = type.getSimpleName();
            }
            sb.append(typeName + (i > 0 ? i : "") + "=None");
            if (i < parameterTypes.length - 1) {
                sb.append(", ");
            }
        }

        return prefix + String.format(DEF_TPL, m.getName(), sb.toString());
    }
}
