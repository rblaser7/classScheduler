package edu.byu.cs.rtblaser.classscheduler.model;
import java.util.List;
import java.util.Map;

public class Preference {
	final int NUM_OF_DAYS = 6;
	final int NUM_OF_TIMES = 4;

	public static Preference SINGLETON = new Preference();

	private Preference() {
		for (int i = 0; i < NUM_OF_DAYS; i++) {
			days[i] = false;
			for (int j = 0; j < NUM_OF_TIMES; j++) {
				dayToTimes[i][j] = false;
			}
		}
	}

	boolean[] days = new boolean[NUM_OF_DAYS];
	boolean[][] dayToTimes = new boolean[NUM_OF_DAYS][NUM_OF_TIMES];

	public void setTimeInDayToTimes(int day, int time, boolean value) {
		dayToTimes[day][time] = value;
	}

	public boolean getTimeInDayToTimes(int day, int time) {
		return dayToTimes[day][time];
	}

	public void setDayInDays(int day, boolean value) {
		days[day] = value;
	}

	public boolean getDayInDays(int day) {
		return days[day];
	}

	public boolean[] getDays() {
		return days;
	}

	public void setDays(boolean[] days) {
		this.days = days;
	}

	public boolean[][] getDayToTimes() {
		return dayToTimes;
	}

	public void setDayToTimes(boolean[][] dayToTimes) {
		this.dayToTimes = dayToTimes;
	}
}
