package com.stockroompro.models.spinners;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alexandr.Bagach on 30.09.2015.
 */
public class Location {

    public interface LocationConfig {
        String ID = "id";
        String NAME = "name";
        String PARENT_ID = "parent_id";
        String LOCATION_TYPE = "location_type";
    }

    public long locationId;
    public String locationName;
    public long parentId;
    public int locationType;

    public Location(long locationId, String locationName, long parentId, int locationType) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.parentId = parentId;
        this.locationType = locationType;
    }

    public Location(String locationString) {
        fromJson(locationString);
    }

    public String getLocationName() {
        return locationName;
    }

    @Override
    public String toString() {
        return "id :" + this.locationId + " name: " + this.locationName;
    }

    public String toJson() {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put(LocationConfig.ID, this.locationId);
            jObj.put(LocationConfig.NAME, this.locationName);
            jObj.put(LocationConfig.PARENT_ID, this.parentId);
            jObj.put(LocationConfig.LOCATION_TYPE, this.locationType);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return (jObj != null) ? jObj.toString() : null;
    }

    public boolean fromJson(String strJson) {
        try {
            JSONObject jObj = new JSONObject(strJson);
            this.locationId = jObj.getLong(LocationConfig.ID);
            this.locationName = jObj.getString(LocationConfig.NAME);
            this.parentId = jObj.getLong(LocationConfig.PARENT_ID);
            this.locationType = jObj.getInt(LocationConfig.LOCATION_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }
}