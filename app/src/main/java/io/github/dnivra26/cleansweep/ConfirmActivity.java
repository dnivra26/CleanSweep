package io.github.dnivra26.cleansweep;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.github.dnivra26.cleansweep.models.Taken;

@EActivity(R.layout.activity_confirm)
public class ConfirmActivity extends AppCompatActivity {


    @ViewById(R.id.finished_issue_title)
    TextView finishedIssueTitle;

    @ViewById(R.id.finished_issue_description)
    TextView finishedIssueDescription;

    @ViewById(R.id.finished_issue_image)
    ParseImageView finishedIssueImage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @AfterViews
    public void init() {
        getSupportActionBar().setTitle("Confirm Service");
    }

    @Click(R.id.yes)
    public void yes() {
        Taken taken = new Taken();
        taken.getUser().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    ParseUser parseUser = (ParseUser) parseObject;
                    ParsePush parsePush = new ParsePush();
                    parsePush.setMessage("Amount has been credited to your account for the completed task :)");
                    final ParseQuery query = ParseInstallation.getQuery();
                    query.whereEqualTo("username", parseUser.getUsername());
                    parsePush.setQuery(query);
                    parsePush.sendInBackground();
                } else {
                    Toast.makeText(ConfirmActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click(R.id.no)
    public void no() {
        Taken taken = new Taken();
        taken.getUser().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    ParseUser parseUser = (ParseUser) parseObject;
                    ParsePush parsePush = new ParsePush();
                    parsePush.setMessage("Users were not satisfied with your service :(");
                    final ParseQuery query = ParseInstallation.getQuery();
                    query.whereEqualTo("username", parseUser.getUsername());
                    parsePush.setQuery(query);
                    parsePush.sendInBackground();
                } else {
                    Toast.makeText(ConfirmActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
