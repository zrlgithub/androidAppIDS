package com.example.ids;

public class ZeekXML {

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public ZeekXML(String name, String version, String description, String main) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.main = main;
    }
    public ZeekXML(String name) {
        this.name = name;}

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getMain() {
        return main;
    }

    private String name;
    private String version;
    private String description;
    private String main;


    @Override
    public String toString() {
        return name + " \n ";
    }
}
