package com.stockroompro.models.spinners;

/**
 * Created by Alexandr.Bagach on 30.09.2015.
 */
public class SpinnerItem {

    private long id;
    private long parentId;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "id: " + id + " name: " + name + " parent: " + parentId;
    }
}
