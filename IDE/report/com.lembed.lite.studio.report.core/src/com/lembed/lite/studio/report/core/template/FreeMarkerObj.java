package com.lembed.lite.studio.report.core.template;

@SuppressWarnings("javadoc")
public class FreeMarkerObj {

    private String name;
    private String developer;

    public FreeMarkerObj(String name, String developer) {
        this.name = name;
        this.developer = developer;
    }

    public String getName() {
        return name;
    }

    public String getDeveloper() {
        return developer;
    }

}