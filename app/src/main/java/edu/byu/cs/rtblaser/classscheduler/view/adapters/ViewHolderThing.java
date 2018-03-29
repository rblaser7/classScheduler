package edu.byu.cs.rtblaser.classscheduler.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;

/**
 * Created by RyanBlaser on 10/9/17.
 */

public class ViewHolderThing {
    CheckBox mAnyTimeBox;
    CheckBox mMorningBox;
    CheckBox mAfternoonBox;
    CheckBox mEveningBox;


    public ViewHolderThing() {

    }

    public CheckBox getmAnyTimeBox() {
        return mAnyTimeBox;
    }

    public void setmAnyTimeBox(CheckBox mAnyTimeBox) {
        this.mAnyTimeBox = mAnyTimeBox;
    }

    public CheckBox getmMorningBox() {
        return mMorningBox;
    }

    public void setmMorningBox(CheckBox mMorningBox) {
        this.mMorningBox = mMorningBox;
    }

    public CheckBox getmAfternoonBox() {
        return mAfternoonBox;
    }

    public void setmAfternoonBox(CheckBox mAfternoonBox) {
        this.mAfternoonBox = mAfternoonBox;
    }

    public CheckBox getmEveningBox() {
        return mEveningBox;
    }

    public void setmEveningBox(CheckBox mEveningBox) {
        this.mEveningBox = mEveningBox;
    }
}
