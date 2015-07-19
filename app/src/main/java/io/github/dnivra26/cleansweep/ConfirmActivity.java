package io.github.dnivra26.cleansweep;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
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

import java.util.List;

import io.github.dnivra26.cleansweep.models.Issue;
import io.github.dnivra26.cleansweep.models.Taken;

@EActivity(R.layout.activity_confirm)
public class ConfirmActivity extends AppCompatActivity {


    @ViewById(R.id.finished_issue_title)
    TextView finishedIssueTitle;

    @ViewById(R.id.finished_issue_description)
    TextView finishedIssueDescription;

    @ViewById(R.id.finished_issue_image)
    ParseImageView finishedIssueImage;

    String takenId;
    private Taken taken;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takenId = getIntent().getStringExtra("task_id");
    }

    @AfterViews
    public void init() {
        getSupportActionBar().setTitle("Confirm Service");

        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
        progressDialog.show();


        ParseQuery parseQuery = new ParseQuery("Taken");
        parseQuery.whereEqualTo("objectId", takenId);

        parseQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {

                taken = (Taken) ((List) o).get(0);
                finishedIssueDescription.setText(taken.getDescription());

                ParseFile photoFile = taken.getParseFile("photo");
                if (photoFile != null) {
                    finishedIssueImage.setParseFile(photoFile);
                    finishedIssueImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            // nothing to do

                        }
                    });

                    taken.getIssue().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                Issue issue = (Issue) parseObject;
                                finishedIssueTitle.setText(issue.getTitle());
                            }

                            progressDialog.dismiss();

                        }
                    });
                }


            }
        });
    }

    @Click(R.id.yes)
    public void yes() {
        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
        progressDialog.show();
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
                progressDialog.dismiss();
                finish();
            }
        });
    }

    @Click(R.id.no)
    public void no() {
        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
        progressDialog.show();
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
                progressDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
