package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.CategoryColumns;

import static com.artjoker.core.database.constants.ExtendedColumns.PARENT_ID;
import static com.stockroompro.models.columns.CategoryColumns.IMAGE_URL;
import static com.stockroompro.models.columns.CategoryColumns.SYSTEM_NAME;

/**
 * Created by alexsergienko on 18.03.15.
 */
public class Category implements CategoryColumns{

    @Expose
    private long id;

    @Expose
    @SerializedName(CREATED_AT)
    private long createdAt;

    /**
     * UNIX time = (android millis)/1000
     */
    @Expose
    @SerializedName(UPDATED_AT)
    private long updatedAt;

    @Expose
    @SerializedName(PARENT_ID)
    private long parentId;

    @Expose
    private String name;

    @Expose
    private String img;

    @Expose
    @SerializedName(SYSTEM_NAME)
    private String systemName;

    @Expose
    private String description;

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Category{");
        sb.append("id=").append(id);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", parentId=").append(parentId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", systemName='").append(systemName).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", updatedAt='").append(updatedAt).append('\'');
        sb.append(", img='").append(img).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
