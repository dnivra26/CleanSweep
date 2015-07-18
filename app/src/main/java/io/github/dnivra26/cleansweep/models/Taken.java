package io.github.dnivra26.cleansweep.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by ganesshkumar on 18/07/15.
 */

@ParseClassName("Taken")
public class Taken extends ParseObject {
    public Taken() {

    }

    public void setIssue(Issue issue) {
        put("issue", issue);
    }

    public Issue getIssue() {
        return (Issue) getParseObject("issue");
    }

    public void setUser(ParseUser parseUser) {
        put("user", parseUser);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile photo) {
        put("photo", photo);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public boolean hasCompleted() {
        return getBoolean("isCompleted");
    }

    public void setCompleted(boolean isCompleted) {
        put("isCompleted", isCompleted);
    }
}
