package io.github.dnivra26.cleansweep.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ganesshkumar on 18/07/15.
 */

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
@ParseClassName("Bid")
public class Bid extends ParseObject{
    private long issueId;
    private ParseUser user;
    private float amount;
}
