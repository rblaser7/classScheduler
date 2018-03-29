package edu.byu.cs.rtblaser.classscheduler.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.byu.cs.rtblaser.classscheduler.R;
import edu.byu.cs.rtblaser.classscheduler.model.Conflict;
import edu.byu.cs.rtblaser.classscheduler.view.ConflictsFragment;

/**
 * Created by RyanBlaser on 10/7/17.
 */

public class MyConflictsRecyclerViewAdapter extends RecyclerView.Adapter<MyConflictsRecyclerViewAdapter.Holder> {
    private List<Conflict> data;

    public interface MyViewHolderClickHandler {
        void onViewClicked(View caller, int whichOne);
        void onImageViewClicked(ImageView callerImage, int whichOne);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mConflict;
        public ImageView mTrashCan;
        public MyCoursesRecyclerViewAdapter.MyViewHolderClickHandler mListener;

        public Holder(View itemLayoutView, MyCoursesRecyclerViewAdapter.MyViewHolderClickHandler listener) {
            super(itemLayoutView);
            mConflict = (TextView) itemLayoutView.findViewById(R.id.course_name);
            mConflict.setOnClickListener(this);
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

    public MyConflictsRecyclerViewAdapter(List<Conflict> data) {
        this.data = data;
    }
    @Override
    public MyConflictsRecyclerViewAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_item, parent, false);
        MyCoursesRecyclerViewAdapter.MyViewHolderClickHandler handler = new MyCoursesRecyclerViewAdapter.MyViewHolderClickHandler() {
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
                    ConflictsFragment.updateAllConflictsRemoved();
                }
            }
        };

        MyConflictsRecyclerViewAdapter.Holder holder = new MyConflictsRecyclerViewAdapter.Holder(view, handler);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyConflictsRecyclerViewAdapter.Holder holder, int position) {
        Conflict conflict = data.get(position);
        holder.mConflict.setText(conflict.toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
