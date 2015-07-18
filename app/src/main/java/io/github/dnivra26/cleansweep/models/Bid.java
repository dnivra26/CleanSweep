package io.github.dnivra26.cleansweep.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ganesshkumar on 18/07/15.
 */

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
public class Bid {
    private long bidId;
    private long issueId;
    private String username;
    private float amount;
}
