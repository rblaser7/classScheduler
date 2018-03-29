package edu.byu.cs.rtblaser.classscheduler;

import android.app.ActionBar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import edu.byu.cs.rtblaser.classscheduler.view.ConflictsFragment;
import edu.byu.cs.rtblaser.classscheduler.view.CoursesFragment;
import edu.byu.cs.rtblaser.classscheduler.view.PreferencesFragment;

public class SchedulerActivity extends AppCompatActivity
        implements ConflictsFragment.OnFragmentInteractionListener, CoursesFragment.OnFragmentInteractionListener, PreferencesFragment.OnFragmentInteractionListener {

    private android.support.v7.app.ActionBar.Tab coursesTab, conflictsTab, preferencesTab;
    private CoursesFragment mCoursesFragment;
    private ConflictsFragment mConflictsFragment;
    private PreferencesFragment mPreferencesFragment;

    private Context context = this;

    private static boolean coursesAreDone = false;
    private static int currentTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        context = this;
        mCoursesFragment = CoursesFragment.newInstance();
        mConflictsFragment = ConflictsFragment.newInstance();
        mPreferencesFragment = PreferencesFragment.newInstance();

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        coursesTab = mActionBar.newTab().setText("1. Courses");
        conflictsTab = mActionBar.newTab().setText("2. Conflicts");
        preferencesTab = mActionBar.newTab().setText("3. Preferences");

        coursesTab.setTabListener(new android.support.v7.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {
                currentTabIndex = 0;
                ft.replace(R.id.container, mCoursesFragment);
            }

            @Override
            public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {
//                ft.remove(mCoursesFragment);
            }

            @Override
            public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {

            }
        });

        conflictsTab.setTabListener(new android.support.v7.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {
                currentTabIndex = 1;
                ft.replace(R.id.container, mConflictsFragment);
            }

            @Override
            public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {
//                ft.remove(mConflictsFragment);
            }

            @Override
            public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {

            }
        });

        preferencesTab.setTabListener(new android.support.v7.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {
                currentTabIndex = 2;
                ft.replace(R.id.container, mPreferencesFragment);
            }

            @Override
            public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {
//                ft.remove(mPreferencesFragment);
            }

            @Override
            public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft) {

            }
        });

        mActionBar.addTab(coursesTab, true);
        mActionBar.addTab(conflictsTab);
        mActionBar.addTab(preferencesTab);
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

    public CoursesFragment getmCoursesFragment() {
        return mCoursesFragment;
    }

    public ConflictsFragment getmConflictsFragment() {
        return mConflictsFragment;
    }

    public PreferencesFragment getmPreferencesFragment() {
        return mPreferencesFragment;
    }

    public static boolean isCoursesAreDone() {
        return coursesAreDone;
    }

    public static void setCoursesAreDone(boolean coursesAreDone) {
        SchedulerActivity.coursesAreDone = coursesAreDone;
    }

    public static int getCurrentTabIndex() {
        return currentTabIndex;
    }

    public static void setCurrentTabIndex(int currentTabIndex) {
        SchedulerActivity.currentTabIndex = currentTabIndex;
    }

    @Override
    public void onBackPressed() {
        switch (currentTabIndex) {
            case 1 :
                coursesTab.select();
                break;
            case 2 :
                conflictsTab.select();
                break;
            default:
                break;
        }
    }
}
