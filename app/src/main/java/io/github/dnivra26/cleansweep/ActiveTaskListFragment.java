package io.github.dnivra26.cleansweep;

import android.app.Fragment;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseQuery;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import io.github.dnivra26.cleansweep.models.Issue;
import io.github.dnivra26.cleansweep.models.Taken;


@EFragment(R.layout.fragment_active_task_list)
public class ActiveTaskListFragment extends Fragment {

    @ViewById(R.id.active_task_list)
    ListView activeTaskList;

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
        ActiveTaskListAdapter activeTaskListAdapter = new ActiveTaskListAdapter(getActivity());
        activeTaskList.setAdapter(activeTaskListAdapter);
        activeTaskListAdapter.notifyDataSetChanged();
    }


}
