package edu.byu.cs.rtblaser.classscheduler.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by RyanBlaser on 10/4/17.
 */

public class Departments {
    private Departments() {
        departments = new ArrayList<>();
    }

    public static Departments SINGLETON = new Departments();

    private List<String> departments;

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }
}
