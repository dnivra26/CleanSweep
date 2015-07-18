package io.github.dnivra26.cleansweep.models;

/**
 * Created by ganesshkumar on 18/07/15.
 */

import android.graphics.Bitmap;

import com.parse.ParseFile;
import com.parse.ParseObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
public class Issue extends ParseObject {
    private String title;
    private String description;
    private String location;
    private ParseFile photo;
    private float bid;
}
