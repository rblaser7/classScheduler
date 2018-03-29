package edu.byu.cs.rtblaser.classscheduler.api;

import android.content.Context;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.byu.cs.rtblaser.classscheduler.view.CoursesFragment;

import static org.junit.Assert.*;

/**
 * Created by RyanBlaser on 10/4/17.
 */

public class ApiConnectorTest {
    ApiConnector apiConnector = new ApiConnector();
    @Test
    public void apiConnectorWorks() throws Exception {
        apiConnector.getDepartments();
    }
}