package io.github.dnivra26.cleansweep;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;

import io.github.dnivra26.cleansweep.models.Bid;
import io.github.dnivra26.cleansweep.models.Issue;

@EActivity(R.layout.activity_new_issue)
public class NewIssueActivity extends AppCompatActivity {


    ParseFile issueImageFile;

    @ViewById(R.id.issue_image)
    ImageView issueImage;

    @ViewById(R.id.issue_title)
    EditText issueTitle;

    @ViewById(R.id.issue_description)
    EditText issueDescription;

    @ViewById(R.id.issue_location)
    EditText issueLocation;

    @ViewById(R.id.initial_bid)
    EditText initialBid;


    @Click(R.id.issue_image)
    public void takePicture() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);

    }

    @Click(R.id.create_issue)
    public void createNewIssue() {
        Issue newIssue = new Issue();
        newIssue.setTitle(issueTitle.getText().toString());
        newIssue.setDescription(issueDescription.getText().toString());
        newIssue.setLocation(issueLocation.getText().toString());
        newIssue.setBid(Long.valueOf(initialBid.getText().toString()));
        try {
            issueImageFile.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newIssue.setPhotoFile(issueImageFile);


        try {
            newIssue.save();
            Toast.makeText(this, "New Issue created!", Toast.LENGTH_LONG).show();

            Bid bid = new Bid();
            bid.setBid(newIssue.getBid());
            bid.setUser(ParseUser.getCurrentUser());
            bid.setParent(newIssue);

            bid.save();


            finish();
        } catch (ParseException e) {
            Toast.makeText(this, "Issue creation failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        issueImage.setImageBitmap(bp);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        issueImageFile = new ParseFile("issue_image.jpg", byteArray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_issue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
