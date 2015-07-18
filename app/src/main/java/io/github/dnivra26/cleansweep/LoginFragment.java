package io.github.dnivra26.cleansweep;


import android.app.Fragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {


    @ViewById(R.id.fab)
    FloatingActionButton floatingActionButton;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment_();
    }

    @Click(R.id.fab)
    public void addNewIssue() {
        startActivity(new Intent(getActivity(), NewIssueActivity_.class));
    }


}
