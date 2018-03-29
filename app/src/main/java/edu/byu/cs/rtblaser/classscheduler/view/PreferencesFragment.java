package edu.byu.cs.rtblaser.classscheduler.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import edu.byu.cs.rtblaser.classscheduler.ScheduleActivity;
import edu.byu.cs.rtblaser.classscheduler.SchedulerActivity;
import edu.byu.cs.rtblaser.classscheduler.api.ApiConnector;
import edu.byu.cs.rtblaser.classscheduler.model.Conflict;
import edu.byu.cs.rtblaser.classscheduler.model.Preference;
import edu.byu.cs.rtblaser.classscheduler.model.Course;
import edu.byu.cs.rtblaser.classscheduler.model.Scheduler;
import edu.byu.cs.rtblaser.classscheduler.model.Section;
import edu.byu.cs.rtblaser.classscheduler.view.adapters.ViewHolderThing;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.byu.cs.rtblaser.classscheduler.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreferencesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferencesFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private CheckBox mMonBox;
    private CheckBox mTueBox;
    private CheckBox mWedBox;
    private CheckBox mThuBox;
    private CheckBox mFriBox;
    private CheckBox mSatBox;
    private RelativeLayout mListContainer;
    private List<RelativeLayout> mDayLists = new ArrayList<>();
    private int currentDayIndex = 0;
