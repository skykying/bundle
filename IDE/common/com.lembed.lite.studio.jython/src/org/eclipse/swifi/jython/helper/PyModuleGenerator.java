package org.eclipse.swifi.jython.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class PyModuleGenerator extends ClassGenerator {
    protected PyModuleGenerator(String outputDir) {
        super(outputDir);
    }

    public void createPyForClass(Class clazz) throws IOException {
        File directory = createDirectory(clazz);
        String classContent = generateClassAsModule(clazz);
        if (classContent == null) {
            return;
        }
        File classFile = new File(directory, clazz.getSimpleName() + ".py");
        FileWriter fileWriter = new FileWriter(classFile);
        fileWriter.write(classContent);
        fileWriter.close();
    }

    private String generateMethodForModule(Method m) {
        Class<?>[] parameterTypes = m.getParameterTypes();
        StringBuilder sb = new StringBuilder();
        int modifiers = m.getModifiers();
        String prefix = "";
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


    private String generateClassAsModule(Class clazz) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = new Field[0];

        try {
            fields = clazz.getFields();
        } catch (Throwable e) {
            System.err.println("Get Field error:" + e.getMessage());
        }
        for (Field f : fields) {
            int modifiers = f.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                if (!IGNORED_FIELDS.contains(f.getName())) {
                    sb.append(indent(makeAField(f), 0));
                    sb.append(LINE_SEPARATOR);
                }
            }
        }
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        Method[] methods = new Method[0];
        try {
            methods = clazz.getMethods();
        } catch (Throwable e) {
            System.err.println("Get Method error:" + e.getMessage());
        }
        methods = filterOverrideMethods(methods);
        for (Method m : methods) {
            int modifiers = m.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                String name = m.getName();
                if (!IGNORED_METHODS.contains(name)) {
                    sb.append(indents(generateMethodForModule(m), 0));
                    sb.append(LINE_SEPARATOR);
                    sb.append(indents(PASS, 2));
                    sb.append(LINE_SEPARATOR);
                    sb.append(LINE_SEPARATOR);
                    sb.append(LINE_SEPARATOR);
                }
            }
        }
        return sb.toString();
    }
}