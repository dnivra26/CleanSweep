package io.github.dnivra26.cleansweep;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import io.github.dnivra26.cleansweep.models.Issue;
import io.github.dnivra26.cleansweep.models.Taken;

public class ActiveTaskListAdapter extends ParseQueryAdapter<Taken> {
    public ActiveTaskListAdapter(Context context) {

        super(context, new ParseQueryAdapter.QueryFactory<Taken>() {
            public ParseQuery<Taken> create() {
                ParseQuery query = new ParseQuery("Taken");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereEqualTo("isCompleted", false);
                return query;
            }
        });

    }

    @Override
    public View getItemView(Taken taken, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.layout_issue_row, null);
        }

        super.getItemView(taken, v, parent);

        ParseImageView issueImage = (ParseImageView) v.findViewById(R.id.list_issue_image);


        Issue issue = taken.getIssue();

        ParseQuery parseQuery = new ParseQuery("Issue").whereEqualTo("objectId", issue.getObjectId());
        try {
            issue = (Issue) parseQuery.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        TextView titleTextView = (TextView) v.findViewById(R.id.list_issue_title);
        titleTextView.setText(issue.getTitle());

        TextView issueDescriptionTextView = (TextView) v
                .findViewById(R.id.list_issue_description);
        issueDescriptionTextView.setText(issue.getDescription());

        TextView issueBidTextView = (TextView) v
                .findViewById(R.id.list_issue_bid);
        issueBidTextView.setText(String.valueOf(issue.getBid()));

        return v;
    }
}
