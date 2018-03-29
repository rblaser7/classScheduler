package edu.byu.cs.rtblaser.classscheduler.model;

import java.sql.Time;

import edu.byu.cs.rtblaser.classscheduler.view.ConflictsFragment;

public class Conflict {
	public Conflict() {
		
	}

	public Conflict(String day, Time start, Time end) {
		this.day = day;
		startEndTimes = new Time[] {start, end};
	}
	String day;
	Time[] startEndTimes;
	
	public Time getStartTime() {
		return startEndTimes[0];
	}
	
	public Time getEndTime() {
		return startEndTimes[1];
	}
	
	public Time[] getStartEndTimes() {
		return startEndTimes;
	}

	public void setStartEndTimes(Time[] startEndTimes) {
		this.startEndTimes = startEndTimes;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}

	@Override
	public String toString() {
//		Time noon = new Time(12,0,0);
		Time startTime = getStartTime();
		Time endTime = getEndTime();
		String start = formatTime(startTime.getHours(), startTime.getMinutes());
		String end = formatTime(endTime.getHours(), endTime.getMinutes());
		return day + "\t\t" + start + " - " + end;
	}

	public static String formatTime(int hour, int minute) {
		String hourText;
		String minuteText;
		String amOrPm;
		if (hour > 12) {
			hourText = Integer.toString(hour - 12);
			amOrPm = " PM";
		} else if (hour == 0) {
			hourText = "12";
			amOrPm = " AM";
		} else if (hour == 12) {
			hourText = "12";
			amOrPm = " PM";
		} else {
			hourText = Integer.toString(hour);
			amOrPm = " AM";
		}
		if (minute < 10) {
			minuteText = "0" + Integer.toString(minute);
		} else {
			minuteText = Integer.toString(minute);
		}
		return hourText + ":" + minuteText + amOrPm;
	}
}
