package io.github.dnivra26.cleansweep;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.github.dnivra26.cleansweep.models.Bid;
import io.github.dnivra26.cleansweep.models.Issue;
import io.github.dnivra26.cleansweep.models.Taken;


@EFragment(R.layout.fragment_progress_tracker)
public class ProgressTracker extends Fragment {

    private Issue issue;

    ParseFile workImageFile;

    @ViewById(R.id.issue_status_image)
    ParseImageView issueStatusImage;

    @ViewById(R.id.work_description)
    EditText workDescription;

    @ViewById(R.id.issue_title)
    TextView activeIssueTitle;

    @Click(R.id.issue_status_image)
    public void takePicture() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }


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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Working Issue");
        activeIssueTitle.setText(issue.getTitle());
        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(getActivity());
        progressDialog.show();

        ParseQuery parseQuery = new ParseQuery("Taken");
        parseQuery.whereEqualTo("issue", issue);

        parseQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                Taken taken = (Taken) list.get(0);
                workDescription.setText(taken.getDescription());

                ParseFile photoFile = taken.getParseFile("photo");
                if (photoFile != null) {
                    issueStatusImage.setParseFile(photoFile);
                    issueStatusImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            // nothing to do
                        }
                    });
                }
                progressDialog.dismiss();
            }

            @Override
            public void done(Object o, Throwable throwable) {
                Taken taken = (Taken) ((List) o).get(0);
                workDescription.setText(taken.getDescription());

                ParseFile photoFile = taken.getParseFile("photo");
                if (photoFile != null) {
                    issueStatusImage.setParseFile(photoFile);
                    issueStatusImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            // nothing to do
                        }
                    });
                }
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data.getExtras() != null) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            issueStatusImage.setImageBitmap(bp);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            workImageFile = new ParseFile("issue_image.jpg", byteArray);
        }
    }

    @Click(R.id.save_work)
    public void saveWork() {
        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(getActivity());
        progressDialog.show();

        ParseQuery parseQuery = new ParseQuery("Taken");
        parseQuery.whereEqualTo("issue", issue);

        parseQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                return;
            }

            @Override
            public void done(Object o, Throwable throwable) {
                final Taken taken = (Taken) ((List) o).get(0);
                workImageFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            taken.setPhotoFile(workImageFile);
                            taken.setDescription(workDescription.getText().toString());
                            taken.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                progressDialog.dismiss();
            }
        });

    }

    @Click(R.id.submit_work)
    public void submitWork() {
        ProgressDialog progressDialog = UiUtil.buildProgressDialog(getActivity());
        progressDialog.show();

        ParseQuery parseQuery = new ParseQuery("Taken");
        parseQuery.whereEqualTo("issue", issue);

        parseQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {
                final Taken taken = (Taken) ((List) o).get(0);
                workImageFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            taken.setPhotoFile(workImageFile);
                            taken.setDescription(workDescription.getText().toString());
                            taken.setCompleted(true);
                            taken.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(), "Successfully submitted", Toast.LENGTH_SHORT).show();
                                        ParseQuery parseQuery2 = new ParseQuery("Bid");
                                        parseQuery2.whereEqualTo("issueId", issue.getObjectId());
                                        parseQuery2.findInBackground(new FindCallback() {
                                            @Override
                                            public void done(List list, ParseException e) {

                                            }

                                            @Override
                                            public void done(Object o, Throwable throwable) {
                                                for (Object o1 : ((List) o)) {
                                                    Bid bid = ((Bid) o1);
                                                    final ParsePush parsePush = new ParsePush();
                                                    parsePush.setMessage(issue.getTitle() + "has been completed");
                                                    final ParseQuery query = ParseInstallation.getQuery();
                                                    bid.getUser().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                                        @Override
                                                        public void done(ParseObject parseObject, ParseException e) {
                                                            query.whereEqualTo("username", ((ParseUser) parseObject).getUsername());
                                                            parsePush.setQuery(query);
                                                            parsePush.sendInBackground();
                                                        }
                                                    });

                                                }
                                                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                                                getActivity().finish();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), "Submit Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });
    }
}
