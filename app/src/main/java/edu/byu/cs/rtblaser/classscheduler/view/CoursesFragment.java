package edu.byu.cs.rtblaser.classscheduler.view;

import android.support.v7.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.rtblaser.classscheduler.R;
import edu.byu.cs.rtblaser.classscheduler.SchedulerActivity;
import edu.byu.cs.rtblaser.classscheduler.api.ApiConnector;
import edu.byu.cs.rtblaser.classscheduler.model.Course;
import edu.byu.cs.rtblaser.classscheduler.model.Departments;
import edu.byu.cs.rtblaser.classscheduler.view.adapters.MyCoursesRecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CoursesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesFragment extends Fragment {
    private String currentDepartment = null;
    private String currentCourseNumber = null;

    private OnFragmentInteractionListener mListener;
    private Spinner mDepartmentDropdown;
    private Spinner mCourseNumbersDropdown;
    private ArrayAdapter<String> mDepartmentsAdapter;
    private static ArrayAdapter<String> mCourseNumsAdapter;
    private Button mAddCourseButton;
    private static Button mSaveAndContinue;
    private static TextView mMyCoursesText;
    private RecyclerView mMyCoursesList;
    private RecyclerView.Adapter mCourseListAdapter;
    public static List<Course> coursesToAdd = new ArrayList<>();
    public static List<String> courseNums = new ArrayList<>();
    public static Map<String,String[]> numberToCIdandTC = null;

    public CoursesFragment() {
        // Required empty public constructor
    }

    public static CoursesFragment newInstance() {
        CoursesFragment fragment = new CoursesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDepartmentsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Departments.SINGLETON.getDepartments()) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
                return true;
            }
        };

        courseNums.add("Course #");
        mCourseNumsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, courseNums) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
                return true;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_courses, container, false);

        mAddCourseButton = (Button) view.findViewById(R.id.add_course_button);
        mAddCourseButton.setEnabled(false);
        mAddCourseButton.setAlpha(.5f);
        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDepartment != null && currentCourseNumber != null) {
                    addCourseToList();
                }
            }
        });

        mSaveAndContinue = (Button) view.findViewById(R.id.save_and_continue_courses);
        mSaveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enable and click second tab
                SchedulerActivity.setCoursesAreDone(true);
                ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
                ActionBar.Tab conflictsTab = ab.getTabAt(SchedulerActivity.getCurrentTabIndex()+1);
                ab.selectTab(conflictsTab);
            }
        });

        mMyCoursesText = (TextView) view.findViewById(R.id.my_courses_text_view);
        if (coursesToAdd.size() == 0) {
            mSaveAndContinue.setEnabled(false);
            mSaveAndContinue.setAlpha(.5f);
            mMyCoursesText.setVisibility(View.INVISIBLE);
        }

        mDepartmentDropdown = (Spinner) view.findViewById(R.id.department_dropdown);
        mDepartmentDropdown.setAdapter(mDepartmentsAdapter);
        mDepartmentDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dep = parent.getItemAtPosition(position).toString();
                if (!dep.equals("Department")) {
                    currentDepartment = dep;
                    ApiConnector.SINGLETON.getCoursesFromDepartment(dep);
                    mCourseNumbersDropdown.setEnabled(true);
                    mCourseNumbersDropdown.setAlpha(1f);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCourseNumbersDropdown = (Spinner) view.findViewById(R.id.course_number_dropdown);
        mCourseNumbersDropdown.setEnabled(false);
        mCourseNumbersDropdown.setAlpha(.5f);
        mCourseNumbersDropdown.setAdapter(mCourseNumsAdapter);
        mCourseNumbersDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentDepartment != null) {
                    String num = parent.getItemAtPosition(position).toString();
                    if (!num.equals("Course #")) {
                        currentCourseNumber = num;
                        mAddCourseButton.setEnabled(true);
                        mAddCourseButton.setAlpha(1f);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMyCoursesList = (RecyclerView) view.findViewById(R.id.course_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMyCoursesList.setLayoutManager(linearLayoutManager);
        mCourseListAdapter = new MyCoursesRecyclerViewAdapter(coursesToAdd);
        mMyCoursesList.setAdapter(mCourseListAdapter);

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

    public void addCourseToList() {
        coursesToAdd.add(new Course(currentDepartment, currentCourseNumber, numberToCIdandTC.get(currentCourseNumber)[0], numberToCIdandTC.get(currentCourseNumber)[1]));
        mCourseListAdapter.notifyDataSetChanged();
        currentDepartment = null;
        currentCourseNumber = null;
        numberToCIdandTC = null;
        mAddCourseButton.setEnabled(false);
        mAddCourseButton.setAlpha(.5f);
        mCourseNumbersDropdown.setSelection(0);
        mCourseNumbersDropdown.setEnabled(false);
        mCourseNumbersDropdown.setAlpha(.5f);
        mDepartmentDropdown.setSelection(0);
        if (!mSaveAndContinue.isEnabled()) {
            mSaveAndContinue.setEnabled(true);
            mSaveAndContinue.setAlpha(1f);
        }
        if (mMyCoursesText.getVisibility() == View.INVISIBLE) {
            mMyCoursesText.setVisibility(View.VISIBLE);
        }
    }

    public static void updateAllCoursesRemoved() {
        mSaveAndContinue.setEnabled(false);
        mSaveAndContinue.setAlpha(.5f);
        mMyCoursesText.setVisibility(View.INVISIBLE);
    }

    public static Map<String, String[]> getNumberToCIdandTC() {
        return numberToCIdandTC;
    }

    public static void setNumberToCIdandTC(Map<String, String[]> numberToCIdandTC) {
        CoursesFragment.numberToCIdandTC = numberToCIdandTC;
    }

    public static List<String> getCourseNums() {
        return courseNums;
    }

    public static void setCourseNums(List<String> courseNums) {
        CoursesFragment.courseNums.clear();
        for (String num : courseNums) {
            CoursesFragment.courseNums.add(num);
        }
        mCourseNumsAdapter.notifyDataSetChanged();
    }
}
