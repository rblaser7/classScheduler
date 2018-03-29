package edu.byu.cs.rtblaser.classscheduler.view.adapters;

import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.util.List;

import edu.byu.cs.rtblaser.classscheduler.R;
import edu.byu.cs.rtblaser.classscheduler.ScheduleActivity;
import edu.byu.cs.rtblaser.classscheduler.model.Section;
import edu.byu.cs.rtblaser.classscheduler.view.CourseInfoDialogFragment;

/**
 * Created by RyanBlaser on 10/11/17.
 */

public class MyScheduleRecyclerViewAdapter extends RecyclerView.Adapter<MyScheduleRecyclerViewAdapter.Holder> {
    private List<Section> data;
    private Context context;

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mCourseName;
        public TextView mSectionNumber;
        public TextView mSectionTime;

        public Holder(View itemLayoutView) {
            super(itemLayoutView);
            mCourseName = (TextView) itemLayoutView.findViewById(R.id.schedule_course_name);
            mSectionNumber = (TextView) itemLayoutView.findViewById(R.id.schedule_section_number);
            mSectionTime = (TextView) itemLayoutView.findViewById(R.id.schedule_section_time);
        }
        @Override
        public void onClick(View v) {
            initiateCourseInfoDialog(this.getAdapterPosition());
        }
    }

    public MyScheduleRecyclerViewAdapter(List<Section> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        Holder holder = new Holder(view);
        view.setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Section section = data.get(position);
        holder.mCourseName.setText(section.getCourseTitle());
        holder.mSectionNumber.setText("Sec " + section.getSectionNumber());
        holder.mSectionTime.setText(section.displayDaysAndTimes());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void initiateCourseInfoDialog(int index) {
        CourseInfoDialogFragment dialog = new CourseInfoDialogFragment();
        dialog.setCourseInfo(data.get(index).print());
        dialog.show(((ScheduleActivity)context).getSupportFragmentManager(), "CourseInfoDialogFragment");
    }
}
