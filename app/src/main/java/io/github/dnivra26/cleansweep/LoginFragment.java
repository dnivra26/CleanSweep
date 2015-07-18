package io.github.dnivra26.cleansweep;


import android.app.Fragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import io.github.dnivra26.cleansweep.models.Issue;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {


    @ViewById(R.id.fab)
    FloatingActionButton floatingActionButton;

    @ViewById(R.id.issue_list)
    ListView issueList;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment_();
    }

    @ItemClick(R.id.issue_list)
    public void onIssueClick(Issue issue) {
        ((MainActivity) getActivity()).onIssueClicked(issue);
    }

    @Click(R.id.fab)
    public void addNewIssue() {
        startActivity(new Intent(getActivity(), NewIssueActivity_.class));
    }

    @AfterViews
    public void init() {
        IssueListAdapter issueListAdapter = new IssueListAdapter(getActivity());
        issueList.setAdapter(issueListAdapter);
        issueListAdapter.notifyDataSetChanged();
    }

}
