package io.github.dnivra26.cleansweep;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.github.dnivra26.cleansweep.models.Issue;
import io.github.dnivra26.cleansweep.models.Taken;


@EFragment(R.layout.fragment_active_task_list)
public class ActiveTaskListFragment extends Fragment {

    @ViewById(R.id.active_task_list)
    ListView activeTaskList;

    @ViewById(R.id.no_active_issues_label)
    TextView noActiveIssuesLabel;

    public static ActiveTaskListFragment newInstance() {
        ActiveTaskListFragment fragment = new ActiveTaskListFragment_();
        return fragment;
    }

    public ActiveTaskListFragment() {
        // Required empty public constructor
    }

    @ItemClick(R.id.active_task_list)
    public void onActiveTaskClick(Taken taken) {
        Issue issue = taken.getIssue();
        ParseQuery parseQuery = new ParseQuery("Issue").whereEqualTo("objectId", issue.getObjectId());
        try {
            issue = (Issue) parseQuery.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((MainActivity) getActivity()).onActiveTaskClick(issue);
    }

    @AfterViews
    public void init() {
        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(getActivity());
        ActiveTaskListAdapter activeTaskListAdapter = new ActiveTaskListAdapter(getActivity());
        activeTaskListAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Taken>() {
            @Override
            public void onLoading() {
                progressDialog.show();
            }

            @Override
            public void onLoaded(List<Taken> list, Exception e) {
                if (list.size() > 0) {
                    noActiveIssuesLabel.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }
        });
        activeTaskList.setAdapter(activeTaskListAdapter);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Active Tasks");
    }


}
