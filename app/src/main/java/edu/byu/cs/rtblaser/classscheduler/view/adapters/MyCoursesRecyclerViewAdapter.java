package edu.byu.cs.rtblaser.classscheduler.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.byu.cs.rtblaser.classscheduler.R;
import edu.byu.cs.rtblaser.classscheduler.model.Course;
import edu.byu.cs.rtblaser.classscheduler.view.CoursesFragment;

/**
 * Created by RyanBlaser on 10/5/17.
 */

public class MyCoursesRecyclerViewAdapter extends RecyclerView.Adapter<MyCoursesRecyclerViewAdapter.Holder> {

    private List<Course> data;

    public interface MyViewHolderClickHandler {
        void onViewClicked(View caller, int whichOne);
        void onImageViewClicked(ImageView callerImage, int whichOne);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mCourseName;
        public ImageView mTrashCan;
        public MyViewHolderClickHandler mListener;

        public Holder(View itemLayoutView, MyViewHolderClickHandler listener) {
            super(itemLayoutView);
            mCourseName = (TextView) itemLayoutView.findViewById(R.id.course_name);
            mCourseName.setOnClickListener(this);
            mTrashCan = (ImageView) itemLayoutView.findViewById(R.id.trash_can);
            mTrashCan.setOnClickListener(this);
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            int whichOne = this.getAdapterPosition();
            if (v.getId() == mTrashCan.getId()){
                mListener.onImageViewClicked((ImageView)v, whichOne);
            } else {
                mListener.onViewClicked(v,whichOne);
            }
        }
    }

    public MyCoursesRecyclerViewAdapter(List<Course> data) {
        this.data = data;
    }
    @Override
    public MyCoursesRecyclerViewAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_item, parent, false);
        MyViewHolderClickHandler handler = new MyViewHolderClickHandler() {
            @Override
            public void onViewClicked(View caller, int whichOne) {
                // create course email_dialogue_layout
            }

            @Override
            public void onImageViewClicked(ImageView callerImage, int whichOne) {
                // remove course
                data.remove(whichOne);
                notifyDataSetChanged();
                if (data.size() == 0) {
                    CoursesFragment.updateAllCoursesRemoved();
                }
            }
        };

        Holder holder = new Holder(view, handler);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Course course = data.get(position);
        holder.mCourseName.setText(course.getDepartment() + " " + course.getCatalogNumber());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
