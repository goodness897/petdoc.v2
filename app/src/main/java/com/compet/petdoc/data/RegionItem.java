package com.compet.petdoc.data;

/**
 * Created by Mu on 2016-12-01.
 */

public class RegionItem implements java.io.Serializable {

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
