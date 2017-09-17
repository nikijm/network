package com.jk.ndt.etl.entity.system;

import com.jk.ndt.etl.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/22.
 */
public class Menu extends BaseEntity  implements Serializable {

    private List<Menu> items;

    private String name;
    private String url;
    private String key;//和Resource中的key匹配  key唯一
    private String type;
    private String icon;
    private String route;
    private String notTree;

    public String getNotTree() {
        return notTree;
    }

    public void setNotTree(String notTree) {
        this.notTree = notTree;
    }

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(List<Menu> items) {
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
