package io.github.dnivra26.cleansweep;

import android.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import io.github.dnivra26.cleansweep.models.Issue;


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
    public void onActiveTaskClick(Issue issue) {
        ((MainActivity) getActivity()).onActiveTaskClick(issue);
    }

    @AfterViews
    public void init() {
        ActiveTaskListAdapter activeTaskListAdapter = new ActiveTaskListAdapter(getActivity());
        activeTaskList.setAdapter(activeTaskListAdapter);
        activeTaskListAdapter.notifyDataSetChanged();
    }


}
