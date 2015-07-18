package io.github.dnivra26.cleansweep.models;

/**
 * Created by ganesshkumar on 18/07/15.
 */

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Issue")
public class Issue extends ParseObject {
    public Issue() {

    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location) {
        put("location", location);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile photo) {
        put("photo", photo);
    }

    public long getBid() {
        return getLong("bid");
    }

    public void setBid(float bid) {
        put("bid", bid);
    }


}
