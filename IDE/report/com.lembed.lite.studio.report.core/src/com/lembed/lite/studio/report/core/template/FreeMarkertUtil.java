package com.lembed.lite.studio.report.core.template;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

@SuppressWarnings("javadoc")
public class FreeMarkertUtil {
	public static void main(String[] args) throws Exception {

        // 1. Configure FreeMarker
        //
        // You should do this ONLY ONCE, when your application starts,
        // then reuse the same Configuration object elsewhere.

        Configuration cfg = new Configuration(new Version(2, 3, 20));

        // Where do we load the templates from:
        cfg.setClassForTemplateLoading(FreeMarkertUtil.class, "templates"); //$NON-NLS-1$

        // Some other recommended settings:
        //cfg.setIncompatibleImprovements(new Version(2, 3, 22));
        cfg.setDefaultEncoding("UTF-8"); //$NON-NLS-1$
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // 2. Proccess template(s)
        //
        // You will do this for several times in typical applications.

        // 2.1. Prepare the template input:

        Map<String, Object> input = new HashMap<>();

        input.put("title", "Vogella example"); //$NON-NLS-1$ //$NON-NLS-2$

        input.put("exampleObject", new FreeMarkerObj("Java object", "me")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        List<FreeMarkerObj> systems = new ArrayList<>();
        systems.add(new FreeMarkerObj("Android", "Google")); //$NON-NLS-1$ //$NON-NLS-2$
        systems.add(new FreeMarkerObj("iOS States", "Apple")); //$NON-NLS-1$ //$NON-NLS-2$
        systems.add(new FreeMarkerObj("Ubuntu", "Canonical")); //$NON-NLS-1$ //$NON-NLS-2$
        systems.add(new FreeMarkerObj("Windows7", "Microsoft")); //$NON-NLS-1$ //$NON-NLS-2$
        
        input.put("systems", systems); //$NON-NLS-1$

        // 2.2. Get the template

        Template template = cfg.getTemplate("helloworld.ftl"); //$NON-NLS-1$

        // 2.3. Generate the output

        // Write output to the console
        Writer consoleWriter = new OutputStreamWriter(System.out);
        template.process(input, consoleWriter);

        // For the sake of example, also write output into a file:
        try (Writer fileWriter = new FileWriter(new File("output.html"))) { //$NON-NLS-1$
            template.process(input, fileWriter);
        } 
	}
}
