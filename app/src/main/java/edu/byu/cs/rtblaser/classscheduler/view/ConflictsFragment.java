package edu.byu.cs.rtblaser.classscheduler.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.rtblaser.classscheduler.R;
import edu.byu.cs.rtblaser.classscheduler.SchedulerActivity;
import edu.byu.cs.rtblaser.classscheduler.model.Conflict;
import edu.byu.cs.rtblaser.classscheduler.view.adapters.MyConflictsRecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConflictsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConflictsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConflictsFragment extends Fragment {
    public static List<Conflict> conflicts = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private Spinner mDaysDropdown;
    private Button mStartTimeButton;
    private Time mStartTime = null;
    private Button mEndTimeButton;
    private Time mEndTime = null;
    private Button mAddConflictButton;
    private static Button mSaveAndContinue;
    private RecyclerView mMyConflictsList;
    private RecyclerView.Adapter mConflictsListAdapter;
    private static TextView mMyConflictsText;

    public ConflictsFragment() {
        // Required empty public constructor
    }

    //TODO: Make transition from start time to end time better

    public static ConflictsFragment newInstance() {
        ConflictsFragment fragment = new ConflictsFragment();
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
        View view = inflater.inflate(R.layout.fragment_conflicts, container, false);


        mMyConflictsText = (TextView) view.findViewById(R.id.my_conflicts_test_view);

        mSaveAndContinue = (Button) view.findViewById(R.id.save_and_continue_conflicts);
        mSaveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
                ActionBar.Tab conflictsTab = ab.getTabAt(SchedulerActivity.getCurrentTabIndex()+1);
                ab.selectTab(conflictsTab);
            }
        });
        if (conflicts.size() == 0) {
//            mSaveAndContinue.setEnabled(false);
//            mSaveAndContinue.setAlpha(.5f);
            mMyConflictsText.setVisibility(View.INVISIBLE);
        }

        mDaysDropdown = (Spinner) view.findViewById(R.id.conflict_day_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.days)) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
                return true;
            }
        };
        mDaysDropdown.setAdapter(arrayAdapter);
        mDaysDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validateTimes();
                if (position != 0 && mStartTime == null) {
                    mStartTimeButton.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mStartTimeButton = (Button) view.findViewById(R.id.start_time_button);
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 12;
                int minute = 0;
                if (mStartTime != null) {
                    hour = mStartTime.getHours();
                    minute = mStartTime.getMinutes();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mStartTime = new Time(hourOfDay, minute, 0);
                        mStartTimeButton.setText(Conflict.formatTime(hourOfDay, minute));
                        validateTimes();
                        if (mEndTime == null) {
                            mEndTimeButton.performClick();
                        }
                    }
                }, hour, minute, false);
                timePickerDialog.setTitle("Start Time");
                timePickerDialog.show();
            }
        });

        mEndTimeButton = (Button) view.findViewById(R.id.end_time_button);
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 12;
                int minute = 0;
                if (mEndTime != null) {
                    hour = mEndTime.getHours();
                    minute = mEndTime.getMinutes();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mEndTime = new Time(hourOfDay, minute, 0);
                        mEndTimeButton.setText(Conflict.formatTime(hourOfDay, minute));
                        validateTimes();
                    }
                }, hour, minute, false);
                timePickerDialog.setTitle("End Time");
                timePickerDialog.show();
            }
        });
        
        mAddConflictButton = (Button) view.findViewById(R.id.add_conflict_button);
        mAddConflictButton.setEnabled(false);
        mAddConflictButton.setAlpha(.5f);
        mAddConflictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConflictToList();
            }
        });

        mMyConflictsList = (RecyclerView) view.findViewById(R.id.conflicts_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMyConflictsList.setLayoutManager(linearLayoutManager);
        mConflictsListAdapter = new MyConflictsRecyclerViewAdapter(conflicts);
        mMyConflictsList.setAdapter(mConflictsListAdapter);

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

    public void validateTimes() {
        if (mEndTime != null && mStartTime != null && !mDaysDropdown.getSelectedItem().toString().equals("Day")
                && mStartTime.before(mEndTime)) {
            mAddConflictButton.setEnabled(true);
            mAddConflictButton.setAlpha(1f);
        } else if (mEndTime != null && mStartTime != null && mStartTime.after(mEndTime) && mAddConflictButton.isEnabled()) {
            mAddConflictButton.setEnabled(false);
            mAddConflictButton.setAlpha(.5f);
        }
    }

    public void addConflictToList() {
        String day = mDaysDropdown.getSelectedItem().toString();
        Conflict conflictToAdd = new Conflict(day, mStartTime, mEndTime);
        conflicts.add(conflictToAdd);
        mConflictsListAdapter.notifyDataSetChanged();
        mStartTimeButton.setText("Start Time");
        mEndTimeButton.setText("End Time");
        mAddConflictButton.setEnabled(false);
        mAddConflictButton.setAlpha(.5f);
        mDaysDropdown.setSelection(0);
        mStartTime = null;
        mEndTime = null;
//        if (!mSaveAndContinue.isEnabled()) {
//            mSaveAndContinue.setEnabled(true);
//            mSaveAndContinue.setAlpha(1f);
//        }
        if (mMyConflictsText.getVisibility() == View.INVISIBLE) {
            mMyConflictsText.setVisibility(View.VISIBLE);
        }
    }

    public static void updateAllConflictsRemoved() {
//        mSaveAndContinue.setEnabled(false);
//        mSaveAndContinue.setAlpha(.5f);
        mMyConflictsText.setVisibility(View.INVISIBLE);
    }
}
