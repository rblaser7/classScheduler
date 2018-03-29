package edu.byu.cs.rtblaser.classscheduler.api;

import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.rtblaser.classscheduler.ScheduleActivity;
import edu.byu.cs.rtblaser.classscheduler.SchedulerActivity;
import edu.byu.cs.rtblaser.classscheduler.model.Conflict;
import edu.byu.cs.rtblaser.classscheduler.model.Departments;
import edu.byu.cs.rtblaser.classscheduler.model.Course;
import edu.byu.cs.rtblaser.classscheduler.model.Preference;
import edu.byu.cs.rtblaser.classscheduler.model.Scheduler;
import edu.byu.cs.rtblaser.classscheduler.model.Section;
import edu.byu.cs.rtblaser.classscheduler.view.CoursesFragment;
import edu.byu.cs.rtblaser.classscheduler.view.PreferencesFragment;

/**
 * Created by RyanBlaser on 10/4/17.
 */

public class ApiConnector {
    public static ApiConnector SINGLETON = new ApiConnector();
    protected Context context;
    protected String bearer;
    protected String year;
    protected String term;
    protected int coursesCompleted;
    protected List<Course> courses;
    protected Map<String, List<Conflict>> conflicts;

    public Course getSectionsForCourse(Course course) {
        try {
            String department = course.getDepartment();
            if (department.contains(" ")) {
                String[] tokens = department.split(" ");
                department = "";
                for (int i = 0; i < tokens.length - 1; i++) {
                    department += tokens[i] + "%20";
                }
                department += tokens[tokens.length - 1];
            }
            URL url = new URL("https://api.byu.edu/domains/legacy/academic/registration/offerings/v1/" + year + term
                    + "/" + department + "/" + course.getCurriculumId() + "/" + course.getTitleCode());
            GetSectionsTask task = new GetSectionsTask(course);
            task.execute(url);
            return course;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getDepartments() {
        try {
            URL url = new URL("https://api.byu.edu/domains/legacy/academic/registration/offerings/v1/" + year + term);
            GetDepartmentsTask task = new GetDepartmentsTask();
            task.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void getCoursesFromDepartment(String department) {
        try {
            if (department.contains(" ")) {
                String[] tokens = department.split(" ");
                department = "";
                for (int i = 0; i < tokens.length - 1; i++) {
                    department += tokens[i] + "%20";
                }
                department += tokens[tokens.length - 1];
            }
            URL url = new URL("https://api.byu.edu/domains/legacy/academic/registration/offerings/v1/" + year + term + "/" + department);
            GetCourseNumbersTask task = new GetCourseNumbersTask();
            task.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class GetDepartmentsTask extends AsyncTask<URL, Integer, JSONObject> {

        protected JSONObject doInBackground(URL... urls) {
            HttpsClient httpsClient = new HttpsClient();
            String urlContent = httpsClient.getUrl(urls[0], null, bearer);
            if (urlContent != null) {
                try {
                    JSONObject object = new JSONObject(urlContent);
                    return object;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                final List<String> departments = new ArrayList<>();
                departments.add("Department");
                try {
                    JSONArray teachingAreas = response.getJSONObject("RegOfferingsService").
                            getJSONObject("response").getJSONArray("teaching_areas ");
                    for (int i = 0; i < teachingAreas.length(); i++) {
                        JSONObject area = teachingAreas.getJSONObject(i);
                        String department = area.getString("department");
                        departments.add(department);
                    }
                    Departments.SINGLETON.setDepartments(departments);
                    Intent intent = new Intent(context, SchedulerActivity.class);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "We are currently unable to retreive the scheduling data from BYU. We apologize for the inconvenience.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GetCourseNumbersTask extends AsyncTask<URL, Integer, JSONObject> {

        protected JSONObject doInBackground(URL... urls) {
            HttpsClient httpsClient = new HttpsClient();
            String urlContent = httpsClient.getUrl(urls[0], null, bearer);
            if (urlContent != null) {
                try {
                    JSONObject object = new JSONObject(urlContent);
                    return object;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                final List<String> courseNums = new ArrayList<>();
                final Map<String, String[]> numToCidAndTc = new HashMap<>();
                courseNums.add("Course #");
                try {
                    JSONArray courses = response.getJSONObject("RegOfferingsService").
                            getJSONObject("response").getJSONArray("courses ");
                    for (int i = 0; i < courses.length(); i++) {
                        JSONObject course = courses.getJSONObject(i);
                        String num = course.getString("catalog_number");
                        courseNums.add(num);
                        numToCidAndTc.put(num, new String[] {course.getString("curriculum_id"), course.getString("title_code")});
                    }
                    CoursesFragment.setCourseNums(courseNums);
                    CoursesFragment.setNumberToCIdandTC(numToCidAndTc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class GetSectionsTask extends AsyncTask<URL, Integer, JSONObject> {

        protected Course theCourse;
        public GetSectionsTask(Course course) {
            theCourse = course;
        }

        protected JSONObject doInBackground(URL... urls) {
            HttpsClient httpsClient = new HttpsClient();
            String urlContent = httpsClient.getUrl(urls[0], null, bearer);
            if (urlContent != null) {
                try {
                    JSONObject object = new JSONObject(urlContent);
                    return object;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    JSONArray sections = response.getJSONObject("RegOfferingsService").getJSONObject("response").
                            getJSONArray("course_catalog_information").getJSONObject(0).getJSONArray("course_sections");
                    int numberOfSections = 0;
                    for (int i = 0; i < sections.length(); i++) {
                        Section theSection;
                        JSONObject section = sections.getJSONObject(i);
                        if (section.getString("sequence").equals("child")) {
                            theSection = theCourse.getSections().get(numberOfSections - 1);
                        } else {
                            theSection = new Section(
                                    theCourse.getCourseTitle(), section.getBoolean("permission_code_required"), section.getString("section"), section.getInt("available_seats"),
                                    section.getInt("section_size"), section.getInt("waitlist_count"), section.getDouble("credit_hours"), new HashMap<String, List<Time[]>>(),
                                    section.getString("room"), section.getString("building"), section.getString("instructor"), section.getString("course_fee")
                            );
                            theCourse.addSection(theSection);
                            numberOfSections++;
                        }

                        Map<String, List<Time[]>> dtt = new HashMap<>();

                        String days = section.getString("days");
                        List<String> theDays = new ArrayList<>();
                        if (days.equals("Daily") || days.equals("TBA")) {
                            theDays.add(days);
                        } else {
                            for (int j = 0; j < days.length(); j++) {
                                char day = days.charAt(j);
                                String theDay;
                                if (day == 'T' && (j + 1 < days.length() && days.charAt(j+1) == 'h')) {
                                    theDay = days.substring(j, ++j + 1);
                                } else if (day == 'T') {
                                    theDay = "Tu";
                                } else {
                                    theDay = Character.toString(days.charAt(j));
                                }
                                theDays.add(theDay);
                            }
                        }

                        String class_period = section.getString("class_period");
                        Time startTime = null;
                        Time endTime = null;
                        if (!class_period.equals("TBA")) {
                            String[] times = class_period.split(" - ");
                            for (int j = 0; j < times.length; j++) {
                                String[] hoursMinutes = times[j].split(":");
                                int hour = Integer.valueOf(hoursMinutes[0]);
                                int minutes = Integer.valueOf(hoursMinutes[1].substring(0,2));
                                if (hour != 12 && hoursMinutes[1].contains("p")) {
                                    hour += 12;
                                }
                                if (j == 0) {
                                    startTime = new Time(hour, minutes, 0);
                                } else {
                                    endTime = new Time(hour, minutes, 0);
                                }
                            }
                            List<Time[]> timeList = new ArrayList<Time[]>();
                            timeList.add(new Time[] {startTime, endTime});
                            for(String day : theDays) {
                                if (!dtt.containsKey(theDays)) {
                                    dtt.put(day, timeList);
                                }
                            }
                        } else {
                            dtt.put("TBA", null);
                        }

                        theSection.addAllTimes(dtt);
                    }
                    coursesCompleted++;
                    if (coursesCompleted == courses.size()) {
                        try {
                            coursesCompleted = 0;
                            Scheduler.SINGLETON.makeSchedule(courses, conflicts, Preference.SINGLETON);
                            Intent intent = new Intent(context, ScheduleActivity.class);
                            context.startActivity(intent);
                        } catch (Scheduler.AllSectionsConflictWithConflictsException e) {
                            coursesCompleted = 0;
                            String message = e.getMessage();
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            SchedulerActivity.setCurrentTabIndex(0);
                            ActionBar ab = ((AppCompatActivity)context).getSupportActionBar();
                            ActionBar.Tab coursesTab = ab.getTabAt(SchedulerActivity.getCurrentTabIndex());
                            ab.selectTab(coursesTab);
                        } catch (Scheduler.NoPossibleScheduleException e) {
                            coursesCompleted = 0;
                            String message = e.getMessage();
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            SchedulerActivity.setCurrentTabIndex(0);
                            ActionBar ab = ((AppCompatActivity)context).getSupportActionBar();
                            ActionBar.Tab coursesTab = ab.getTabAt(SchedulerActivity.getCurrentTabIndex());
                            ab.selectTab(coursesTab);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setConflicts(Map<String, List<Conflict>> conflicts) {
        this.conflicts = conflicts;
    }
}
