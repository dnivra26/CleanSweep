package io.github.dnivra26.cleansweep.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by ganesshkumar on 18/07/15.
 */

@ParseClassName("Bid")
public class Bid extends ParseObject {

    public long getBid() {
        return getLong("bid");
    }

    public void setBid(float bid) {
        put("bid", bid);
    }

    public void setParent(Issue issue) {
        setIssueId(issue.getObjectId());
        put("parent", issue);
    }

    public void setSatisfied(boolean isSatisfied) {
        put("isSatisified", isSatisfied);
    }

    public boolean isSatisfied() {
        return getBoolean("isSatisified");
    }

    public Issue getParent() {
        return (Issue) getParseObject("parent");
    }

    public void setUser(ParseUser parseUser) {
        put("user", parseUser);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setIssueId(String issueId) {
        put("issueId", issueId);
    }

}
