package org.eclipse.swifi.jython.helper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: gongze
 * Date: 1/30/13
 * Time: 2:52 PM
 */
public class JythonHelper {

    public static final Pattern PATTERN_FROM_IMPORT = Pattern.compile("\\s*from\\s*(\\S+)\\s*import\\s+(\\w[\\w,\\p{Blank}]*)");

    public static final Pattern PATTERN_IMPORT = Pattern.compile("\\s*import\\s*(\\S+)");

    public static final Pattern PATTERN_FROM_IMPORT_MULTIPLE = Pattern.compile("\\s*from\\s*(\\S+)\\s*import\\s+\\(\\s*(.*)\\s*\\)", Pattern.MULTILINE);

    public static final String[] PREDEFINED_CLASSES = {};


    public static ClassGenerator getPyCls(String outputDir) {
    	ClassGenerator cg = new PyClassGenerator(outputDir);
    	return cg;
    }

    public ClassLoader loadJarLibrarys(String[] libs) {
        List<URL> urls = new ArrayList<URL>();
        ClassLoader classLoader = null;
        
        for (String name : libs) {
            try {
                if (name.endsWith("*")) {
                    name = name.substring(0, name.length() - 1);
                }
                File file = new File(name);
                if (file.isDirectory()) {
                    File[] files = file.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".jar");
                        }
                    });
                    for (File f : files) {
                        urls.add(f.toURI().toURL());
                    }
                } else {
                    urls.add(file.toURI().toURL());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
        return classLoader;
    }

    private static String[] getCommandLineFilesFromArgs(String[] args) {
        List<String> list = new ArrayList<String>();
        for (String arg : args) {
            String[] split = arg.split(";|:");
            list.addAll(Arrays.asList(split));
        }
        return list.toArray(new String[list.size()]);
    }

    public static Set<String> generatePys(String[] pyFiles, ClassLoader classLoader, ClassGenerator clsGenerator) {
        Set<String> classList = new HashSet<String>();
        classList.addAll(Arrays.asList(PREDEFINED_CLASSES));
        for (String f : pyFiles) {
            File file = new File(f);
            if (file.isFile()) {
                System.out.println("File:--->" + file.getName());
                classList.addAll(getClassDepListFromPyfile(file));
            } else {
                System.out.println("Directory:--->" + file.getAbsolutePath());
                File[] files = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".py");
                    }
                });
                if (files != null) {
                    for (File child : files) {
                        System.out.println("--->" + child.getName());
                        classList.addAll(getClassDepListFromPyfile(child));
                    }
                }
            }
        }

        for (String className : classList) {
            try {
                System.out.println("Generating:" + className);
                Class<?> clazz = Class.forName(className, false, classLoader);
                System.out.println(clazz);
                clsGenerator.createPyForClass(clazz);
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found:" + e.getMessage());
            } catch (IOException e) {
                System.err.println("IO Exception:" + e.getMessage());
            }
        }
		return classList;
    }

    private static Set<String> getClassDepListFromPyfile(File pyFile) {
        StringBuilder sb = new StringBuilder();
        Set<String> classes = new HashSet<String>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(pyFile));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                Matcher matcher = PATTERN_FROM_IMPORT.matcher(line);
                if (matcher.matches()) {
                    System.out.println(line);
                    String packageName = matcher.group(1);
                    String classSimpleName = matcher.group(2);
                    String[] names = classSimpleName.split(",");//for from xyz import Abc, Def, Ghi
                    for (String className : names) {
                        className = className.trim();
                        if (!className.isEmpty()) {
                            String[] split = className.split(" ");// for import Abc as abc
                            className = split[0].trim();
                            classes.add(packageName + "." + className);
                        }
                    }
                    continue;
                }

                matcher = PATTERN_IMPORT.matcher(line);
                if (matcher.matches()) {
                    String classSimpleName = matcher.group(1);
                    classes.add(classSimpleName);
                }
            }
            br.close();
            Matcher matcher = PATTERN_FROM_IMPORT_MULTIPLE.matcher(sb.toString());
            while (matcher.find()) {
                String packageName = matcher.group(1);
                String classSimpleName = matcher.group(2);
                String[] names = classSimpleName.split(",");//for from xyz import Abc, Def, Ghi
                for (String className : names) {
                    className = className.trim();
                    if (!className.isEmpty()) {
                        String[] split = className.split(" ");// for import Abc as abc
                        className = split[0].trim();
                        classes.add(packageName + "." + className);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }
}

