package io.github.dnivra26.cleansweep.models;

/**
 * Created by ganesshkumar on 18/07/15.
 */

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
public class Issue {
    private long issueId;
    private String title;
    private String location;
    private Bitmap photo;
    private float bid;
}
