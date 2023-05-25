package com.example.mob104_app.Models;

public class Province {
    private String name;
    private String type;
    private String slug;
    private String name_with_type;
    private String path;
    private String path_with_type;
    private String code;

    public Province() {
    }

    public Province(String name, String type, String slug, String name_with_type, String path, String path_with_type, String code) {
        this.name = name;
        this.type = type;
        this.slug = slug;
        this.name_with_type = name_with_type;
        this.path = path;
        this.path_with_type = path_with_type;
        this.code = code;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName_with_type() {
        return name_with_type;
    }

    public void setName_with_type(String name_with_type) {
        this.name_with_type = name_with_type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath_with_type() {
        return path_with_type;
    }

    public void setPath_with_type(String path_with_type) {
        this.path_with_type = path_with_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