//    private List<ViewHolderThing> mHolders = new ArrayList<>();
    private TabLayout mTabs;
    private Button mGenerateSchedule;
    private View mProgressView;
    private static int sectionsCompleted = 0;
    private View mContainer;


    public PreferencesFragment() {
        // Required empty public constructor
    }

    public static PreferencesFragment newInstance() {
        PreferencesFragment fragment = new PreferencesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);
        mMonBox = (CheckBox) view.findViewById(R.id.mon_check_box);
        mMonBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Preference.SINGLETON.setDayInDays(0, isChecked);
            }
        });

        mTueBox = (CheckBox) view.findViewById(R.id.tue_check_box);
        mTueBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Preference.SINGLETON.setDayInDays(1, isChecked);
            }
        });

        mWedBox = (CheckBox) view.findViewById(R.id.wed_check_box);
        mWedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Preference.SINGLETON.setDayInDays(2, isChecked);
            }
        });

        mThuBox = (CheckBox) view.findViewById(R.id.thu_check_box);
        mThuBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Preference.SINGLETON.setDayInDays(3, isChecked);
            }
        });

        mFriBox = (CheckBox) view.findViewById(R.id.fri_check_box);
        mFriBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Preference.SINGLETON.setDayInDays(4, isChecked);
            }
        });

        mSatBox = (CheckBox) view.findViewById(R.id.sat_check_box);
        mSatBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Preference.SINGLETON.setDayInDays(5, isChecked);
            }
        });

        for (int i = 0; i < 6; i++) {
            RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(
                    R.layout.preferred_time, null);
            final ViewHolderThing holder = new ViewHolderThing();
            final int day = i;
            holder.setmAnyTimeBox((CheckBox) layout.findViewById(R.id.any_time_checkbox));
            holder.getmAnyTimeBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Preference.SINGLETON.setTimeInDayToTimes(day, 0, isChecked);
                    CheckBox morn = holder.getmMorningBox();
                    CheckBox aft = holder.getmAfternoonBox();
                    CheckBox eve = holder.getmEveningBox();
                    if (isChecked) {
                        if (!morn.isChecked()) {
//                            Preference.SINGLETON.setTimeInDayToTimes(day, 1, isChecked);
                            morn.setChecked(true);
                        }
                        if (!aft.isChecked()) {
//                            Preference.SINGLETON.setTimeInDayToTimes(day, 2, isChecked);
                            aft.setChecked(true);
                        }
                        if (!eve.isChecked()) {
//                            Preference.SINGLETON.setTimeInDayToTimes(day, 3, isChecked);
                            eve.setChecked(true);
                        }
                    } else {
                        if (morn.isChecked() && aft.isChecked() && eve.isChecked()) {
                            morn.setChecked(false);
                            aft.setChecked(false);
                            eve.setChecked(false);
                        }
                    }
                }
            });
            holder.setmMorningBox((CheckBox) layout.findViewById(R.id.morning_checkbox));
            holder.getmMorningBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Preference.SINGLETON.setTimeInDayToTimes(day, 1, isChecked);
                    if (!isChecked) {
                        CheckBox any = holder.getmAnyTimeBox();
                        if (any.isChecked()) {
                            any.setChecked(false);
                        }
                    }
                }
            });
            holder.setmAfternoonBox((CheckBox) layout.findViewById(R.id.afternoon_checkbox));
            holder.getmAfternoonBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Preference.SINGLETON.setTimeInDayToTimes(day, 2, isChecked);
                    if (!isChecked) {
                        CheckBox any = holder.getmAnyTimeBox();
                        if (any.isChecked()) {
                            any.setChecked(false);
                        }
                    }
                }
            });
            holder.setmEveningBox((CheckBox) layout.findViewById(R.id.evening_checkbox));
            holder.getmEveningBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Preference.SINGLETON.setTimeInDayToTimes(day, 3, isChecked);
                    if (!isChecked) {
                        CheckBox any = holder.getmAnyTimeBox();
                        if (any.isChecked()) {
                            any.setChecked(false);
                        }
                    }
                }
            });

            mDayLists.add(layout);
        }

        mListContainer = (RelativeLayout) view.findViewById(R.id.preferences_list_container);

        mTabs = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentDayIndex = tab.getPosition();
                RelativeLayout layoutToAdd = mDayLists.get(currentDayIndex);
                RelativeLayout parent;
                if ((parent = (RelativeLayout)layoutToAdd.getParent()) != null) {
                    parent.removeView(layoutToAdd);
                }
                mListContainer.addView(layoutToAdd);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                mListContainer.removeView(mDayLists.get(pos));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                RelativeLayout layoutToAdd = mDayLists.get(currentDayIndex);
                RelativeLayout parent;
                if ((parent = (RelativeLayout)layoutToAdd.getParent()) == null) {
                    currentDayIndex = tab.getPosition();
                    mListContainer.addView(layoutToAdd);
                } else {
                    parent.removeView(layoutToAdd);
                    mListContainer.addView(layoutToAdd);
                }
            }
        });

        mGenerateSchedule = (Button) view.findViewById(R.id.generate_schedule);
        mGenerateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                List<Course> courses = CoursesFragment.coursesToAdd;
                ApiConnector.SINGLETON.setContext(getActivity());
                Map<String, List<Conflict>> conflicts = new HashMap<String, List<Conflict>>();
                for (Conflict conflict : ConflictsFragment.conflicts) {
                    String day = conflict.getDay();
                    if (day.equals("Tuesday")) {
                        day = "Tu";
                    } else if (day.equals("Thursday")) {
                        day = "Th";
                    }  else {
                        day = Character.toString(day.charAt(0));
                    }
                    if (conflicts.containsKey(day)) {
                        conflicts.get(day).add(conflict);
                    } else {
                        List<Conflict> newCon = new ArrayList<Conflict>();
                        newCon.add(conflict);
                        conflicts.put(day, newCon);
                    }
                }
                ApiConnector.SINGLETON.setCourses(courses);
                ApiConnector.SINGLETON.setConflicts(conflicts);
                for(Course course : courses) {
                    course = ApiConnector.SINGLETON.getSectionsForCourse(course);
                }
            }
        });

        if (!SchedulerActivity.isCoursesAreDone()) {
            mGenerateSchedule.setEnabled(false);
            mGenerateSchedule.setAlpha(.5f);
        }

        mProgressView = view.findViewById(R.id.generation_progress);
        mContainer = view.findViewById(R.id.preferences_container);

        TabLayout.Tab tabToSelect = mTabs.getTabAt(currentDayIndex);
        tabToSelect.select();

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static int getSectionsCompleted() {
        return sectionsCompleted;
    }

    public static void setSectionsCompleted(int sectionsCompleted) {
        PreferencesFragment.sectionsCompleted = sectionsCompleted;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mContainer.setVisibility(show ? View.GONE : View.VISIBLE);
            mContainer.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContainer.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
