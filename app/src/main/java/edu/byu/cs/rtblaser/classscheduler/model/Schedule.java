package edu.byu.cs.rtblaser.classscheduler.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Schedule {
	public Schedule() {
		classes = new ArrayList<>();
	}
	List<Section> classes;
	Map<String, Conflict> conflicts;
	final int MAX_NUMBER_OF_CREDITS = 18;
	
	public void addSection(Section sec) {
		classes.add(sec);
	}

	public List<Section> getClasses() {
		return classes;
	}

	public void setClasses(List<Section> classes) {
		this.classes = classes;
	}
	
	public void print() {
		for (Section section : classes) {
			System.out.println(section.getCourseTitle() + "\n");
		}
	}
}
