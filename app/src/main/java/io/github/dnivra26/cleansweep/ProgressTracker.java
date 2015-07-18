package io.github.dnivra26.cleansweep;

import android.app.Fragment;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import io.github.dnivra26.cleansweep.models.Issue;


@EFragment(R.layout.fragment_progress_tracker)
public class ProgressTracker extends Fragment {

    private Issue issue;

    @ViewById(R.id.issue_title)
    TextView activeIssueTitle;

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
    }
}
