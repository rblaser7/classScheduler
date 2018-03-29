package edu.byu.cs.rtblaser.classscheduler.model;
import android.support.annotation.NonNull;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Section implements Comparable<Section> {
	public Section() {
		
	}

//	"sequence": "parent",
//            "permission_code_accepted": "false",
//            "permission_code_required": "false",
//            "add_action": null,
//            "limitations": null,
//            "section": "001",
//            "section_type": null,
//            "available_seats": "23",
//            "section_size": "25",
//            "waitlist_count": "0",
//            "lab_quiz_section": null,
//            "credit_hours": "4.0",
//            "class_period": "8:00a - 8:50a",
//            "days": "MWF",
//            "room": "W112",
//            "building": "BNSN",
//            "honors": "false",
//            "na1": "001",
//            "instructor": "Hansen, Jaron C",
//            "section_begin_date": "2018-01-08",
//            "section_end_date": "2018-04-18",
//            "course_fee": null


//    "sequence": "child",
//            "permission_code_accepted": "false",
//            "permission_code_required": "false",
//            "add_action": null,
//            "limitations": null,
//            "section": "001",
//            "section_type": null,
//            "available_seats": "23",
//            "section_size": "0",
//            "waitlist_count": "0",
//            "lab_quiz_section": null,
//            "credit_hours": "4.0",
//            "class_period": "8:00a - 8:50a",
//            "days": "TTh",
//            "room": "W005",
//            "building": "BNSN",
//            "honors": "false",
//            "na1": "001",
//            "instructor": null,
//            "section_begin_date": null,
//            "section_end_date": null,
//            "course_fee": null
	public Section(int seatsAvail, Map<String, List<Time[]>> dTT, String title, String sectionNumber) {
		seatsAvailable = seatsAvail;
		dayToTime = dTT;
		courseTitle = title;
        this.sectionNumber = sectionNumber;
        weight = 20;
	}
    public Section(String title, boolean permissionCodeRequired, String sectionNumber, int seatsAvail, int sectionSize,
                   int waitlistSize, double creditHours, Map<String, List<Time[]>> dTT, String roomNumber, String building,
                   String instructor, String courseFee) {
        courseTitle = title;
        this.permissionCodeRequired = permissionCodeRequired;
        this.sectionNumber = sectionNumber;
        seatsAvailable = seatsAvail;
        this.sectionSize = sectionSize;
        this.waitlistSize = waitlistSize;
        this.creditHours = creditHours;
        dayToTime = dTT;
        this.roomNumber = roomNumber;
        this.building = building;
        this.instructor = instructor;
        this.courseFee = courseFee;
        weight = 20;
    }
	String courseTitle;
    boolean permissionCodeRequired;
	String sectionNumber;
	int seatsAvailable;
    int sectionSize;
	int waitlistSize;
    double creditHours;
	Map<String, List<Time[]>> dayToTime;
    String roomNumber;
	String building;
	String instructor;
    String courseFee;
	// To determine if course has multiple days and times, check the response field "sequence" for "parent" and "child"

    int weight;
	
	public boolean conflictsWithScheduledSection(Section scheduledSection) {
		Iterator<String> iter = dayToTime.keySet().iterator();
		while(iter.hasNext()) {
			String day = iter.next();
			List<Time[]> times = dayToTime.get(day);
            if (times != null) {
                for (Time[] startEnd : times) {
                    Time startTime = startEnd[0];
                    Time endTime = startEnd[1];
                    List<Time[]> scheduledTimes = scheduledSection.dayToTime.get(day);
                    if (scheduledTimes != null) {
                        for (Time[] scheduledStartEnd : scheduledTimes) {
                            Time scheduledStartTime = scheduledStartEnd[0];
                            Time scheduledEndTime = scheduledStartEnd[1];
                            boolean conflictsWithStart = (startTime.after(scheduledStartTime) && startTime.before(scheduledEndTime))
                                    || startTime.equals(scheduledStartTime) || startTime.equals(scheduledEndTime);
                            boolean conflictsWithEnd = (endTime.after(scheduledStartTime) && endTime.before(scheduledEndTime))
                                    || endTime.equals(scheduledStartTime) || endTime.equals(scheduledEndTime);
                            boolean edgeCases = startTime.before(scheduledStartTime) && endTime.after(scheduledEndTime);
                            if (conflictsWithStart || conflictsWithEnd || edgeCases) {
                                return true;
                            }
                        }
                    }
                }
            }
		}
		return false;
	}

	public void addDaysAndTimes(Map<String, List<Time[]>> dtt) {

    }

	public String getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

    public boolean isPermissionCodeRequired() {
        return permissionCodeRequired;
    }

    public void setPermissionCodeRequired(boolean permissionCodeRequired) {
        this.permissionCodeRequired = permissionCodeRequired;
    }

    public double getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(double creditHours) {
        this.creditHours = creditHours;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(String courseFee) {
        this.courseFee = courseFee;
    }

    public int getSeatsAvailable() {
		return seatsAvailable;
	}

	public void setSeatsAvailable(int seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}

	public int getSectionSize() {
		return sectionSize;
	}

	public void setSectionSize(int classSize) {
		this.sectionSize = classSize;
	}

	public int getWaitlistSize() {
		return waitlistSize;
	}

	public void setWaitlistSize(int waitlistSize) {
		this.waitlistSize = waitlistSize;
	}

	public Map<String, List<Time[]>> getDayToTime() {
		return dayToTime;
	}

	public void setDayToTime(Map<String, List<Time[]>> dayToTime) {
		this.dayToTime = dayToTime;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}
	
	

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public boolean isAvailable() {
		return (seatsAvailable > 0);
	}

	public String displayDaysAndTimes() {
		StringBuilder stringBuilder = new StringBuilder();
		List<String> daysMWF = new ArrayList<>();
		List<String> daysTTh = new ArrayList<>();
		List<Time[]> mondayTimes = null;
        if(dayToTime.containsKey("TBA")) {
            return "\tTBA\n";
        }
        if(dayToTime.containsKey("Daily")) {
            return oneDay(dayToTime.get("Daily"), "Daily").toString();
        }
		if (dayToTime.containsKey("M")) {
			mondayTimes = dayToTime.get("M");
			daysMWF.add("M");
		}
		List<Time[]> tuesdayTimes = null;
		if (dayToTime.containsKey("Tu")) {
			tuesdayTimes = dayToTime.get("Tu");
			daysTTh.add("Tu");
		}
		List<Time[]> wednesdayTimes = null;
		if (dayToTime.containsKey("W")) {
			wednesdayTimes = dayToTime.get("W");
			daysMWF.add("W");
		}
		List<Time[]> thursdayTimes = null;
		if (dayToTime.containsKey("Th")) {
			thursdayTimes = dayToTime.get("Th");
			daysTTh.add("Th");
		}
		List<Time[]> fridayTimes = null;
		if (dayToTime.containsKey("F")) {
			fridayTimes = dayToTime.get("F");
			daysMWF.add("F");
		}
		switch(daysMWF.size()) {
            case 0:
                break;
            case 1:
                String day = daysMWF.get(0);
                switch (day) {
                    case "M":
                        stringBuilder.append(oneDay(mondayTimes, "M"));
                        break;
                    case "W":
                        stringBuilder.append(oneDay(wednesdayTimes, "W"));
                        break;
                    case "F":
                        stringBuilder.append(oneDay(fridayTimes, "F"));
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                String days = daysMWF.get(0) + daysMWF.get(1);
                switch (days) {
                    case "MW":
                        stringBuilder.append(twoDays(mondayTimes, wednesdayTimes, days));
                        break;
                    case "MF":
                        stringBuilder.append(twoDays(mondayTimes, fridayTimes, days));
                        break;
                    case "WF":
                        stringBuilder.append(twoDays(wednesdayTimes, fridayTimes, days));
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                if (mondayTimes.equals(wednesdayTimes) && mondayTimes.equals(fridayTimes)) {
                    stringBuilder.append("MWF");
                    for (Time[] startEnd : mondayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                } else if (mondayTimes.equals(wednesdayTimes)) {
                    stringBuilder.append("MW");
                    for (Time[] startEnd : mondayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                    stringBuilder.append("F");
                    for (Time[] startEnd : fridayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                } else if (mondayTimes.equals(fridayTimes)) {
                    stringBuilder.append("MF");
                    for (Time[] startEnd : mondayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                    stringBuilder.append("W");
                    for (Time[] startEnd : wednesdayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                } else if (wednesdayTimes.equals(fridayTimes)) {
                    stringBuilder.append("WF");
                    for (Time[] startEnd : wednesdayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                    stringBuilder.append("M");
                    for (Time[] startEnd : mondayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                } else {
                    stringBuilder.append("M");
                    for (Time[] startEnd : mondayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                    stringBuilder.append("W");
                    for (Time[] startEnd : wednesdayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                    stringBuilder.append("F");
                    for (Time[] startEnd : fridayTimes) {
                        stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                                + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
                    }
                }
                break;
            default:
                break;
        }
        switch(daysTTh.size()) {
            case 0:
                break;
            case 1:
                String day = daysTTh.get(0);
                switch (day) {
                    case "Tu":
                        stringBuilder.append(oneDay(tuesdayTimes, "Tu"));
                        break;
                    case "Th":
                        stringBuilder.append(oneDay(thursdayTimes, "Th"));
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                stringBuilder.append(twoDays(tuesdayTimes, thursdayTimes, "TuTh"));
                break;
            default:
                break;
        }

		return stringBuilder.toString();
	}

	private StringBuilder oneDay(List<Time[]> day1, String day) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(day);
        for (Time[] startEnd : day1) {
            stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                    + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
        }
        return stringBuilder;
    }

	private StringBuilder twoDays(List<Time[]> day1, List<Time[]> day2, String days) {
        StringBuilder stringBuilder = new StringBuilder();
        if (day1.equals(day2)) {
            stringBuilder.append(days);
            for (Time[] startEnd : day1) {
                stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                        + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
            }
        } else {
            if (days.equals("TuTh")) {
                stringBuilder.append(days.substring(0,1));
            } else {
                stringBuilder.append(days.charAt(0));
            }
            for (Time[] startEnd : day1) {
                stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                        + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
            }
            if (days.equals("TuTh")) {
                stringBuilder.append(days.substring(2));
            } else {
                stringBuilder.append(days.charAt(1));
            }
            for (Time[] startEnd : day2) {
                stringBuilder.append("\t" + Conflict.formatTime(startEnd[0].getHours(), startEnd[0].getMinutes()) + " - "
                        + Conflict.formatTime(startEnd[1].getHours(), startEnd[1].getMinutes()) + "\n");
            }
        }
        return stringBuilder;
    }

    public Section weightByPreferences(Preference preferences) {
        final int NUM_TIMES_OF_DAY = 4;
        String[] days = new String[] {"M", "Tu", "W", "Th", "F", "S"};
        Time[] times = new Time[] {new Time(7,0,0),new Time(12,0,0),new Time(16,0,0), new Time(22,0,0)};
        boolean[] preferredDays = preferences.getDays();
        boolean[][] preferredTimeOfDay = preferences.getDayToTimes();
        for (int i = 0; i < preferredDays.length; i++) {
            if (dayToTime.containsKey(days[i])) {
                if (preferredDays[i]) {
                    weight += 5;
                } else {
                    weight -= 3;
                }
                List<Time[]> startEnds = dayToTime.get(days[i]);
                for (int j = 0; j < NUM_TIMES_OF_DAY; j++) {
                    for (Time[] startEnd : startEnds) {
                        boolean pref = preferredTimeOfDay[i][j];
                        if (j == 0) {
                            if (pref) {
                                weight += 3;
                                break;
                            }
                            j++;
                        }
                        Time time = times[j - 1];
                        Time start = startEnd[0];
                        pref = pref && (start.equals(time) || (start.after(time) && start.before(times[j])));
                        if (pref) {
                            weight++;
                        }
                    }
                }
            }
        }
        return this;
    }

    @Override
    public int compareTo(@NonNull Section s) {
        if (this.weight > s.weight) return -1;
        if (this.weight < s.weight) return 1;
        return 0;
    }

    public void addAllTimes(Map<String, List<Time[]>> dtt) {
        Iterator iter = dtt.keySet().iterator();
        while(iter.hasNext()) {
            String day = (String)iter.next();
            if (!dayToTime.containsKey(day)) {
                dayToTime.put(day, dtt.get(day));
            } else {
                List<Time[]> newTimes = dtt.get(day);
                for (int i = 0; i < newTimes.size(); i++) {
                    if (!dayToTime.get(day).contains(newTimes.get(i))){
                        dayToTime.get(day).add(newTimes.get(i));
                    }
                }
            }
        }
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append(courseTitle + "\nSection " + sectionNumber
                + "\nInstructor: " + instructor + "\nCredit Hours: " + creditHours + "\n" + displayDaysAndTimes()
                + "Location: " + building + " " + roomNumber + "\n");
        if (!courseFee.equals("null")) {
            sb.append("Course fee: $" + courseFee + '\n');
        }
        return sb.toString();
    }
}
