package com.gmail.arnasrad.travelplanner.data;

/*
 * Data model ListItem
 *
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class ListItem {
    @PrimaryKey
    @NonNull
    private String itemId;
    private String message;
    private int colorResource;

    public ListItem(String itemId, String message, int colorResource) {
        this.itemId = itemId;
        this.message = message;
        this.colorResource = colorResource;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getColorResource() {
        return colorResource;
    }

    public void setColorResource(int colorResource) {
        this.colorResource = colorResource;
    }
}
