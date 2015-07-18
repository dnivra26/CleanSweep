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

import io.github.dnivra26.cleansweep.models.Issue;

public class ActiveTaskListAdapter extends ParseQueryAdapter<Issue> {
    public ActiveTaskListAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Issue>() {
            public ParseQuery<Issue> create() {
                ParseQuery query = new ParseQuery("Issue");
                return query;
            }
        });
    }

    @Override
    public View getItemView(Issue issue, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.layout_issue_row, null);
        }

        super.getItemView(issue, v, parent);

        ParseImageView issueImage = (ParseImageView) v.findViewById(R.id.list_issue_image);
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
