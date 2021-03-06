package io.github.dnivra26.cleansweep;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

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

    @ViewById(R.id.no_issues_label)
    TextView noIssuesLabel;
    private IssueListAdapter issueListAdapter;


    public LoginFragment() {
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

    public void init() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Issue List");
        issueListAdapter = new IssueListAdapter(getActivity());
        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(getActivity());
        issueListAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Issue>() {
            @Override
            public void onLoading() {
                progressDialog.show();
            }

            @Override
            public void onLoaded(List<Issue> list, Exception e) {
                if (list.size() > 0) {
                    noIssuesLabel.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }
        });
        issueList.setAdapter(issueListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}
