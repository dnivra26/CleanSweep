package io.github.dnivra26.cleansweep.models;

/**
 * Created by ganesshkumar on 18/07/15.
 */

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Issue")
public class Issue extends ParseObject {
    private String title;
    private String description;
    private String location;
    private ParseFile photo;
    private float bid;

    public Issue() {

    }

    public Issue(String title, String description, String location, ParseFile photo, float bid) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.photo = photo;
        this.bid = bid;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ParseFile getPhoto() {
        return photo;
    }

    public void setPhoto(ParseFile photo) {
        this.photo = photo;
    }

    public float getBid() {
        return bid;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }


}
