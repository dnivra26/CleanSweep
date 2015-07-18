package io.github.dnivra26.cleansweep;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;

import io.github.dnivra26.cleansweep.models.Issue;
import io.github.dnivra26.cleansweep.models.Taken;


@EFragment(R.layout.fragment_progress_tracker)
public class ProgressTracker extends Fragment {

    private Issue issue;

    ParseFile workImageFile;

    @ViewById(R.id.issue_status_image)
    ParseImageView issueStatusImage;

    @ViewById(R.id.work_description)
    EditText workDescription;

    @ViewById(R.id.issue_title)
    TextView activeIssueTitle;

    @Click(R.id.issue_status_image)
    public void takePicture() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }


    public static ProgressTracker newInstance(Issue issue) {
        ProgressTracker fragment = new ProgressTracker_();
        fragment.setIssue(issue);
        return fragment;
    }

    public ProgressTracker() {
        // Required empty public constructor
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @AfterViews
    public void init() {
        activeIssueTitle.setText(issue.getTitle());
        ParseQuery parseQuery = new ParseQuery("Taken");
        parseQuery.whereEqualTo("issue", issue);

        try {
            Taken taken = (Taken) parseQuery.find().get(0);
            workDescription.setText(taken.getDescription());

            ParseFile photoFile = taken.getParseFile("photo");
            if (photoFile != null) {
                issueStatusImage.setParseFile(photoFile);
                issueStatusImage.loadInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        // nothing to do
                    }
                });
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        issueStatusImage.setImageBitmap(bp);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        workImageFile = new ParseFile("issue_image.jpg", byteArray);
    }

    @Click(R.id.save_work)
    public void saveWork() {
        ParseQuery parseQuery = new ParseQuery("Taken");
        parseQuery.whereEqualTo("issue", issue);

        try {
            Taken taken = (Taken) parseQuery.find().get(0);
            workImageFile.save();
            taken.setPhotoFile(workImageFile);
            taken.setDescription(workDescription.getText().toString());
            taken.save();
            Toast.makeText(getActivity(), "Successfully Saved", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Click(R.id.submit_work)
    public void submitWork() {
        ParseQuery parseQuery = new ParseQuery("Taken");
        parseQuery.whereEqualTo("issue", issue);

        try {
            Taken taken = (Taken) parseQuery.find().get(0);
            workImageFile.save();
            taken.setPhotoFile(workImageFile);
            taken.setDescription(workDescription.getText().toString());
            taken.setCompleted(true);
            taken.save();
            Toast.makeText(getActivity(), "Successfully submitted", Toast.LENGTH_SHORT).show();
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Submit Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
