package io.github.dnivra26.cleansweep;

import android.app.Fragment;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.github.dnivra26.cleansweep.models.Bid;
import io.github.dnivra26.cleansweep.models.Issue;
import io.github.dnivra26.cleansweep.models.Taken;

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

    @ViewById(R.id.add_fund_layout)
    LinearLayout addFundLayout;

    @ViewById(R.id.new_bid_amount)
    EditText newBidAmount;

    public static IssueDetailFragment newInstance(Issue issue) {
        IssueDetailFragment issueDetailFragment = new IssueDetailFragment_();
        issueDetailFragment.setIssue(issue);
        return issueDetailFragment;
    }

    public IssueDetailFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        issueTitle.setText(issue.getTitle());
        issueDescription.setText(issue.getDescription());
        issueLocation.setText(issue.getLocation());
        issueBid.setText(String.valueOf(issue.getBid()));
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Issue Detail");

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

    @Click(R.id.add_bid)
    public void addBidToIssue() {
        addFundLayout.setVisibility(View.VISIBLE);
    }

    @Click(R.id.add_fund_ok)
    public void addFundConfirm() {
        Bid bid = new Bid();
        bid.setBid(Long.valueOf(newBidAmount.getText().toString()));
        bid.setUser(ParseUser.getCurrentUser());
        bid.setParent(issue);

        try {
            bid.save();
            issue.setBid(issue.getBid() + bid.getBid());
            issue.save();
            Toast.makeText(getActivity(), "Bid added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));

            getActivity().finish();
        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Bid add failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Click(R.id.take_up)
    public void signUpForTask() {
        Taken taken = new Taken();
        taken.setUser(ParseUser.getCurrentUser());
        taken.setCompleted(false);
        taken.setIssue(issue);
        try {
            taken.save();
            Toast.makeText(getActivity(), "Successfully signed up for task", Toast.LENGTH_LONG).show();
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();


            ParseQuery parseQuery = new ParseQuery("Bid");
            parseQuery.whereEqualTo("issueId", issue.getObjectId());

            List<Bid> bidList = parseQuery.find();

            for (Bid bid : bidList) {

                ParsePush parsePush = new ParsePush();
                parsePush.setMessage(issue.getTitle() + "has been Taken Up");
                ParseQuery query = ParseInstallation.getQuery();
                query.whereEqualTo("username", bid.getUser().fetchIfNeeded().getUsername());
                parsePush.setQuery(query);
                parsePush.send();
            }


        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Failed to sign up for task", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Issue getIssue() {
        return issue;
    }
}
