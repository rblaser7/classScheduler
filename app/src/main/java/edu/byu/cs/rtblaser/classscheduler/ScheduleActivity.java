package edu.byu.cs.rtblaser.classscheduler;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;

import edu.byu.cs.rtblaser.classscheduler.model.Section;
import edu.byu.cs.rtblaser.classscheduler.view.CourseInfoDialogFragment;
import edu.byu.cs.rtblaser.classscheduler.view.EmailDialogFragment;

import android.widget.Toast;

import edu.byu.cs.rtblaser.classscheduler.model.Scheduler;
import edu.byu.cs.rtblaser.classscheduler.view.adapters.MyScheduleRecyclerViewAdapter;

public class ScheduleActivity extends AppCompatActivity implements EmailDialogFragment.EmailDialogListener {

    Context context;
    private RecyclerView mSchedule;
    private RecyclerView.Adapter mScheduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateEmailDialog(view);
//                composeEmail(new String[] {"rblaser7@gmail.com"}, "Schedule U: Your Schedule", "This is your schedule.");
            }
        });

        mSchedule = (RecyclerView) findViewById(R.id.schedule_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSchedule.setLayoutManager(linearLayoutManager);
        mScheduleAdapter = new MyScheduleRecyclerViewAdapter(Scheduler.SINGLETON.getMySchedule().getClasses(), this);
        mSchedule.setAdapter(mScheduleAdapter);
    }

    public void composeEmail(String[] addresses, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void initiateEmailDialog(View v) {
        DialogFragment dialog = new EmailDialogFragment();
        dialog.show(getSupportFragmentManager(), "EmailDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String emailAddress) {
        String body = createEmailBody();
        composeEmail(new String[] {emailAddress}, "Your Schedule by Schedule U", body);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public String createEmailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("Schedule U User,\n\n\tThank you for using Schedule U! The schedule we generated for you is listed below. When registration begins, go to your myBYU account and add these sections.\n\n");
        for (Section section : Scheduler.SINGLETON.getMySchedule().getClasses()) {
            sb.append(section.print() + '\n');
        }
        sb.append("Thanks again!\n\nSchedule U");
        return sb.toString();
    }
}
