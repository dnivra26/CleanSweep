package io.github.dnivra26.cleansweep;

import android.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import io.github.dnivra26.cleansweep.models.Issue;

@EFragment(R.layout.activity_issue_detail)
public class IssueDetailFragment extends Fragment {

    private Issue issue;

    @ViewById(R.id.issue_image)
    ParseImageView issueImage;

    @ViewById(R.id.label_issue_title)
    TextView issueTitle;

    @ViewById(R.id.label_issue_description)
    TextView issueDescription;

    @ViewById(R.id.label_issue_location)
    TextView issueLocation;

    @ViewById(R.id.label_total_bid)
    TextView issueBid;

    public static IssueDetailFragment newInstance(Issue issue) {
        IssueDetailFragment issueDetailFragment = new IssueDetailFragment_();
        issueDetailFragment.setIssue(issue);
        return issueDetailFragment;
    }

    public IssueDetailFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init(){
        issueTitle.setText(issue.getTitle());
        issueDescription.setText(issue.getDescription());
        issueLocation.setText(issue.getLocation());
        issueBid.setText(String.valueOf(issue.getBid()));

        ParseFile photoFile = issue.getParseFile("photo");
        if (photoFile != null) {
            issueImage.setParseFile(photoFile);
            issueImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Issue getIssue() {
        return issue;
    }
}
